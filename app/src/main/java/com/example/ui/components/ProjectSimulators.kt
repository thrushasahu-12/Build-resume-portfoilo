package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Project
import com.example.model.SimulationType
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun ProjectSimulatorSection(
    project: Project,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(1.dp, EmeraldAccent.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(EmeraldAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Interactive Simulator",
                    tint = EmeraldAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Live Interactive Simulation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Test variables & observe real-time algorithmic output",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (project.simulationType) {
            SimulationType.BEFORE_AFTER_EFFICIENCY -> BeforeAfterEfficiencySimulator()
            SimulationType.SQL_DIAGNOSTIC_QUERY -> SqlDiagnosticQuerySimulator()
            SimulationType.AB_TEST_SIGNIFICANCE -> AbTestingSignificanceSimulator()
            SimulationType.STAR_SCHEMA_WAREHOUSE -> StarSchemaWarehouseSimulator()
            SimulationType.KPI_WHAT_IF -> KpiWhatIfSimulator()
        }
    }
}

// 1. ServiceNow Automation Before vs After Simulator
@Composable
fun BeforeAfterEfficiencySimulator() {
    var automationDegree by remember { mutableFloatStateOf(0.5f) }
    var selectedDept by remember { mutableStateOf("Content Ops") }

    val baseHours = 40f
    val currentHours = (baseHours * (1f - (automationDegree * 0.5f))).roundToInt()
    val accuracy = (78f + (automationDegree * 21.2f)).coerceAtMost(99.2f)
    val monthlySavings = (automationDegree * 2400f).roundToInt()

    Column {
        Text(
            text = "AUTOMATION ADOPTION LEVEL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = CyanPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Slider(
            value = automationDegree,
            onValueChange = { automationDegree = it },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = EmeraldAccent,
                activeTrackColor = EmeraldAccent
            ),
            modifier = Modifier.testTag("automation_slider")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Manual Spreadsheets (0%)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Full Python/BI Pipeline (100%)",
                style = MaterialTheme.typography.labelSmall,
                color = EmeraldAccent,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Department filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Content Ops", "Global Sales", "Tier 2 Support").forEach { dept ->
                val isSelected = selectedDept == dept
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDept = dept },
                    label = { Text(dept, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = CyanPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Metrics Comparison Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Reporting Time", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "$currentHours hrs/mo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (automationDegree > 0.5f) EmeraldAccent else Color(0xFFF59E0B)
                    )
                    Text(
                        text = if (automationDegree > 0.5f) "-50% Turnaround" else "Baseline",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Data Accuracy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = String.format("%.1f%%", accuracy),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldAccent
                    )
                    Text(
                        text = "+30% Quality Audit",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Monthly ROI", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "+$$monthlySavings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CyanPrimary
                    )
                    Text(
                        text = "Engineering time saved",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// 2. Customer Support Root-Cause SQL Simulator
@Composable
fun SqlDiagnosticQuerySimulator() {
    var isExecuting by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All Tiers") }

    val categories = listOf(
        Pair("API Timeout & Latency", 0.38f),
        Pair("Token Refresh Desync", 0.22f),
        Pair("Missing Role Permission", 0.18f),
        Pair("Legacy UI Rendering Bug", 0.12f),
        Pair("Network / ISP Gateway", 0.10f)
    )

    Column {
        Text(
            text = "DIAGNOSTIC SQL EXECUTION PLAN",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = CyanPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            color = Color(0xFF0F172A)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "EXPLAIN ANALYZE\nSELECT root_cause, COUNT(*) as volume, \nROUND(AVG(resolution_hrs), 1) as avg_hrs\nFROM incident_escalations\nGROUP BY 1 ORDER BY volume DESC;",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp,
                    color = Color(0xFF38BDF8)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pareto Root Cause Clusters:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = { isExecuting = !isExecuting },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF0A0F1D)),
                modifier = Modifier.testTag("run_query_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Re-Run Query (2.4s)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Animated Pareto Distribution Bars
        categories.forEach { (name, pct) ->
            val animatedWidth by animateFloatAsState(
                targetValue = if (isExecuting) pct * 0.9f else pct,
                animationSpec = tween(600),
                label = "bar_$name"
            )

            Column(modifier = Modifier.padding(vertical = 3.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "${(pct * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (pct >= 0.22f) Color(0xFFEF4444) else EmeraldAccent
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { animatedWidth },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (pct >= 0.22f) Color(0xFFEF4444) else CyanPrimary,
                    trackColor = Color(0xFF1E293B)
                )
            }
        }
    }
}

// 3. A/B Testing Significance Simulator
@Composable
fun AbTestingSignificanceSimulator() {
    var sampleSize by remember { mutableFloatStateOf(48500f) }

    val baselineConv = 0.084f // 8.4%
    val challengerConv = 0.096f // 9.6%
    val relativeLift = ((challengerConv - baselineConv) / baselineConv) * 100f
    val pValue = (0.045f * (20000f / sampleSize)).coerceIn(0.002f, 0.08f)
    val isSignificant = pValue < 0.05f

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SAMPLE SIZE (N USERS)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CyanPrimary
            )
            Text(
                text = "${(sampleSize / 1000).toInt()},000 Users",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldAccent
            )
        }

        Slider(
            value = sampleSize,
            onValueChange = { sampleSize = it },
            valueRange = 10000f..100000f,
            colors = SliderDefaults.colors(
                thumbColor = CyanPrimary,
                activeTrackColor = CyanPrimary
            ),
            modifier = Modifier.testTag("ab_test_slider")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Animated Bell Curve Comparison Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0F172A))
        ) {
            val w = size.width
            val h = size.height

            // Control distribution (Center around 0.38w)
            val pathA = Path()
            val meanA = w * 0.38f
            val stdA = 48f

            for (x in 0..w.toInt() step 4) {
                val fx = (x - meanA) / stdA
                val y = h - (exp(-0.5f * fx.pow(2)) * (h * 0.72f))
                if (x == 0) pathA.moveTo(x.toFloat(), y) else pathA.lineTo(x.toFloat(), y)
            }

            drawPath(pathA, color = Color(0xFF64748B), style = Stroke(width = 2.5f))

            // Challenger distribution (Center around 0.58w)
            val pathB = Path()
            val meanB = w * 0.58f
            val stdB = 44f

            for (x in 0..w.toInt() step 4) {
                val fx = (x - meanB) / stdB
                val y = h - (exp(-0.5f * fx.pow(2)) * (h * 0.82f))
                if (x == 0) pathB.moveTo(x.toFloat(), y) else pathB.lineTo(x.toFloat(), y)
            }

            drawPath(pathB, color = EmeraldAccent, style = Stroke(width = 3.0f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = if (isSignificant) EmeraldAccent.copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isSignificant) "✓ STATISTICALLY SIGNIFICANT" else "⚠ INSUFFICIENT POWER",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isSignificant) EmeraldAccent else Color(0xFFEF4444)
                    )
                }
            }

            Text(
                text = "p-value: ${String.format("%.3f", pValue)} (Lift: +${String.format("%.1f", relativeLift)}%)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// 4. Cloud Warehouse Star Schema Simulator
@Composable
fun StarSchemaWarehouseSimulator() {
    var selectedTable by remember { mutableStateOf("FACT_INCIDENT") }

    val tableColumns = mapOf(
        "FACT_INCIDENT" to listOf("incident_id (PK)", "date_key (FK)", "dept_key (FK)", "agent_key (FK)", "resolution_time_hrs", "sla_status"),
        "DIM_DATE" to listOf("date_key (PK)", "full_date", "day_of_week", "fiscal_quarter", "is_holiday"),
        "DIM_DEPARTMENT" to listOf("dept_key (PK)", "department_name", "cost_center", "vp_lead"),
        "DIM_AGENT" to listOf("agent_key (PK)", "agent_name", "tier_level", "region_hub")
    )

    Column {
        Text(
            text = "DIMENSIONAL STAR SCHEMA (SNOWFLAKE)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = CyanPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("FACT_INCIDENT", "DIM_DATE", "DIM_DEPARTMENT", "DIM_AGENT").forEach { table ->
                val isSelected = selectedTable == table
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedTable = table }
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) CyanPrimary else Color(0xFF334155),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    color = if (isSelected) CyanPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = table.replace("DIM_", "D_").replace("FACT_", "F_"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) CyanPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selected table schema view
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp)),
            color = Color(0xFF0F172A)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Table: $selectedTable",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        fontSize = 13.sp
                    )
                    Text(
                        text = if (selectedTable.startsWith("FACT")) "Fact (1.2M rows)" else "Dimension (Type 1 SCD)",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                tableColumns[selectedTable]?.forEach { col ->
                    Text(
                        text = "• $col",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.5.sp,
                        color = if (col.contains("PK")) EmeraldAccent else if (col.contains("FK")) CyanPrimary else Color(0xFFCBD5E1)
                    )
                }
            }
        }
    }
}

