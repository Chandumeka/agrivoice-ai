package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var animStage by remember { mutableStateOf(0) }

    val scale by animateFloatAsState(
        targetValue = when (animStage) {
            0 -> 0.7f
            1 -> 1.0f
            2 -> 1.15f
            else -> 1.0f
        },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "splashScale"
    )

    LaunchedEffect(Unit) {
        delay(400)
        animStage = 1 // Sprout
        delay(500)
        animStage = 2 // Plant
        delay(600)
        animStage = 3 // Harvest & Complete
        delay(700)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AgriGreenDark, AgriGreenPrimary, AgriBackgroundDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (animStage) {
                        0 -> "🌱" // Seed
                        1 -> "🌿" // Sprout
                        2 -> "🪴" // Plant
                        else -> "🌾" // Harvest
                    },
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "AgriVoice AI",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "Personal Multilingual AI Farming Agent",
                style = MaterialTheme.typography.bodyMedium,
                color = AgriGreenContainer,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            CircularProgressIndicator(
                color = AgriHarvestGold,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
