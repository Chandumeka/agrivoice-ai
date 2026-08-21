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
import com.example.ui.ProfitCalculation
import com.example.ui.theme.*

@Composable
fun ProfitSimulatorScreen(
    acres: Double,
    yieldPerAcre: Double,
    costPerAcre: Double,
    priceMin: Double,
    priceMax: Double,
    profitCalc: ProfitCalculation,
    onUpdateInputs: (acres: Double, yield: Double, cost: Double, priceMin: Double, priceMax: Double) -> Unit
) {
    var currentAcres by remember { mutableStateOf(acres) }
    var currentYield by remember { mutableStateOf(yieldPerAcre) }
    var currentCost by remember { mutableStateOf(costPerAcre) }
    var currentPriceMin by remember { mutableStateOf(priceMin) }
    var currentPriceMax by remember { mutableStateOf(priceMax) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Crop Profit Simulator",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Simulate harvest economics, total investment, break-even yield & net ROI",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Summary Net Profit Highlight Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Estimated Net Profit Range",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${profitCalc.netProfitMin.toInt().coerceAtLeast(0)} – ₹${profitCalc.netProfitMax.toInt()}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriGreenDark
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SimStatItem(label = "Total Investment", value = "₹${profitCalc.totalCost.toInt()}")
                        SimStatItem(label = "Total Output", value = "${(currentAcres * currentYield).toInt()} Qtl")
                        SimStatItem(label = "ROI Range", value = "${profitCalc.roiPercentMin.toInt()}% - ${profitCalc.roiPercentMax.toInt()}%")
                        SimStatItem(label = "Break-Even", value = "${profitCalc.breakEvenYieldQuintals.toInt()} Qtl")
                    }
                }
            }
        }

        // Interactive Sliders Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚙️ Adjust Farming Variables",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 1. Farm Size
                    SliderRow(
                        label = "Cultivation Area",
                        displayValue = "${String.format("%.1f", currentAcres)} Acres",
                        value = currentAcres.toFloat(),
                        range = 0.5f..10f,
                        steps = 19,
                        onValueChange = {
                            currentAcres = it.toDouble()
                            onUpdateInputs(currentAcres, currentYield, currentCost, currentPriceMin, currentPriceMax)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Expected Yield
                    SliderRow(
                        label = "Expected Yield per Acre",
                        displayValue = "${currentYield.toInt()} Quintals",
                        value = currentYield.toFloat(),
                        range = 20f..150f,
                        steps = 26,
                        onValueChange = {
                            currentYield = it.toDouble()
                            onUpdateInputs(currentAcres, currentYield, currentCost, currentPriceMin, currentPriceMax)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. Input Cost per Acre
                    SliderRow(
                        label = "Input & Labor Cost / Acre",
                        displayValue = "₹${currentCost.toInt()}",
                        value = currentCost.toFloat(),
                        range = 15000f..90000f,
                        steps = 15,
                        onValueChange = {
                            currentCost = it.toDouble()
                            onUpdateInputs(currentAcres, currentYield, currentCost, currentPriceMin, currentPriceMax)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4. Expected Market Price (Max)
                    SliderRow(
                        label = "Expected Selling Rate / Quintal",
                        displayValue = "₹${currentPriceMax.toInt()}",
                        value = currentPriceMax.toFloat(),
                        range = 1000f..5000f,
                        steps = 40,
                        onValueChange = {
                            currentPriceMax = it.toDouble()
                            currentPriceMin = (it * 0.75).toDouble()
                            onUpdateInputs(currentAcres, currentYield, currentCost, currentPriceMin, currentPriceMax)
                        }
                    )
                }
            }
        }

        // Financial Insights & Safety Margins
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📊 Economic Safety Buffer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Break-Even Price: At current yield (${currentYield.toInt()} Q/acre), you break even at ₹${(currentCost / currentYield).toInt()}/Quintal.\n• Margin of Safety: Current mandi price (₹${currentPriceMax.toInt()}) offers a +${((currentPriceMax - (currentCost/currentYield))/(currentCost/currentYield)*100).toInt()}% safety margin above production costs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderRow(
    label: String,
    displayValue: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(displayValue, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = AgriGreenPrimary,
                activeTrackColor = AgriGreenPrimary
            )
        )
    }
}

@Composable
private fun SimStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = AgriGreenDark)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f), fontSize = 10.sp)
    }
}
