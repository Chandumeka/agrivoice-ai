package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.CropCycle
import com.example.ui.theme.*

data class GrowthStage(
    val stageName: String,
    val dayRange: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean,
    val taskDesc: String
)

@Composable
fun CropCalendarScreen(cropCycle: CropCycle?) {
    val stages = listOf(
        GrowthStage("Nursery & Seed Sowing", "Days 1 – 25", isCompleted = true, isCurrent = false, "Raised bed nursery, Trichoderma seed treatment."),
        GrowthStage("Transplanting & Rooting", "Days 25 – 35", isCompleted = true, isCurrent = false, "Transplant in paired rows with drip lateral lines."),
        GrowthStage("Vegetative Growth", "Days 35 – 45", isCompleted = true, isCurrent = false, "Staking with bamboo poles, first top-dressing of nitrogen."),
        GrowthStage("Flowering & Fruit Setting", "Days 45 – 70", isCompleted = false, isCurrent = true, "Foliar Calcium-Boron spray, monitor thrips and blight."),
        GrowthStage("Fruit Enlargement", "Days 70 – 90", isCompleted = false, isCurrent = false, "High potassium fertigation for fruit size and firmness."),
        GrowthStage("Harvesting & Marketing", "Days 90 – 110", isCompleted = false, isCurrent = false, "Pick at breaker stage for distant mandis, red ripe for local.")
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
                text = "Crop Growth Calendar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${cropCycle?.cropName ?: "Tomato"} (${cropCycle?.variety ?: "Arka Rakshak"}) • Planted: ${cropCycle?.plantingDate ?: "03 Jul 2026"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Summary Progress Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Stage: Flowering",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Day ${cropCycle?.daysSincePlanting ?: 48} of ${cropCycle?.totalEstimatedDays ?: 110} (62 days to harvest)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            text = "${cropCycle?.stageProgressPercent ?: 65}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgriGreenDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { (cropCycle?.stageProgressPercent ?: 65) / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = AgriGreenPrimary,
                        trackColor = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Timeline Stages
        items(stages) { stage ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Stepper Icon and Line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    stage.isCurrent -> AgriGreenPrimary
                                    stage.isCompleted -> StatusHealthy
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                stage.isCompleted -> Icons.Default.Check
                                stage.isCurrent -> Icons.Default.PlayArrow
                                else -> Icons.Default.HourglassEmpty
                            },
                            contentDescription = null,
                            tint = if (stage.isCompleted || stage.isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Stage Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (stage.isCurrent) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = if (stage.isCurrent) androidx.compose.foundation.BorderStroke(2.dp, AgriGreenPrimary) else null,
                    tonalElevation = if (stage.isCurrent) 3.dp else 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stage.stageName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stage.dayRange,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (stage.isCurrent) AgriGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stage.taskDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
