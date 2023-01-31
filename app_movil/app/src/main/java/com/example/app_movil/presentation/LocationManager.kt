package com.example.app_movil.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.HandlerThread
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.app_movil.presentation.data.WeatherApi
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class LocationManager(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    data class LocationUiState(
        val isLoading: Boolean = false,
        val error: Throwable? = null,
        val locations: List<CardData> = emptyList(),
    )

    private val locations: MutableList<CardData> = mutableListOf()
    private val _uiState = MutableStateFlow(LocationUiState(isLoading = true))
    val uiState: StateFlow<LocationUiState> = _uiState

    fun createLocationRequest(
        longitude: Double = 0.0,
        latitude: Double = 0.0,
    ) {
        _uiState.value = LocationUiState(isLoading = true)
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, 2000
        ).build()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        try {
            val handler = HandlerThread("RequestLocation")
            handler.start()
            fusedLocationClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            val lat = if (latitude != 0.0) latitude else location.latitude
                            val long = if (longitude != 0.0) longitude else location.longitude
                            CoroutineScope(Dispatchers.IO).launch {
                                val weatherDTO = WeatherApi.apiInstance.getWeatherDetails(
                                    lat,
                                    long,
                                    "0a3ef84c03da5be5c9dc302b7f347ce9"
                                )
                                locations.add(
                                    CardData(
                                        weatherInfo = weatherDTO.weather[0].description,
                                        time = "${(weatherDTO.main.temp - 273.15).roundToInt()}°C",
                                        name = weatherDTO.name,
                                        temp = (weatherDTO.main.temp - 273).roundToInt().toDouble()
                                    )
                                )
                                _uiState.value = LocationUiState(
                                    isLoading = false,
                                    locations = locations
                                )
                            }
                        }

                        fusedLocationClient.removeLocationUpdates(this)
                        handler.quit()
                    }
                }, handler.looper
            )
        } catch (e: Exception) {
            _uiState.value = LocationUiState(
                isLoading = false,
                locations = emptyList(),
                error = e
            )
            Log.d("LCT", "error: ${e.message}")
        }
    }
}