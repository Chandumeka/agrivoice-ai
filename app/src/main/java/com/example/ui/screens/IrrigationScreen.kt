package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CropCycle
import com.example.data.model.Farm
import com.example.ui.theme.*

@Composable
fun IrrigationScreen(
    farm: Farm?,
    cropCycle: CropCycle?
) {
    var isDripPaused by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Smart Irrigation Management",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Data-driven irrigation schedule powered by crop water balance and soil sensors",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Today's Decision Recommendation Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = AgriSkyBlue, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Today's AI Irrigation Call",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StatusWatch.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "PAUSE RECOMMENDED",
                                color = StatusWatch,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Do not irrigate today.",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Reason: 18.5mm rain forecast tomorrow in Kolar will replenish root zone moisture. Soil moisture is currently 68% (optimal range: 60-75%). Saving ~18,000 Liters of water and 2.5 kWh electricity.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Drip Line Auto-Pause", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Text("Suspends scheduled valve cycle", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isDripPaused,
                            onCheckedChange = { isDripPaused = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AgriGreenPrimary)
                        )
                    }
                }
            }
        }

        // Soil Moisture Sensor Readings
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌱 Root Zone Soil Moisture (IoT Node #1)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar for moisture
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Moisture: 68%", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Target: 60-75%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { 0.68f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = AgriSkyBlue,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MoistureStatItem(label = "Topsoil (0-15cm)", value = "65%")
                        MoistureStatItem(label = "Subsoil (15-45cm)", value = "71%")
                        MoistureStatItem(label = "Evapotranspiration", value = "4.2 mm/day")
                    }
                }
            }
        }

        // Weekly Schedule
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📅 7-Day Planned Water Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ScheduleItem(day = "Fri (Today)", duration = "0 mins", status = "PAUSED (Rain Imminent)", color = StatusWatch)
                    ScheduleItem(day = "Sat (Tomorrow)", duration = "0 mins", status = "NATURAL RAIN (18mm)", color = AgriSkyBlue)
                    ScheduleItem(day = "Sun (24 Aug)", duration = "0 mins", status = "POST-RAIN DRYOUT", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ScheduleItem(day = "Mon (25 Aug)", duration = "45 mins", status = "DRIP RUN (Early Morning)", color = StatusHealthy)
                    ScheduleItem(day = "Tue (26 Aug)", duration = "0 mins", status = "REST DAY", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ScheduleItem(day = "Wed (27 Aug)", duration = "45 mins", status = "DRIP RUN (Fertigation)", color = StatusHealthy)
                }
            }
        }
    }
}

@Composable
private fun MoistureStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AgriSkyBlue)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }
}

@Composable
private fun ScheduleItem(day: String, duration: String, status: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(day, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(duration, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color.copy(alpha = 0.15f)
        ) {
            Text(
                text = status,
                color = color,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 10.sp
            )
        }
    }
}
