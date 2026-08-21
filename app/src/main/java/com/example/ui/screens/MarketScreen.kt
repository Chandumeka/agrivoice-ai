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
import com.example.data.model.MarketPrice
import com.example.ui.NavigationScreen
import com.example.ui.theme.*

@Composable
fun MarketScreen(
    marketPrices: List<MarketPrice>,
    onNavigateTo: (NavigationScreen) -> Unit
) {
    var selectedCropFilter by remember { mutableStateOf("All") }
    val crops = listOf("All", "Tomato", "Paddy", "Chilli", "Cotton", "Onion")

    val filteredList = if (selectedCropFilter == "All") {
        marketPrices
    } else {
        marketPrices.filter { it.cropName.contains(selectedCropFilter, ignoreCase = true) }
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
                text = "Mandi Intelligence & Rates",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Live APMC mandi prices, transport deductions & optimal selling window",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Profit Simulator Hero Action Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AgriHarvestGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTo(NavigationScreen.PROFIT_SIMULATOR) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "💰 Crop Profit Simulator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3E2700)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Calculate expected harvest revenue, costs, net profit & ROI with dynamic sliders",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF5E3F00)
                        )
                    }
                    Button(
                        onClick = { onNavigateTo(NavigationScreen.PROFIT_SIMULATOR) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E2700)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Simulate", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Crop Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                crops.forEach { crop ->
                    FilterChip(
                        selected = selectedCropFilter == crop,
                        onClick = { selectedCropFilter = crop },
                        label = { Text(crop, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Where Should I Sell Insight Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📍 Best Selling Decision: Kolar APMC",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Although Azadpur Mandi offers ₹3,200/Q, transport from Kolar costs ₹480/Q (Net: ₹2,720). Selling locally at Kolar APMC gives ₹2,850/Q - ₹45/Q transport = Net ₹2,805/Quintal (+₹85 higher in hand).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Live Mandi List
        items(filteredList) { item ->
            val isUp = item.priceTrend == "UP"
            val netRealization = item.modalPriceQuintal - item.estimatedTransportCostQuintal

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${item.cropName} • ${item.mandiName}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${item.district}, ${item.state} • ${item.distanceKm} km away",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${item.modalPriceQuintal.toInt()}/Q",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                    contentDescription = null,
                                    tint = if (isUp) StatusHealthy else StatusAttention,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = if (isUp) "+${item.priceChangePercent}%" else "${item.priceChangePercent}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUp) StatusHealthy else StatusAttention
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Range: ₹${item.minPriceQuintal.toInt()} - ₹${item.maxPriceQuintal.toInt()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Net In-Hand: ₹${netRealization.toInt()}/Q",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = StatusHealthy
                        )
                    }
                }
            }
        }
    }
}