// 5. Global Sales KPI What-If Simulator
@Composable
fun KpiWhatIfSimulator() {
    var discountMargin by remember { mutableFloatStateOf(5f) }
    var demandGrowth by remember { mutableFloatStateOf(10f) }

    val baseRevenue = 2.4f // in millions
    val projectedRevenue = baseRevenue * (1f + demandGrowth / 100f)
    val projectedMargin = (32f - discountMargin * 0.8f).coerceAtLeast(10f)
    val netProfit = (projectedRevenue * (projectedMargin / 100f))

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Discount Promotion (-%):", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            Text("${discountMargin.toInt()}%", fontWeight = FontWeight.Bold, color = Color(0xFFEF4444), fontSize = 12.sp)
        }
        Slider(
            value = discountMargin,
            onValueChange = { discountMargin = it },
            valueRange = 0f..25f,
            colors = SliderDefaults.colors(thumbColor = Color(0xFFEF4444), activeTrackColor = Color(0xFFEF4444))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Demand Uplift (+%):", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            Text("+${demandGrowth.toInt()}%", fontWeight = FontWeight.Bold, color = EmeraldAccent, fontSize = 12.sp)
        }
        Slider(
            value = demandGrowth,
            onValueChange = { demandGrowth = it },
            valueRange = 0f..40f,
            colors = SliderDefaults.colors(thumbColor = EmeraldAccent, activeTrackColor = EmeraldAccent)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Proj. Revenue", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$${String.format("%.2f", projectedRevenue)}M", fontWeight = FontWeight.Bold, color = CyanPrimary)
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Net Margin", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${String.format("%.1f", projectedMargin)}%", fontWeight = FontWeight.Bold, color = EmeraldAccent)
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Est. Net Profit", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$${String.format("%.2f", netProfit)}M", fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                }
            }
        }
    }
}
