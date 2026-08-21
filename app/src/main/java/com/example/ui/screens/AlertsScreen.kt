package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.FarmAlert
import com.example.ui.theme.*

@Composable
fun AlertsScreen(
    alerts: List<FarmAlert>,
    onMarkRead: (String) -> Unit
) {
    var selectedSeverity by remember { mutableStateOf("ALL") }
    val severities = listOf("ALL", "CRITICAL", "HIGH", "MEDIUM", "LOW")

    val filteredAlerts = if (selectedSeverity == "ALL") {
        alerts
    } else {
        alerts.filter { it.severity == selectedSeverity }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Farm Alert & Advisory Center",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Automated proactive alerts for weather, disease vulnerability, and mandi market spikes",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Severity Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                severities.forEach { sev ->
                    FilterChip(
                        selected = selectedSeverity == sev,
                        onClick = { selectedSeverity = sev },
                        label = { Text(sev, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredAlerts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No alerts in this category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(filteredAlerts) { alert ->
            val isUrgent = alert.severity == "HIGH" || alert.severity == "CRITICAL"
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUrgent) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (alert.category) {
                                    "WEATHER" -> Icons.Default.Thunderstorm
                                    "DISEASE" -> Icons.Default.Coronavirus
                                    else -> Icons.Default.TrendingUp
                                },
                                contentDescription = null,
                                tint = if (isUrgent) StatusAttention else AgriHarvestGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = alert.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (alert.severity) {
                                "CRITICAL" -> Color(0xFFFFCDD2)
                                "HIGH" -> Color(0xFFFFE0B2)
                                "MEDIUM" -> Color(0xFFFFF9C4)
                                else -> Color(0xFFE8F5E9)
                            }
                        ) {
                            Text(
                                text = "${alert.severity} PRIORITY",
                                color = when (alert.severity) {
                                    "CRITICAL", "HIGH" -> StatusAttention
                                    "MEDIUM" -> Color(0xFFE65100)
                                    else -> StatusHealthy
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = alert.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = AgriGreenContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.TaskAlt, contentDescription = null, tint = AgriGreenDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Recommended Action: ${alert.actionableStep}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark
                            )
                        }
                    }
                }
            }
        }
    }
}
