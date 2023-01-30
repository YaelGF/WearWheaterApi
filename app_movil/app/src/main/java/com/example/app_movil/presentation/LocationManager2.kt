package com.example.app_movil.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.example.app_movil.presentation.data.WeatherApi
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class LocationManager2 {


    val dataLoaded = mutableStateOf(false)

    val data = mutableStateOf(
        CardData(weatherInfo = "", time = "", name = "", temp = 0.0)
    )

    fun createLocationRequest(
        context: Context,
        fusedLocationclient: FusedLocationProviderClient,
        longitude: Double,
        latitude: Double
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000
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
        fusedLocationclient.requestLocationUpdates(
            locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val weatherDTO = WeatherApi.apiInstance.getWeatherDetails(
                                // latitud 20.085833,
                                // longitud -98.363333,
                                latitude,
                                longitude,
                                "0a3ef84c03da5be5c9dc302b7f347ce9"
                            )
                            dataLoaded.value = true
                            data.value = CardData(
                                weatherInfo = weatherDTO.weather[0].description,
                                time = "${(weatherDTO.main.temp - 273.15).roundToInt()}°C",
                                name = weatherDTO.name,
                                temp = (weatherDTO.main.temp - 273).roundToInt().toDouble()
                            )
                        }
                    }
                }
            }, Looper.getMainLooper()
        )
    }
}