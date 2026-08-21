package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Farm
import com.example.data.model.FarmerProfile
import com.example.ui.NavigationScreen
import com.example.ui.theme.*
import com.example.util.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriTopBar(
    profile: FarmerProfile?,
    selectedFarm: Farm?,
    allFarms: List<Farm>,
    currentScreen: NavigationScreen,
    onFarmSelected: (String) -> Unit,
    onLanguageClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateTo: (NavigationScreen) -> Unit
) {
    var farmMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentScreen != NavigationScreen.HOME && currentScreen != NavigationScreen.ONBOARDING) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    // Brand Icon & Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onNavigateTo(NavigationScreen.HOME) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AgriGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "AgriVoice Logo",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "AgriVoice AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(StatusHealthy)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (profile?.isDemoMode == true) "Demo Mode • Online" else "Cloud Connected",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Right Actions: Farm Switcher & Language Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Farm Dropdown Pill
                    if (selectedFarm != null) {
                        Box {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.clickable { farmMenuExpanded = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Agriculture,
                                        contentDescription = "Farm",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = selectedFarm.name.take(10) + if (selectedFarm.name.length > 10) ".." else "",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = farmMenuExpanded,
                                onDismissRequest = { farmMenuExpanded = false }
                            ) {
                                Text(
                                    text = "Select Active Farm",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                allFarms.forEach { farm ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(farm.name, fontWeight = if (farm.id == selectedFarm.id) FontWeight.Bold else FontWeight.Normal)
                                                Text("${farm.sizeAcres} Acres • ${farm.mainCrop}", style = MaterialTheme.typography.bodySmall)
                                            }
                                        },
                                        onClick = {
                                            onFarmSelected(farm.id)
                                            farmMenuExpanded = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = if (farm.id == selectedFarm.id) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                                contentDescription = null,
                                                tint = if (farm.id == selectedFarm.id) StatusHealthy else MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    )
                                }
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Manage All Farms (+)") },
                                    onClick = {
                                        farmMenuExpanded = false
                                        onNavigateTo(NavigationScreen.MY_FARMS)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.AddBusiness, contentDescription = null)
                                    }
                                )
                            }
                        }
                    }

                    // Language Selector Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.clickable { onLanguageClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val lang = LanguageManager.getLanguage(profile?.languageCode ?: "en")
                            Text(
                                text = "${lang.flagEmoji} ${lang.nativeName.take(3)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AgriBottomBar(
    currentScreen: NavigationScreen,
    onNavigateTo: (NavigationScreen) -> Unit,
    onVoiceClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                title = "Home",
                icon = Icons.Default.Home,
                selected = currentScreen == NavigationScreen.HOME,
                onClick = { onNavigateTo(NavigationScreen.HOME) }
            )

            BottomNavItem(
                title = "My Farms",
                icon = Icons.Default.Agriculture,
                selected = currentScreen == NavigationScreen.MY_FARMS,
                onClick = { onNavigateTo(NavigationScreen.MY_FARMS) }
            )

            // Center Raised Animated Talk Button
            Box(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AgriGreenPrimary)
                    .clickable { onVoiceClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Assistant",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            BottomNavItem(
                title = "Market",
                icon = Icons.Default.Storefront,
                selected = currentScreen == NavigationScreen.MARKET || currentScreen == NavigationScreen.PROFIT_SIMULATOR,
                onClick = { onNavigateTo(NavigationScreen.MARKET) }
            )

            BottomNavItem(
                title = "Profile",
                icon = Icons.Default.Person,
                selected = currentScreen == NavigationScreen.PROFILE,
                onClick = { onNavigateTo(NavigationScreen.PROFILE) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            fontSize = 11.sp
        )
    }
}
