package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.util.LanguageManager

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (name: String, lang: String, loc: String, acres: Double, crop: String) -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var selectedLang by remember { mutableStateOf("en") }
    var farmerName by remember { mutableStateOf("Ramesh Kumar") }
    var location by remember { mutableStateOf("Kolar District, Karnataka") }
    var farmAcres by remember { mutableStateOf("2.5") }
    var mainCrop by remember { mutableStateOf("Tomato") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AgriVoice AI Setup",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Step $step of 3",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Center Content based on step
        when (step) {
            1 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AgriGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Choose Your Language / भाषा चुनें",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Speak & listen in your comfortable native language",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        LanguageManager.languages.take(6).forEach { lang ->
                            val isSel = lang.code == selectedLang
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedLang = lang.code }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(lang.flagEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${lang.nativeName} (${lang.name})",
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AgriHarvestGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Farmer Profile & Region",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = farmerName,
                        onValueChange = { farmerName = it },
                        label = { Text("Your Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("District & State (e.g. Kolar, Karnataka)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            3 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AgriSkyBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Agriculture, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Primary Farm Details",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = farmAcres,
                        onValueChange = { farmAcres = it },
                        label = { Text("Farm Size in Acres") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = mainCrop,
                        onValueChange = { mainCrop = it },
                        label = { Text("Current Main Crop (e.g. Tomato, Paddy, Chilli)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Bottom Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = { step-- },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Back")
                }
            } else {
                Spacer(modifier = Modifier.width(10.dp))
            }

            Button(
                onClick = {
                    if (step < 3) {
                        step++
                    } else {
                        val acresVal = farmAcres.toDoubleOrNull() ?: 2.5
                        onCompleteOnboarding(farmerName, selectedLang, location, acresVal, mainCrop)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
            ) {
                Text(if (step == 3) "Start Farming with AI" else "Continue", fontWeight = FontWeight.Bold)
            }
        }
    }
}
