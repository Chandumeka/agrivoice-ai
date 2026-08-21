package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.NavigationScreen
import com.example.ui.theme.*
import com.example.util.LanguageManager

@Composable
fun HomeScreen(
    profile: FarmerProfile?,
    selectedFarm: Farm?,
    cropCycle: CropCycle?,
    alerts: List<FarmAlert>,
    marketPrices: List<MarketPrice>,
    isBriefingPlaying: Boolean,
    onPlayBriefing: () -> Unit,
    onStopBriefing: () -> Unit,
    onOpenVoice: () -> Unit,
    onNavigateTo: (NavigationScreen) -> Unit,
    onMarkAlertRead: (String) -> Unit
) {
    val lang = profile?.languageCode ?: "en"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // 1. Farmer Hero Greeting & Morning Briefing Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.img_farmer_avatar),
                                contentDescription = "Farmer Avatar",
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${LanguageManager.getString("greeting_morning", lang)},",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = profile?.name ?: "Ramesh Kumar",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Selected Farm Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AgriGreenContainer,
                            modifier = Modifier.clickable { onNavigateTo(NavigationScreen.MY_FARMS) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = AgriGreenDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = selectedFarm?.name ?: "Farm A",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriGreenDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Morning Audio Briefing Player Banner
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(AgriGreenPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isBriefingPlaying) Icons.Default.GraphicEq else Icons.Default.Headphones,
                                        contentDescription = "Audio Briefing",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = LanguageManager.getString("today_briefing", lang),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "Rain tomorrow • Drip pause • Tomato ₹2,850/Qtl",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (isBriefingPlaying) onStopBriefing() else onPlayBriefing()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isBriefingPlaying) StatusCritical else AgriGreenPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBriefingPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBriefingPlaying) "Stop" else "Listen",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Large Central Primary Action: "TALK TO AGRIVOICE"
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = AgriGreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenVoice() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(AgriGreenPrimary, AgriGreenLight)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🎙️",
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = LanguageManager.getString("talk_to_agrivoice", lang),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = LanguageManager.getString("ask_anything_subtitle", lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Mic",
                                tint = AgriGreenPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. Quick Feature Launch Grid
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Smart Agricultural Tools",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolCard(
                        title = "Crop Scanner",
                        subtitle = "Disease AI Vision",
                        icon = Icons.Default.DocumentScanner,
                        bgColor = Color(0xFFE8F5E9),
                        iconTint = StatusHealthy,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo(NavigationScreen.CROP_SCANNER) }
                    )
                    QuickToolCard(
                        title = "Smart Irrigation",
                        subtitle = "Moisture & Rain",
                        icon = Icons.Default.WaterDrop,
                        bgColor = Color(0xFFE1F5FE),
                        iconTint = AgriSkyBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo(NavigationScreen.IRRIGATION) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolCard(
                        title = "Soil Analysis",
                        subtitle = "NPK & pH Guide",
                        icon = Icons.Default.Grass,
                        bgColor = Color(0xFFFFF3E0),
                        iconTint = AgriHarvestGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo(NavigationScreen.SOIL) }
                    )
                    QuickToolCard(
                        title = "Mandi Prices",
                        subtitle = "APMC Trends",
                        icon = Icons.Default.TrendingUp,
                        bgColor = Color(0xFFF3E5F5),
                        iconTint = Color(0xFF7B1FA2),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo(NavigationScreen.MARKET) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolCard(
                        title = "Crop Calendar",
                        subtitle = "Growth Timeline",
                        icon = Icons.Default.CalendarMonth,
                        bgColor = Color(0xFFE0F2F1),
                        iconTint = Color(0xFF00796B),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo(NavigationScreen.CROP_CALENDAR) }
                    )
                    QuickToolCard(
                        title = "Farm Map",
                        subtitle = "Zones & Satellite",
                        icon = Icons.Default.Map,
                        bgColor = Color(0xFFEDE7F6),
                        iconTint = Color(0xFF512DA8),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo(NavigationScreen.FARM_MAP) }
                    )
                }
            }
        }

        // 4. Farm Health & Current Crop Stage Card
        item {
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
                        Text(
                            text = LanguageManager.getString("farm_health", lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (selectedFarm?.healthStatus) {
                                "Healthy" -> StatusHealthy.copy(alpha = 0.15f)
                                "Watch" -> StatusWatch.copy(alpha = 0.15f)
                                else -> StatusAttention.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = selectedFarm?.healthStatus ?: "Healthy",
                                color = when (selectedFarm?.healthStatus) {
                                    "Healthy" -> StatusHealthy
                                    "Watch" -> StatusWatch
                                    else -> StatusAttention
                                },
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Health Score Progress Dial
                        val score = selectedFarm?.healthScore ?: 88
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { score / 100f },
                                modifier = Modifier.size(64.dp),
                                color = if (score >= 80) StatusHealthy else if (score >= 65) StatusWatch else StatusAttention,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                strokeWidth = 7.dp
                            )
                            Text(
                                text = "$score",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "${cropCycle?.cropName ?: "Tomato"} (${cropCycle?.variety ?: "Arka Rakshak"})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Stage: ${cropCycle?.currentStage ?: "Flowering"} • Day ${cropCycle?.daysSincePlanting ?: 48} of ${cropCycle?.totalEstimatedDays ?: 110}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (cropCycle?.stageProgressPercent ?: 65) / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = AgriGreenPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "💡 ${cropCycle?.currentAdvisory ?: "Maintain optimal soil moisture and apply calcium foliar spray."}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 5. Weather & Irrigation Overview Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTo(NavigationScreen.WEATHER) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Cloud, contentDescription = null, tint = AgriSkyBlue)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Kolar Weather & Irrigation",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Details ›",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherStatPill(icon = Icons.Default.Thermostat, label = "27°C", sub = "Partly Cloudy")
                        WeatherStatPill(icon = Icons.Default.WaterDrop, label = "82%", sub = "Humidity")
                        WeatherStatPill(icon = Icons.Default.Grain, label = "18 mm", sub = "Rain Tomorrow")
                        WeatherStatPill(icon = Icons.Default.Air, label = "14 km/h", sub = "Gentle Wind")
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = AgriSkyContainer.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = AgriSkyBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Irrigation recommendation: Skip today's drip cycle due to forecast rain.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 6. Mandi Prices Ticker Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTo(NavigationScreen.MARKET) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = AgriHarvestGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mandi Prices (APMC)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Live Updates ›",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(marketPrices.take(4)) { item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.width(160.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = item.cropName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = item.mandiName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "₹${item.modalPriceQuintal.toInt()}/Q",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = if (item.priceTrend == "UP") "+${item.priceChangePercent}%" else "${item.priceChangePercent}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.priceTrend == "UP") StatusHealthy else StatusAttention,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. Important Alerts
        if (alerts.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "High Priority Alerts",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "View All (${alerts.size}) ›",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateTo(NavigationScreen.ALERTS) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    alerts.take(2).forEach { alert ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = when (alert.severity) {
                                "CRITICAL" -> Color(0xFFFFEBEE)
                                "HIGH" -> Color(0xFFFFF3E0)
                                else -> MaterialTheme.colorScheme.surface
                            },
                            tonalElevation = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = when (alert.category) {
                                        "WEATHER" -> Icons.Default.Thunderstorm
                                        "DISEASE" -> Icons.Default.Coronavirus
                                        else -> Icons.Default.NotificationsActive
                                    },
                                    contentDescription = null,
                                    tint = when (alert.severity) {
                                        "HIGH", "CRITICAL" -> StatusAttention
                                        else -> AgriHarvestGold
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = alert.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = alert.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Action: ${alert.actionableStep}",
                                        style = MaterialTheme.typography.labelSmall,
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
    }
}

@Composable
private fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bgColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun WeatherStatPill(
    icon: ImageVector,
    label: String,
    sub: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = AgriSkyBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
    }
}
