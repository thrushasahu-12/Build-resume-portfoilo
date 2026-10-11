package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldAccent
import kotlin.math.sin

@Composable
fun DataAnalystHeroIllustration(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "IllustrationPulse")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF131D36),
                        Color(0xFF0A0F1D)
                    )
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(CyanPrimary.copy(alpha = 0.6f), EmeraldAccent.copy(alpha = 0.5f))
                ),
                RoundedCornerShape(18.dp)
            )
    ) {
        // Background Technical Canvas: Holographic Graph, Grid Lines & Traveling Packets
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Technical coordinate grid lines
            val gridStep = 32f
            var x = 0f
            while (x < w) {
                drawLine(
                    color = Color(0xFF1E293B).copy(alpha = 0.45f),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 0.8f
                )
                x += gridStep
            }

            var y = 0f
            while (y < h) {
                drawLine(
                    color = Color(0xFF1E293B).copy(alpha = 0.45f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 0.8f
                )
                y += gridStep
            }

            // Dual holographic telemetry wave curves
            val wavePath1 = Path()
            val wavePath2 = Path()

            val baseY = h * 0.72f
            for (step in 0..w.toInt() step 6) {
                val stepF = step.toFloat()
                val waveY1 = baseY - 24f - (sin((stepF * 0.015f) + (pulseProgress * 6.28f)) * 26f) - (stepF * 0.05f)
                val waveY2 = baseY - 8f - (sin((stepF * 0.022f) - (pulseProgress * 6.28f)) * 18f)

                if (step == 0) {
                    wavePath1.moveTo(stepF, waveY1)
                    wavePath2.moveTo(stepF, waveY2)
                } else {
                    wavePath1.lineTo(stepF, waveY1)
                    wavePath2.lineTo(stepF, waveY2)
                }
            }

            drawPath(
                path = wavePath1,
                color = Color(0xFF00D2FF).copy(alpha = 0.55f),
                style = Stroke(width = 2.4f)
            )

            drawPath(
                path = wavePath2,
                color = Color(0xFF10B981).copy(alpha = 0.45f),
                style = Stroke(width = 2.0f)
            )

            // Dynamic Bar Chart Silhouettes in background
            val barCount = 10
            val barWidth = 14f
            val startBarX = w * 0.48f
            for (i in 0 until barCount) {
                val bx = startBarX + (i * 24f)
                val bh = 30f + (i * 8f) + (sin(i + pulseProgress * 6.28f) * 12f)
                if (bx + barWidth < w - 16f) {
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF00D2FF).copy(alpha = 0.6f), Color(0xFF0284C7).copy(alpha = 0.2f))
                        ),
                        topLeft = Offset(bx, baseY - bh),
                        size = Size(barWidth, bh),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
            }

            // Moving glowing data particle on telemetry curve
            val packetX = w * pulseProgress
            val packetY = baseY - 24f - (sin((packetX * 0.015f) + (pulseProgress * 6.28f)) * 26f) - (packetX * 0.05f)
            drawCircle(
                color = Color(0xFF00D2FF).copy(alpha = 0.4f),
                radius = 12f,
                center = Offset(packetX, packetY)
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(packetX, packetY)
            )
        }

        // Foreground UI Elements: Workstation Monitors & Metric Badges
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Holographic Terminal (SQL Query & Pipeline Status)
            Surface(
                modifier = Modifier
                    .width(185.dp)
                    .fillMaxHeight(),
                color = Color(0xFF0B132B).copy(alpha = 0.92f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        }
                        Surface(
                            color = EmeraldAccent.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "LIVE ETL",
                                color = EmeraldAccent,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "-- Snowflake Analytics Warehouse\nSELECT department,\n       COUNT(*) as tickets,\n       ROUND(AVG(sla_hrs), 1)\nFROM kpi_fact_metrics\nGROUP BY 1 HAVING COUNT(*) > 10\nORDER BY 2 DESC;",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = Color(0xFF38BDF8),
                        lineHeight = 12.5.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Rows: 250k/day", color = Color(0xFF94A3B8), fontSize = 9.sp)
                        Text("Latency: 0.42s", color = EmeraldAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Right Hero Status Hub (Data Analyst Identity & Live KPI Rings)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                // Top Badge: Data Analyst Workspace
                Surface(
                    color = CyanPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DATA ANALYST WORKSPACE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyanPrimary,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                // Middle Floating Analytics Pill
                Surface(
                    color = Color(0xFF1E293B).copy(alpha = 0.88f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldAccent.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(EmeraldAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = EmeraldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "50% Turnaround Boost",
                                color = EmeraldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Automated Python & Power BI",
                                color = Color(0xFF94A3B8),
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                // Bottom Floating Analytics Pill
                Surface(
                    color = Color(0xFF1E293B).copy(alpha = 0.88f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(CyanPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dataset,
                                contentDescription = null,
                                tint = CyanPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "30+ Executive BI Models",
                                color = CyanPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ServiceNow • Magnit Global",
                                color = Color(0xFF94A3B8),
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
