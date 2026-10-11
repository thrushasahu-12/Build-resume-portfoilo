package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PortfolioRepository
import com.example.ui.components.CodeSnippetViewer
import com.example.ui.components.ProjectArchitectureDiagram
import com.example.ui.components.ProjectSimulatorSection
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent

enum class ProjectDetailTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    ARCHITECTURE("Pipeline Flow", Icons.Default.AccountTree),
    BUSINESS_CONTEXT("Business Context", Icons.Default.BusinessCenter),
    TRADEOFFS("Tech Decisions", Icons.Default.Handyman),
    SIMULATION("Live Simulator", Icons.Default.Science),
    LESSONS("Lessons Learned", Icons.Default.Lightbulb)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onBack != null) {
        BackHandler { onBack() }
    }

    val context = LocalContext.current
    val project = remember(projectId) {
        PortfolioRepository.getProjectById(projectId) ?: PortfolioRepository.projects.first()
    }

    var selectedTab by remember { mutableStateOf(ProjectDetailTab.ARCHITECTURE) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "${project.organization} • ${project.period}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanPrimary
                        )
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = CyanPrimary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Project Architecture Deep Dive: ${project.title}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out this data architecture case study by Thrusha Sahu: ${project.title} (${project.organization})\n\nBusiness Problem: ${project.businessProblem}\nImpact: ${project.businessImpact}\nTech: ${project.techStack.joinToString(", ")}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Project"))
                        },
                        modifier = Modifier.testTag("share_project_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Enhanced Project Header Banner
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
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

                            Surface(
                                color = EmeraldAccent.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = project.category.displayName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EmeraldAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = project.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Key Impact Metrics Strip
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            project.keyMetrics.forEach { metric ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp)),
                                    color = Color(0xFF0F172A)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(metric.label, fontSize = 9.5.sp, color = Color(0xFF94A3B8))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = metric.value,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldAccent
                                        )
                                        Text(
                                            text = metric.delta,
                                            fontSize = 9.5.sp,
                                            color = CyanPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Explanation Tabs
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    contentColor = CyanPrimary,
                    edgePadding = 0.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                ) {
                    ProjectDetailTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            text = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            selectedContentColor = CyanPrimary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Tab Content
            item {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "TabContentTransition"
                ) { currentTab ->
                    when (currentTab) {
                        ProjectDetailTab.ARCHITECTURE -> {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                ProjectArchitectureDiagram(stages = project.architectureStages)

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "HIGH-LEVEL ARCHITECTURE SUMMARY",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = CyanPrimary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = project.technicalArchitectureSummary,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }

                        ProjectDetailTab.BUSINESS_CONTEXT -> {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "THE BUSINESS PROBLEM & BOTTLENECK",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFEF4444)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = project.businessProblem,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.dp, EmeraldAccent.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldAccent, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "MEASURABLE BUSINESS IMPACT & ROI",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldAccent
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = project.businessImpact,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }

                        ProjectDetailTab.TRADEOFFS -> {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                if (project.architecturalTradeoffs.isNotEmpty()) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(1.dp, CyanPrimary.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "ENGINEERING TRADEOFFS & DECISION LOGIC",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = CyanPrimary
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            project.architecturalTradeoffs.forEach { (decision, rationale) ->
                                                Text(
                                                    text = "⚖️ $decision",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = EmeraldAccent
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = rationale,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    lineHeight = 18.sp
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                            }
                                        }
                                    }
                                }

                                if (project.challengesSolved.isNotEmpty()) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "EDGE CASES & DATA CHALLENGES RESOLVED",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldAccent
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            project.challengesSolved.forEach { challenge ->
                                                Row(
                                                    modifier = Modifier.padding(vertical = 3.dp),
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    Text("• ", color = EmeraldAccent, fontWeight = FontWeight.Bold)
                                                    Text(
                                                        text = challenge,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        lineHeight = 18.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        ProjectDetailTab.SIMULATION -> {
                            ProjectSimulatorSection(project = project)
                        }

                        ProjectDetailTab.LESSONS -> {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, EmeraldAccent.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "ENTERPRISE LESSONS LEARNED & PRODUCTION REFLECTIONS",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldAccent
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    project.lessonsLearned.forEach { lesson ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = CyanPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = lesson,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                lineHeight = 19.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Code Snippets Section (if present)
            if (project.codeSnippets.isNotEmpty()) {
                item {
                    Column {
                        Text(
                            text = "ANNOTATED SQL & ALGORITHMIC ARTIFACTS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CyanPrimary,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        project.codeSnippets.forEach { snippet ->
                            CodeSnippetViewer(
                                title = snippet.title,
                                language = snippet.language,
                                code = snippet.code,
                                explanation = snippet.explanation
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            // Tech Stack Chips
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "TECHNOLOGIES & PLATFORMS UTILIZED",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            project.techStack.forEach { tech ->
                                Surface(
                                    color = Color(0xFF0F172A),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
                                ) {
                                    Text(
                                        text = tech,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFE2E8F0)
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
