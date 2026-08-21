package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.DiseaseAnalysisResult
import com.example.ui.theme.*

@Composable
fun CropScannerScreen(
    isScanning: Boolean,
    scanProgress: Float,
    analysisResult: DiseaseAnalysisResult?,
    onScanImage: () -> Unit,
    onResetScanner: () -> Unit,
    onRequestExpertReview: (issue: String, preliminary: String, confidence: Int) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "AI Crop Health Scanner",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Point camera at affected leaves to diagnose plant diseases and pests instantly",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Scanner Viewport Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, AgriGreenPrimary, RoundedCornerShape(20.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_crop_leaf),
                contentDescription = "Sample Crop Leaf",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Scanning laser effect
            if (isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.04f)
                        .align(Alignment.TopCenter)
                        .offset(y = (240 * laserOffset).dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xFF00E676), Color.Transparent)
                            )
                        )
                )

                // Overlay progress
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = StatusHealthy,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Analyzing with Computer Vision...",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bounding box overlay for identified spots
            if (analysisResult != null && !isScanning) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = 20.dp, y = (-20).dp)
                        .border(2.dp, StatusWatch, RoundedCornerShape(8.dp))
                ) {
                    Surface(
                        color = StatusWatch,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "Lesion Area 14%",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scanner Action Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onScanImage,
                enabled = !isScanning,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan Leaf Image", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onResetScanner,
                enabled = !isScanning && analysisResult != null,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(0.6f)
            ) {
                Text("Retake")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Analysis Results Card
        if (analysisResult != null) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                                text = "Diagnosed Issue",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = analysisResult.possibleIssue,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Severity Chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (analysisResult.severity) {
                                "High" -> StatusAttention.copy(alpha = 0.15f)
                                "Medium" -> StatusWatch.copy(alpha = 0.15f)
                                else -> StatusHealthy.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = "${analysisResult.severity} Severity",
                                color = when (analysisResult.severity) {
                                    "High" -> StatusAttention
                                    "Medium" -> StatusWatch
                                    else -> StatusHealthy
                                },
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Confidence and stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ResultStatItem(label = "AI Confidence", value = "${analysisResult.confidencePercent}%", color = StatusHealthy)
                        ResultStatItem(label = "Affected Foliage", value = "${analysisResult.affectedAreaPercent}%", color = StatusWatch)
                        ResultStatItem(label = "Crop Identified", value = "Tomato", color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(14.dp))

                    // Symptoms
                    Text(
                        text = "Observed Symptoms:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    analysisResult.symptoms.forEach { symptom ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                            Text(text = symptom, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Immediate Action Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AgriGoldContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "⚠️ Immediate Recommended Step",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5E3F00)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = analysisResult.immediateAction,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF422B00)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Organic Remedy
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AgriGreenContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🌱 Organic / Bio-Control Solution",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = analysisResult.organicRemedy,
                                style = MaterialTheme.typography.bodySmall,
                                color = AgriOnGreenContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Chemical Option
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🧪 Verified IPM Chemical Alternative",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = analysisResult.chemicalTreatment,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Trust Notice & Expert Escalation Button
                    Text(
                        text = "Trust Notice: AI diagnostic output is an estimate based on visual symptoms. If uncertainty persists, connect with our agricultural extension officer.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            onRequestExpertReview(
                                "Early blight symptoms with leaf curling on tomato.",
                                analysisResult.possibleIssue,
                                analysisResult.confidencePercent
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Request Expert Scientist Review", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }
}
