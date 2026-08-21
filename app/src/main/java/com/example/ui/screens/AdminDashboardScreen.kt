package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Enterprise Admin & Cloud Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Multi-agent telemetry, language breakdown & AWS Production Architecture",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Platform KPIs
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📈 Platform Telemetry (Past 24 Hours)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AdminKpiItem(label = "Queries Handled", value = "1,482", color = AgriGreenPrimary)
                        AdminKpiItem(label = "Avg AI Latency", value = "420 ms", color = AgriSkyBlue)
                        AdminKpiItem(label = "CV Accuracy", value = "94.2%", color = StatusHealthy)
                        AdminKpiItem(label = "Active Farms", value = "3,210", color = AgriHarvestGold)
                    }
                }
            }
        }

        // Language Distribution
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌐 Voice Language Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LanguageShareBar(lang = "Kannada (ಕನ್ನಡ)", percent = 38, color = AgriGreenPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    LanguageShareBar(lang = "Hindi (हिंदी)", percent = 28, color = AgriHarvestGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LanguageShareBar(lang = "Telugu (తెలుగు)", percent = 18, color = AgriSkyBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    LanguageShareBar(lang = "English & Others", percent = 16, color = Color(0xFF7B1FA2))
                }
            }
        }

        // AWS Cloud Architecture Blueprint
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF232F3E)), // AWS Dark Slate
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CloudQueue, contentDescription = null, tint = Color(0xFFFF9900), modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AWS Production Cloud Architecture",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Production backend mapping for secure scalability and zero secret frontend exposure:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    AwsComponentCard(
                        service = "AWS Cognito",
                        desc = "Farmer phone OTP authentication, JWT token vending & RBAC identity pools."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AwsComponentCard(
                        service = "Amazon Bedrock & Gemini 3.5",
                        desc = "Multi-agent LLM orchestrator for Crop, Weather, Soil & Disease specialization."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AwsComponentCard(
                        service = "AWS Lambda & API Gateway",
                        desc = "Serverless REST endpoints for query processing, weather cache & market data ingestion."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AwsComponentCard(
                        service = "Amazon DynamoDB",
                        desc = "Ultra-low latency NoSQL store for Farmer Profiles, Farm Parcels, Crop Cycles & Chat Logs."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AwsComponentCard(
                        service = "Amazon S3",
                        desc = "Encrypted object storage for high-res leaf scans, audio voice clips & satellite NDVI tiles."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AwsComponentCard(
                        service = "Amazon EventBridge & SNS",
                        desc = "Proactive SMS & push notifications for extreme weather warnings and mandi spikes."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    AwsComponentCard(
                        service = "AWS IoT Core",
                        desc = "MQTT broker streaming field soil moisture sensor telemetry into DynamoDB."
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminKpiItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }
}

@Composable
private fun LanguageShareBar(lang: String, percent: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(lang, style = MaterialTheme.typography.labelMedium)
            Text("$percent%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun AwsComponentCard(service: String, desc: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF131A22),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = service,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9900)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE8EFEA),
                fontSize = 11.sp
            )
        }
    }
}
