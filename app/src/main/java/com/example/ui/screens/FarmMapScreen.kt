package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Farm
import com.example.ui.theme.*

@Composable
fun FarmMapScreen(selectedFarm: Farm?) {
    var isNdviLayerActive by remember { mutableStateOf(false) }
    var selectedZone by remember { mutableStateOf("Zone 1 (North - Tomato)") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Farm Map & Remote Monitoring",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Parcel boundaries, IoT moisture nodes & Sentinel-2 NDVI satellite layer",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Map Canvas Box
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${selectedFarm?.name ?: "Farm A"} (2.5 Acres)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // NDVI Toggle Pill
                        FilterChip(
                            selected = isNdviLayerActive,
                            onClick = { isNdviLayerActive = !isNdviLayerActive },
                            label = { Text(if (isNdviLayerActive) "NDVI: ON" else "NDVI: OFF", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.SatelliteAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StatusHealthy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2D Custom Parcel Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isNdviLayerActive) Color(0xFF1B382B) else Color(0xFF2C3E35))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw Zone 1 (Tomato - Healthy)
                            drawRect(
                                color = if (isNdviLayerActive) Color(0xFF4CAF50).copy(alpha = 0.7f) else Color(0xFF388E3C).copy(alpha = 0.5f),
                                topLeft = Offset(w * 0.05f, h * 0.08f),
                                size = Size(w * 0.55f, h * 0.84f)
                            )

                            // Draw Zone 2 (Nursery / Boundary)
                            drawRect(
                                color = if (isNdviLayerActive) Color(0xFF8BC34A).copy(alpha = 0.7f) else Color(0xFF689F38).copy(alpha = 0.5f),
                                topLeft = Offset(w * 0.63f, h * 0.08f),
                                size = Size(w * 0.32f, h * 0.40f)
                            )

                            // Draw Zone 3 (Storage / Pump House)
                            drawRect(
                                color = Color(0xFF8D6E63).copy(alpha = 0.6f),
                                topLeft = Offset(w * 0.63f, h * 0.52f),
                                size = Size(w * 0.32f, h * 0.40f)
                            )

                            // Draw Drip Lines (White dashes)
                            for (i in 1..4) {
                                drawLine(
                                    color = Color.White.copy(alpha = 0.4f),
                                    start = Offset(w * 0.08f, h * (0.15f + i * 0.14f)),
                                    end = Offset(w * 0.55f, h * (0.15f + i * 0.14f)),
                                    strokeWidth = 2f
                                )
                            }

                            // IoT Sensor Pins
                            drawCircle(color = Color(0xFF00E5FF), radius = 10f, center = Offset(w * 0.25f, h * 0.45f))
                            drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.25f, h * 0.45f))

                            drawCircle(color = Color(0xFF00E5FF), radius = 10f, center = Offset(w * 0.45f, h * 0.70f))
                            drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.45f, h * 0.70f))
                        }

                        // Map Legend in overlay
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = if (isNdviLayerActive) "🌿 NDVI Index: 0.78 (Vigorous Canopy)" else "📍 2 IoT Moisture Nodes Active",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MapZonePill(label = "Zone 1: Tomato (1.8 Ac)", status = "Healthy (NDVI 0.78)")
                        MapZonePill(label = "Zone 2: Nursery (0.5 Ac)", status = "Good (NDVI 0.65)")
                    }
                }
            }
        }

        // Remote Satellite Intelligence Details
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🛰️ Satellite Pass Details (Sentinel-2)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Last Satellite Pass: 19 Aug 2026 (Cloud cover: 8%)\n• Canopy Uniformity: 92% across rows\n• Moisture Anomaly: None detected in root zone",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MapZonePill(label: String, status: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.width(160.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(status, style = MaterialTheme.typography.bodySmall, color = StatusHealthy, fontSize = 10.sp)
        }
    }
}
