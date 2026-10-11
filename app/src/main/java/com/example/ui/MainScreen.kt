package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BackgroundAnimationMode
import com.example.model.BackgroundSettings
import com.example.ui.animation.AnimationSettingsBottomSheet
import com.example.ui.animation.BackgroundAnimationCanvas
import com.example.ui.screens.*
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent

enum class AppDestination(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    PROJECTS("Projects", Icons.Filled.AccountTree, Icons.Outlined.AccountTree),
    EXPERIENCE("Journey", Icons.Filled.Work, Icons.Outlined.WorkOutline),
    SKILLS("Skills", Icons.Filled.Star, Icons.Outlined.StarBorder),
    CONTACT("Contact", Icons.Filled.Mail, Icons.Outlined.MailOutline)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var currentDestination by remember { mutableStateOf(AppDestination.HOME) }
    var selectedProjectId by remember { mutableStateOf<String?>(null) }
    var showAnimationSettings by remember { mutableStateOf(false) }

    var backgroundSettings by remember {
        mutableStateOf(
            BackgroundSettings(
                mode = BackgroundAnimationMode.CONSTELLATION,
                speedMultiplier = 1.0f,
                glowIntensity = 0.85f,
                touchInteractive = true
            )
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Box(modifier = Modifier.fillMaxSize()) {
            // Continuous 60fps dynamic background canvas behind all content
            BackgroundAnimationCanvas(
                settings = backgroundSettings,
                isDarkTheme = isDarkTheme,
                modifier = Modifier.fillMaxSize()
            )

            if (isWideScreen) {
                // Desktop / Tablet Layout with Navigation Rail
                Row(modifier = Modifier.fillMaxSize()) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        header = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(CyanPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Analytics,
                                        contentDescription = "Logo",
                                        tint = CyanPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "TS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldAccent
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .testTag("desktop_nav_rail")
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        AppDestination.values().forEach { destination ->
                            val isSelected = currentDestination == destination
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = {
                                    currentDestination = destination
                                    selectedProjectId = null
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                        contentDescription = destination.title
                                    )
                                },
                                label = { Text(destination.title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color(0xFF0A0F1D),
                                    selectedTextColor = CyanPrimary,
                                    indicatorColor = CyanPrimary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("desktop_nav_item_${destination.name.lowercase()}")
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Quick Actions at bottom of Rail
                        IconButton(
                            onClick = { showAnimationSettings = true },
                            modifier = Modifier.testTag("rail_bg_anim_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Canvas FX", tint = CyanPrimary)
                        }

                        IconButton(
                            onClick = onToggleTheme,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .testTag("rail_theme_toggle")
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Content Area for Desktop / Tablet
                    Scaffold(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets.safeDrawing,
                        topBar = {
                            TopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldAccent)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Thrusha Sahu • Portfolio & Interactive Architecture",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                },
                                actions = {
                                    FilledTonalButton(
                                        onClick = { showAnimationSettings = true },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = CyanPrimary.copy(alpha = 0.15f),
                                            contentColor = CyanPrimary
                                        )
                                    ) {
                                        Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Background Visualizer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                                )
                            )
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = 1400.dp)
                            ) {
                                when (currentDestination) {
                                    AppDestination.HOME -> HomeScreen(
                                        isWideScreen = true,
                                        onNavigateToProject = { id ->
                                            currentDestination = AppDestination.PROJECTS
                                            selectedProjectId = id
                                        },
                                        onNavigateToProjectsList = { currentDestination = AppDestination.PROJECTS },
                                        onNavigateToExperience = { currentDestination = AppDestination.EXPERIENCE },
                                        onNavigateToContact = { currentDestination = AppDestination.CONTACT },
                                        onOpenAnimationSettings = { showAnimationSettings = true }
                                    )
                                    AppDestination.PROJECTS -> ProjectsScreen(
                                        isWideScreen = true,
                                        onNavigateToProject = { id -> selectedProjectId = id }
                                    )
                                    AppDestination.EXPERIENCE -> ExperienceScreen()
                                    AppDestination.SKILLS -> SkillsCertificationsScreen()
                                    AppDestination.CONTACT -> ContactScreen(isWideScreen = true)
                                }
                            }
                        }
                    }
                }
            } else {
                // Mobile Layout with Bottom Navigation Bar
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        if (selectedProjectId == null) {
                            TopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldAccent)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "Thrusha Sahu",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Data Analyst • ex-ServiceNow",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = CyanPrimary
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    IconButton(
                                        onClick = { showAnimationSettings = true },
                                        modifier = Modifier.testTag("open_bg_anim_settings")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "Background FX",
                                            tint = CyanPrimary
                                        )
                                    }

                                    IconButton(
                                        onClick = onToggleTheme,
                                        modifier = Modifier.testTag("toggle_theme_button")
                                    ) {
                                        Icon(
                                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                            contentDescription = "Toggle Theme",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f)
                                )
                            )
                        }
                    },
                    bottomBar = {
                        if (selectedProjectId == null) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                                tonalElevation = 6.dp,
                                windowInsets = WindowInsets.navigationBars,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                AppDestination.values().forEach { destination ->
                                    val isSelected = currentDestination == destination
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { currentDestination = destination },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                                contentDescription = destination.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = destination.title,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color(0xFF0A0F1D),
                                            selectedTextColor = CyanPrimary,
                                            indicatorColor = CyanPrimary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag("nav_item_${destination.name.lowercase()}")
                                    )
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        if (selectedProjectId != null) {
                            ProjectDetailScreen(
                                projectId = selectedProjectId!!,
                                onBack = { selectedProjectId = null }
                            )
                        } else {
                            AnimatedContent(
                                targetState = currentDestination,
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut()
                                },
                                label = "ScreenTransition"
                            ) { dest ->
                                when (dest) {
                                    AppDestination.HOME -> HomeScreen(
                                        isWideScreen = false,
                                        onNavigateToProject = { id -> selectedProjectId = id },
                                        onNavigateToProjectsList = { currentDestination = AppDestination.PROJECTS },
                                        onNavigateToExperience = { currentDestination = AppDestination.EXPERIENCE },
                                        onNavigateToContact = { currentDestination = AppDestination.CONTACT },
                                        onOpenAnimationSettings = { showAnimationSettings = true }
                                    )
                                    AppDestination.PROJECTS -> ProjectsScreen(
                                        isWideScreen = false,
                                        onNavigateToProject = { id -> selectedProjectId = id }
                                    )
                                    AppDestination.EXPERIENCE -> ExperienceScreen()
                                    AppDestination.SKILLS -> SkillsCertificationsScreen()
                                    AppDestination.CONTACT -> ContactScreen(isWideScreen = false)
                                }
                            }
                        }
                    }
                }
            }

            // Background Animation Customizer Modal Bottom Sheet
            if (showAnimationSettings) {
                AnimationSettingsBottomSheet(
                    settings = backgroundSettings,
                    onSettingsChanged = { backgroundSettings = it },
                    onDismissRequest = { showAnimationSettings = false }
                )
            }
        }
    }
}
