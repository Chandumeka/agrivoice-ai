package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class DailyForecast(
    val day: String,
    val date: String,
    val tempMax: Int,
    val tempMin: Int,
    val rainMm: Double,
    val condition: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun WeatherScreen() {
    val forecastList = listOf(
        DailyForecast("Today", "21 Aug", 28, 20, 2.0, "Partly Cloudy", Icons.Default.CloudQueue),
        DailyForecast("Tomorrow", "22 Aug", 26, 19, 18.5, "Moderate Rain", Icons.Default.Grain),
        DailyForecast("Saturday", "23 Aug", 27, 20, 8.0, "Scattered Showers", Icons.Default.Thunderstorm),
        DailyForecast("Sunday", "24 Aug", 29, 21, 0.0, "Sunny / Clear", Icons.Default.WbSunny),
        DailyForecast("Monday", "25 Aug", 30, 22, 0.0, "Mostly Sunny", Icons.Default.WbSunny),
        DailyForecast("Tuesday", "26 Aug", 28, 20, 4.5, "Light Rain", Icons.Default.Grain),
        DailyForecast("Wednesday", "27 Aug", 27, 19, 12.0, "Rain Showers", Icons.Default.Thunderstorm)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Weather Intelligence",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Hyperlocal meteorological forecast and agricultural microclimate advisories",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Today's Live Weather Hero Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Kolar District, Karnataka",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "Updated 10 mins ago • IMD Doppler Radar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "27°C",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = 44.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Partly Cloudy • Feels like 29°C",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherMetric(label = "Humidity", value = "82%", icon = Icons.Default.WaterDrop)
                        WeatherMetric(label = "Wind Speed", value = "14 km/h", icon = Icons.Default.Air)
                        WeatherMetric(label = "Rainfall Risk", value = "High (85%)", icon = Icons.Default.Grain)
                        WeatherMetric(label = "UV Index", value = "6 (Mod)", icon = Icons.Default.WbSunny)
                    }
                }
            }
        }

        // Spraying & Farm Operation Advisory
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚜 Field Operations Advisory",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AdvisoryRow(
                        title = "Foliar Spraying Window",
                        status = "UNFAVORABLE",
                        reason = "Imminent rain tomorrow will wash off applied chemicals. Postpone until Sunday.",
                        isPositive = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AdvisoryRow(
                        title = "Harvesting Window",
                        status = "GOOD TODAY",
                        reason = "Harvest mature fruits today before heavy showers start tomorrow afternoon.",
                        isPositive = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AdvisoryRow(
                        title = "Pesticide Drift Risk",
                        status = "LOW WIND",
                        reason = "Wind speed is 14 km/h (within safe threshold < 18 km/h).",
                        isPositive = true
                    )
                }
            }
        }

        // 7-Day Forecast List
        item {
            Text(
                text = "7-Day Weather Outlook",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        items(forecastList) { f ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(130.dp)
                    ) {
                        Icon(
                            imageVector = f.icon,
                            contentDescription = null,
                            tint = AgriSkyBlue,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(f.day, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(f.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Text(
                        text = "${f.rainMm} mm rain",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (f.rainMm > 10.0) StatusAttention else if (f.rainMm > 0.0) AgriSkyBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "${f.tempMax}° / ${f.tempMin}°",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherMetric(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f), fontSize = 10.sp)
    }
}

@Composable
private fun AdvisoryRow(title: String, status: String, reason: String, isPositive: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (isPositive) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = if (isPositive) StatusHealthy else StatusWatch,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isPositive) StatusHealthy.copy(alpha = 0.15f) else StatusWatch.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isPositive) StatusHealthy else StatusWatch,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(reason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
