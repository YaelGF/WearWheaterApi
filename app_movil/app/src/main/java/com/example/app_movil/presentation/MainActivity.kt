/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.app_movil.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.wear.compose.material.*
import com.example.app_movil.presentation.theme.App_movilTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("Permisos", "No se concedieron permisos")
        }

        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        val locationManager = LocationManager(this, fusedLocationProviderClient)

        setContent {
            Setup(locationManager)
        }
    }

    @Composable
    @OptIn(ExperimentalPermissionsApi::class)
    private fun Setup(locationManager: LocationManager) {
        App_movilTheme {
            locationManager.createLocationRequest()
            locationManager.createLocationRequest(-99.363333, 20.085833)
            locationManager.createLocationRequest(-98.363333, 19.085833)
            locationManager.createLocationRequest(-97.363333, 18.085833)
            locationManager.createLocationRequest(-96.363333, 16.085833)

            val locationPermissionState =
                rememberMultiplePermissionsState(
                    listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 7.dp)

            val iconModifier = Modifier
                .size(23.dp)
                .padding()
                .wrapContentSize(align = Alignment.Center)

            Column {
                if (locationPermissionState.allPermissionsGranted) {
                    val scalingLazyState = remember { ScalingLazyListState() }
                    val state = locationManager.uiState.collectAsState()

                    ScalingLazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = scalingLazyState,
                    ) {
                        item {
                            ListHeader {
                                Text(text = "Weather")
                            }
                        }

                        if (state.value.error != null) {
                            item {
                                ListHeader {
                                    Text(text = "Error: ${state.value.error?.localizedMessage}")
                                }
                            }
                        }
                        if (state.value.isLoading) {
                            item {
                                ListHeader {
                                    Text(text = "loading ....")
                                }
                            }
                        }
                        items(state.value.locations) {
                            CardComponent(
                                title = it.name,
                                weatherDes = it.weatherInfo,
                                time = it.time,
                                temp = it.temp
                            )
                            Spacer(modifier = Modifier.size(19.dp))
                        }
                        item {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(7.dp),
                                onClick = {
                                    val lat = 15.085833
                                    val long = -94.363333
                                    locationManager.createLocationRequest(long, lat)
                                }
                            ) {
                                Text("NEW LOCATION")
                            }
                        }
                    }
                } else {
                    val allpermission =
                        locationPermissionState.permissions.size == locationPermissionState.revokedPermissions.size

                    val textToShow = if (!allpermission) {
                        "Se necesita el acceso a la ubicacion"
                    } else if (locationPermissionState.shouldShowRationale) {
                        "La ubicacion exacta es necesaria"
                    } else {
                        "Para el correcto funcionamiento de la aplicacion, se necesita el acceso a la ubicacion"

                    }
                    TextComponent(contentModifier, textToShow)
                    ButtonWidget(contentModifier, iconModifier) {
                        locationPermissionState.launchMultiplePermissionRequest()
                    }

                }
            }
        }
    }
}

