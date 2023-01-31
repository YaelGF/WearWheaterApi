package com.example.app_movil.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.app_movil.R

@Composable
fun ButtonWidget(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
            onClick = { onClick() }
        ) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = "Llama la solicitud de ubicacion",
                modifier = iconModifier
            )
        }
    }
}


@Composable
fun TextComponent(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = text
    )
}

@Composable
fun CardComponent(
    modifier: Modifier = Modifier,
    title: String,
    weatherDes: String,
    time: String,
    temp: Double
) {
    AppCard(
        onClick = { /*TODO*/ },
        appName = { Text("Clima", color = Color.White) },
        time = { Text(time, color = if (temp < 27) Color.White else Color.Red) },
        title = { Text(title, color = Color.Yellow) }) {
        val icon = if (temp >= 27) R.mipmap.high else R.mipmap.low
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                modifier = Modifier.height(25.dp),
                painter = painterResource(id = icon),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(weatherDes)
        }
    }
}

/*@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WearApp(locations: List<LocationManager>) {
    var listState = rememberScalingLazyListState()

    App_movilTheme {
        val locationPermissionState =
            rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        val contentModifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)

        val iconModifier = Modifier
            .size(24.dp)
            .padding()
            .wrapContentSize(align = Alignment.Center)

        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 32.dp,
                bottom = 32.dp,
                start = 8.dp,
                end = 8.dp
            ),
            verticalArrangement = Arrangement.Bottom,
            state = listState,
            autoCentering = true // AutoCenteringParams(0)
        ) {
            item { Spacer(modifier = Modifier.size(20.dp)) }
            if (locationPermissionState.allPermissionsGranted) {
                if (!locations[0].dataLoaded.value) {
                    item {
                        TextComponent(contentModifier, "Acceso a la ubicacion Concedido")
                    }
                } else {
                    item {
                        CardComponent(
                            title = locations[0].data.value.name,
                            weatherDes = locations[0].data.value.weatherInfo,
                            time = locations[0].data.value.time,
                            temp = locations[0].data.value.temp
                        )
                    }
                    item { Spacer(modifier = Modifier.size(20.dp)) }
                    item {
                        CardComponent(
                            title = locations[1].data.value.name,
                            weatherDes = locations[1].data.value.weatherInfo,
                            time = locations[1].data.value.time,
                            temp = locations[1].data.value.temp
                        )
                    }
                    item { Spacer(modifier = Modifier.size(20.dp)) }
                    item {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = {
                                //MainActivity.addLocation(locations)
                                //addLocation(locations)
                            }
                        ) {
                            Text("NEW CARD")
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
                item { TextComponent(contentModifier, textToShow) }
                item {
                    ButtonWidget(contentModifier, iconModifier) {
                        locationPermissionState.launchMultiplePermissionRequest()
                    }
                }
            }
        }

    }
}
*/
