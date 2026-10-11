package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.model.Project
import com.example.ui.components.DataAnalystHeroIllustration
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent

@Composable
fun HomeScreen(
    isWideScreen: Boolean,
    onNavigateToProject: (String) -> Unit,
    onNavigateToProjectsList: () -> Unit,
    onNavigateToExperience: () -> Unit,
    onNavigateToContact: () -> Unit,
    onOpenAnimationSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = PortfolioRepository.candidateProfile
    val stats = PortfolioRepository.coreStats
    val featuredProject = PortfolioRepository.projects.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = if (isWideScreen) 24.dp else 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (isWideScreen) {
            // Tablet / Desktop Dual-Column Panoramic Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Left Column: Profile Card + Data Analyst Illustration
                    Column(
                        modifier = Modifier.weight(1.1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HeroProfileCard(
                            profile = profile,
                            onOpenAnimationSettings = onOpenAnimationSettings,
                            onNavigateToProjectsList = onNavigateToProjectsList,
                            onNavigateToContact = onNavigateToContact
                        )

                        // Data Analyst Visual Illustration Banner
                        DataAnalystHeroIllustration()
                    }

                    // Right Column: Executive Impact Metrics + Featured Project Deep Dive
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ImpactMetricsSection(stats = stats)

                        FeaturedProjectCard(
                            project = featuredProject,
                            onInspectClick = { onNavigateToProject(featuredProject.id) },
                            onViewAllClick = onNavigateToProjectsList
                        )
                    }
                }
            }
        } else {
            // Handheld Mobile Stacked Flow
            item {
                HeroProfileCard(
                    profile = profile,
                    onOpenAnimationSettings = onOpenAnimationSettings,
                    onNavigateToProjectsList = onNavigateToProjectsList,
                    onNavigateToContact = onNavigateToContact
                )
            }

            // Data Analyst Hero Image / Illustration
            item {
                DataAnalystHeroIllustration()
            }

            // Key Performance Stats Grid
            item {
                ImpactMetricsSection(stats = stats)
            }

            // Featured Project Spotlight
            item {
                FeaturedProjectCard(
                    project = featuredProject,
                    onInspectClick = { onNavigateToProject(featuredProject.id) },
                    onViewAllClick = onNavigateToProjectsList
                )
            }
        }

        // Professional Tenure Teaser
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .clickable { onNavigateToExperience() }
                    .testTag("experience_teaser_card"),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF8B5CF6).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Work, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ServiceNow Tenure & Career Timeline",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Data Analyst • Magnit Global (July 2022 - October 2025)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun HeroProfileCard(
    profile: Map<String, String>,
    onOpenAnimationSettings: () -> Unit,
    onNavigateToProjectsList: () -> Unit,
    onNavigateToContact: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(CyanPrimary.copy(alpha = 0.6f), EmeraldAccent.copy(alpha = 0.4f))),
                RoundedCornerShape(20.dp)
            ),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Status and Visualizer Trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = EmeraldAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldAccent.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EmeraldAccent)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OPEN TO OPPORTUNITIES",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldAccent,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                FilledTonalButton(
                    onClick = onOpenAnimationSettings,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = CyanPrimary.copy(alpha = 0.15f),
                        contentColor = CyanPrimary
                    ),
                    modifier = Modifier.testTag("bg_animation_pill")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Tuning",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Canvas FX", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = profile["name"] ?: "Thrusha Sahu",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = (-0.5).sp
            )

            Text(
                text = profile["title"] ?: "Data Analyst & BI Specialist",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = CyanPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = profile["summary"] ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateToProjectsList,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("explore_projects_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanPrimary,
                        contentColor = Color(0xFF0A0F1D)
                    )
                ) {
                    Icon(Icons.Default.AccountTree, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Projects", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onNavigateToContact,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("contact_hire_btn"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldAccent)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = EmeraldAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Contact", color = EmeraldAccent, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ImpactMetricsSection(stats: List<com.example.model.ProjectMetric>) {
    Column {
        Text(
            text = "EXECUTIVE IMPACT METRICS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = CyanPrimary,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricStatCard(
                    metric = stats[0],
                    accentColor = CyanPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    metric = stats[1],
                    accentColor = EmeraldAccent,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricStatCard(
                    metric = stats[2],
                    accentColor = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    metric = stats[3],
                    accentColor = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FeaturedProjectCard(
    project: Project,
    onInspectClick: () -> Unit,
    onViewAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FEATURED ARCHITECTURE EXPLANATION",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = EmeraldAccent,
                letterSpacing = 1.sp
            )

            TextButton(
                onClick = onViewAllClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("View All (5)", color = CyanPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .clickable { onInspectClick() }
                .testTag("featured_project_card"),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = CyanPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = project.organization,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CyanPrimary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Animation, contentDescription = null, tint = EmeraldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Interactive Breakdown", color = EmeraldAccent, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = project.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    project.techStack.take(4).forEach { tech ->
                        Surface(
                            color = Color(0xFF0F172A),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
                        ) {
                            Text(
                                text = tech,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 11.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Inspect architecture pipeline & simulator",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun MetricStatCard(
    metric: com.example.model.ProjectMetric,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = metric.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = metric.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = metric.delta,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = metric.subtext,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.5.sp,
                lineHeight = 14.sp
            )
        }
    }
}
