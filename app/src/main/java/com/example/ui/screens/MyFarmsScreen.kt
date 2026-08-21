package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Farm
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFarmsScreen(
    farms: List<Farm>,
    selectedFarm: Farm?,
    onSelectFarm: (String) -> Unit,
    onDeleteFarm: (String) -> Unit,
    onAddNewFarm: (name: String, acres: Double, crop: String, soil: String, location: String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var farmName by remember { mutableStateOf("") }
    var farmAcres by remember { mutableStateOf("") }
    var mainCrop by remember { mutableStateOf("") }
    var soilType by remember { mutableStateOf("Red Sandy Loam") }
    var location by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Farms & Parcels",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Manage your multiple farm plots, crop types, and soil records",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = AgriGreenPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Farm")
                }
            }
        }

        items(farms) { farm ->
            val isSelected = farm.id == selectedFarm?.id
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, AgriGreenPrimary) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectFarm(farm.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = null,
                                tint = if (isSelected) StatusHealthy else MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = farm.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = when (farm.healthStatus) {
                                "Healthy" -> StatusHealthy.copy(alpha = 0.15f)
                                "Watch" -> StatusWatch.copy(alpha = 0.15f)
                                else -> StatusAttention.copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                text = "${farm.healthStatus} (${farm.healthScore}/100)",
                                color = when (farm.healthStatus) {
                                    "Healthy" -> StatusHealthy
                                    "Watch" -> StatusWatch
                                    else -> StatusAttention
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FarmDetailChip(icon = Icons.Default.Straighten, label = "${farm.sizeAcres} Acres")
                        FarmDetailChip(icon = Icons.Default.LocalFlorist, label = farm.mainCrop)
                        FarmDetailChip(icon = Icons.Default.WaterDrop, label = farm.irrigationType.take(12) + "..")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "📍 ${farm.location} • Soil: ${farm.soilType}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (!isSelected) {
                            Button(
                                onClick = { onSelectFarm(farm.id) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text("Switch to this Farm", style = MaterialTheme.typography.labelSmall)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        if (farms.size > 1) {
                            IconButton(
                                onClick = { onDeleteFarm(farm.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Farm Plot", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = farmName,
                        onValueChange = { farmName = it },
                        label = { Text("Farm Name (e.g. Farm D - East)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = farmAcres,
                        onValueChange = { farmAcres = it },
                        label = { Text("Size in Acres (e.g. 3.0)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mainCrop,
                        onValueChange = { mainCrop = it },
                        label = { Text("Main Crop (e.g. Cotton, Maize, Onion)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (Village/District)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val acresNum = farmAcres.toDoubleOrNull() ?: 2.0
                        val nameStr = if (farmName.isNotBlank()) farmName else "New Farm Plot"
                        val cropStr = if (mainCrop.isNotBlank()) mainCrop else "Tomato"
                        val locStr = if (location.isNotBlank()) location else "Kolar"
                        onAddNewFarm(nameStr, acresNum, cropStr, soilType, locStr)
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("Create Farm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun FarmDetailChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
        }
    }
}
