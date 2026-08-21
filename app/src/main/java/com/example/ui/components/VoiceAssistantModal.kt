package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.VoiceUiState
import com.example.ui.theme.*
import com.example.util.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceAssistantDialog(
    voiceState: VoiceUiState,
    transcription: String,
    audioLevels: List<Float>,
    chatMessages: List<ChatMessage>,
    currentLangCode: String,
    onStartListening: () -> Unit,
    onSubmitQuery: (String) -> Unit,
    onStopVoice: () -> Unit,
    onDismiss: () -> Unit,
    onLanguageClick: () -> Unit
) {
    // Pulsing animation for microphone
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (voiceState == VoiceUiState.LISTENING) 1.25f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotateAnim"
    )

    ModalBottomSheet(
        onDismissRequest = {
            onStopVoice()
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.surface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AgriGreenPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AgriVoice AI Assistant",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Language quick chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.clickable { onLanguageClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val l = LanguageManager.getLanguage(currentLangCode)
                        Text(
                            text = "${l.flagEmoji} ${l.name}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Central Microphone Visualizer Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AgriGreenContainer.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Outer Pulse Ring when listening
                if (voiceState == VoiceUiState.LISTENING) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(AgriGreenLight.copy(alpha = 0.2f))
                    )
                }

                // Central Mic Button
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            when (voiceState) {
                                VoiceUiState.LISTENING -> AgriGreenLight
                                VoiceUiState.PROCESSING -> AgriHarvestGold
                                VoiceUiState.RESPONDING -> AgriSkyBlue
                                else -> AgriGreenPrimary
                            }
                        )
                        .clickable {
                            if (voiceState == VoiceUiState.LISTENING) {
                                onStopVoice()
                            } else {
                                onStartListening()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (voiceState) {
                            VoiceUiState.LISTENING -> Icons.Default.Mic
                            VoiceUiState.PROCESSING -> Icons.Default.Psychology
                            VoiceUiState.RESPONDING -> Icons.Default.VolumeUp
                            else -> Icons.Default.MicNone
                        },
                        contentDescription = "Microphone",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                // Status text inside visualizer box
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = when (voiceState) {
                            VoiceUiState.LISTENING -> "Listening... Speak naturally"
                            VoiceUiState.PROCESSING -> "Analyzing farm conditions & data..."
                            VoiceUiState.RESPONDING -> "Responding with voice advisory..."
                            VoiceUiState.ERROR -> "Voice error. Tap to retry"
                            else -> "Tap microphone to speak"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dynamic Audio Waveform Bars
            if (voiceState == VoiceUiState.LISTENING) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    audioLevels.forEach { lvl ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(6.dp)
                                .height((lvl * 32).coerceIn(4f, 32f).dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(AgriGreenPrimary)
                        )
                    }
                }
            }

            // Quick Farmer Query Suggestions
            if (voiceState == VoiceUiState.IDLE) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Or tap a common question:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = { onSubmitQuery("What should I do on my farm today?") },
                            label = { Text("What should I do today?", fontSize = 12.sp) }
                        )
                        SuggestionChip(
                            onClick = { onSubmitQuery("Is it going to rain tomorrow?") },
                            label = { Text("Rain forecast?", fontSize = 12.sp) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = { onSubmitQuery("Should I irrigate my tomato crop today?") },
                            label = { Text("Should I irrigate?", fontSize = 12.sp) }
                        )
                        SuggestionChip(
                            onClick = { onSubmitQuery("What is today's tomato mandi price?") },
                            label = { Text("Mandi price?", fontSize = 12.sp) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            // Conversation History List
            Text(
                text = "Recent Advisory History",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(chatMessages.reversed()) { msg ->
                    ChatBubbleItem(msg = msg)
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(msg: ChatMessage) {
    val isUser = msg.role == "user"
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) AgriGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🤖 ${msg.specialistAgent}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Confidence ${(msg.confidence * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusHealthy,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = msg.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )

                if (!isUser && msg.dataSource.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Source: ${msg.dataSource}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}
