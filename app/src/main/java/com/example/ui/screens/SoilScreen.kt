package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SoilReport
import com.example.ui.theme.*

@Composable
fun SoilScreen(soilReport: SoilReport?) {
    val report = soilReport ?: SoilReport(
        id = "sample_soil",
        farmId = "farm_a",
        testDate = "15 Jun 2026",
        ph = 6.8,
        nitrogenKgPerHa = 265.0,
        phosphorusKgPerHa = 34.0,
        potassiumKgPerHa = 310.0,
        organicCarbonPercent = 0.62,
        electricalConductivity = 0.42,
        healthSummary = "Balanced pH and rich potassium reserve. Ideal for solanaceous crops like tomato.",
        recommendedActions = "Supplement with organic compost (FYM) to boost organic carbon above 0.75%."
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
                text = "Soil Health & Nutrients",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Official Soil Health Card analysis (ICAR Lab Tested: ${report.testDate})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Soil pH & Organic Carbon Overview
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Soil Reaction & Biology",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SoilMetricCard(
                            label = "pH Level",
                            value = "${report.ph}",
                            status = if (report.ph in 6.5..7.5) "OPTIMAL (Neutral)" else "ATTENTION",
                            statusColor = StatusHealthy
                        )
                        SoilMetricCard(
                            label = "Organic Carbon",
                            value = "${report.organicCarbonPercent}%",
                            status = if (report.organicCarbonPercent >= 0.6) "MEDIUM-GOOD" else "LOW",
                            statusColor = AgriHarvestGold
                        )
                        SoilMetricCard(
                            label = "EC (Salinity)",
                            value = "${report.electricalConductivity} dS/m",
                            status = "NORMAL (< 1.0)",
                            statusColor = StatusHealthy
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "🔬 Summary: ${report.healthSummary}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Major Nutrients (NPK) Levels
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🌱 Major Macronutrients (NPK)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    NutrientBar(
                        nutrient = "Nitrogen (N)",
                        value = "${report.nitrogenKgPerHa.toInt()} kg/ha",
                        level = "Medium",
                        progress = (report.nitrogenKgPerHa / 500.0).toFloat().coerceIn(0f, 1f),
                        color = AgriGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    NutrientBar(
                        nutrient = "Phosphorus (P)",
                        value = "${report.phosphorusKgPerHa.toInt()} kg/ha",
                        level = "High",
                        progress = (report.phosphorusKgPerHa / 50.0).toFloat().coerceIn(0f, 1f),
                        color = AgriHarvestGold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    NutrientBar(
                        nutrient = "Potassium (K)",
                        value = "${report.potassiumKgPerHa.toInt()} kg/ha",
                        level = "High",
                        progress = (report.potassiumKgPerHa / 400.0).toFloat().coerceIn(0f, 1f),
                        color = AgriSkyBlue
                    )
                }
            }
        }

        // Actionable Fertilizer & Amendment Advisory
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AgriGreenContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Recommend, contentDescription = null, tint = AgriGreenDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scientific Fertilizer Advisory",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = report.recommendedActions,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AgriOnGreenContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Top-dressing: Apply 25 kg Neem-coated Urea per acre at 55 days post planting.\n• Foliar spray: 19:19:19 (5g/L) + Boron (1g/L) during early fruit setting.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AgriOnGreenContainer.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SoilMetricCard(label: String, value: String, status: String, statusColor: Color) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = statusColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = status,
                    color = statusColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun NutrientBar(nutrient: String, value: String, level: String, progress: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(nutrient, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("$value ($level)", style = MaterialTheme.typography.labelMedium, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
