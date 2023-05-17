@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.ui.theme.WeatherAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = koinViewModel()
) {
    val weather by viewModel.weatherInfo.collectAsState()
    var input by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("City") },
                modifier = modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(
                onClick = { viewModel.fetchWeather(input) },
            ) {
                Text("Search")
            }
        }
        Spacer(modifier = Modifier.height(height = 80.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val icon = weather.icon
            val url = "https://openweathermap.org/img/wn/${icon}@2x.png"
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            WeatherDetails(weather = weather)
        }
    }
}

@Composable
fun WeatherDetails(weather: WeatherUIData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, bottom = 40.dp)
    ) {
        Text(
            text = "City: ${weather.name}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Temperature: ${weather.temp}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Weather: ${weather.description}",
            style = MaterialTheme.typography.titleMedium
        )
        // Add more details as needed
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherDetailsPreview() {
    WeatherAppTheme {
        val viewModel: WeatherViewModel = koinViewModel()
        val weather by viewModel.weatherInfo.collectAsState()
        WeatherDetails(weather = weather)
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherAppTheme { WeatherScreen() }
}
