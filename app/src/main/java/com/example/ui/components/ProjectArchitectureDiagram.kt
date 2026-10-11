package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArchitectureStage
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProjectArchitectureDiagram(
    stages: List<ArchitectureStage>,
    modifier: Modifier = Modifier
) {
    if (stages.isEmpty()) return

    var selectedStageIndex by remember { mutableIntStateOf(0) }
    var isSimulatingPipeline by remember { mutableStateOf(false) }
    var simulationActiveStage by remember { mutableIntStateOf(-1) }
    var simulationProgress by remember { mutableFloatStateOf(0f) }
    var simulationLogs by remember { mutableStateOf(listOf<String>()) }

    val coroutineScope = rememberCoroutineScope()

    // Infinite transition for continuous glowing pipeline data packet
    val infiniteTransition = rememberInfiniteTransition(label = "PipelineFlow")
    val flowProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PacketFlow"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountTree,
                        contentDescription = "Architecture Pipeline",
                        tint = CyanPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Interactive Pipeline Architecture",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tap any node to inspect data contracts & logic",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Live Simulation Trigger Button
            FilledTonalButton(
                onClick = {
                    if (!isSimulatingPipeline) {
                        isSimulatingPipeline = true
                        simulationLogs = listOf("⚡ Initializing data ingestion pipeline...")
                        coroutineScope.launch {
                            for (i in stages.indices) {
                                simulationActiveStage = i
                                simulationProgress = (i + 1).toFloat() / stages.size
                                when (i) {
                                    0 -> {
                                        simulationLogs = simulationLogs + "📥 Stage 1: Ingesting batch telemetry (248,310 records)..."
                                    }
                                    1 -> {
                                        simulationLogs = simulationLogs + "⚙️ Stage 2: Pandas transformations & IQR outlier filter passed (0.34s)..."
                                    }
                                    2 -> {
                                        simulationLogs = simulationLogs + "📐 Stage 3: Star-schema joins & DAX time-intelligence computed..."
                                    }
                                    3 -> {
                                        simulationLogs = simulationLogs + "📊 Stage 4: Tabular cache refreshed. Dashboards live & verified!"
                                    }
                                }
                                delay(900)
                            }
                            delay(600)
                            simulationActiveStage = -1
                            isSimulatingPipeline = false
                        }
                    }
                },
                enabled = !isSimulatingPipeline,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isSimulatingPipeline) EmeraldAccent.copy(alpha = 0.3f) else CyanPrimary.copy(alpha = 0.15f),
                    contentColor = if (isSimulatingPipeline) EmeraldAccent else CyanPrimary
                ),
                modifier = Modifier.testTag("simulate_pipeline_button")
            ) {
                Icon(
                    imageVector = if (isSimulatingPipeline) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = "Run Pipeline",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSimulatingPipeline) "Streaming..." else "Run Sim",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Animated Pipeline Stage Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            // Connector line with animated glowing packet
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                val startX = 28f
                val endX = size.width - 28f
                val centerY = 21f

                // Base connector line
                drawLine(
                    color = Color(0xFF334155),
                    start = Offset(startX, centerY),
                    end = Offset(endX, centerY),
                    strokeWidth = 3f
                )

                // Animated moving packet pulse
                val packetX = startX + (endX - startX) * flowProgress
                drawCircle(
                    color = Color(0xFF00D2FF).copy(alpha = 0.45f),
                    radius = 9f,
                    center = Offset(packetX, centerY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.5f,
                    center = Offset(packetX, centerY)
                )
            }

            // Pipeline Stage Nodes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                stages.forEachIndexed { index, stage ->
                    val isSelected = selectedStageIndex == index
                    val isSimulationActive = simulationActiveStage == index

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedStageIndex = index }
                            .testTag("pipeline_stage_$index")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSimulationActive -> EmeraldAccent
                                        isSelected -> CyanPrimary
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                )
                                .border(
                                    width = if (isSelected || isSimulationActive) 2.5.dp else 1.5.dp,
                                    color = when {
                                        isSimulationActive -> EmeraldAccent
                                        isSelected -> CyanPrimary
                                        else -> MaterialTheme.colorScheme.outline
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${stage.stepNumber}",
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected || isSimulationActive) Color(0xFF0A0F1D) else MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Stage ${stage.stepNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) CyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Live Simulation Status Progress Bar
        AnimatedVisibility(
            visible = isSimulatingPipeline,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(modifier = Modifier.padding(top = 14.dp)) {
                LinearProgressIndicator(
                    progress = { simulationProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = EmeraldAccent,
                    trackColor = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                simulationLogs.takeLast(2).forEach { log ->
                    Text(
                        text = log,
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldAccent,
                        fontSize = 11.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Stage Technical Detail Card
        val currentStage = stages.getOrElse(selectedStageIndex) { stages.first() }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, CyanPrimary.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentStage.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = currentStage.shortDescription,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = CyanPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = currentStage.tool,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CyanPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "TECHNICAL SPECIFICATION",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldAccent,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = currentStage.deepTechnicalDetails,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                if (currentStage.inputOutputSummary.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF0F172A),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⚡ ${currentStage.inputOutputSummary}",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stage Performance Telemetry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(6.dp)) {
                            Text("Latency", style = MaterialTheme.typography.labelSmall, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(currentStage.latencyMs, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = CyanPrimary)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(6.dp)) {
                            Text("Throughput", style = MaterialTheme.typography.labelSmall, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(currentStage.throughput, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = EmeraldAccent)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1.4f),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(6.dp)) {
                            Text("Fault Tolerance", style = MaterialTheme.typography.labelSmall, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(currentStage.faultToleranceStrategy, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                        }
                    }
                }

                // Code snippet if available
                if (currentStage.codeOrQuerySnippet.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    CodeSnippetViewer(
                        title = "Stage ${currentStage.stepNumber} Implementation",
                        language = currentStage.snippetLanguage,
                        code = currentStage.codeOrQuerySnippet
                    )
                }
            }
        }
    }
}
