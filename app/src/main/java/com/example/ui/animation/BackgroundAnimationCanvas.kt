package com.example.ui.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.model.BackgroundAnimationMode
import com.example.model.BackgroundSettings
import kotlin.math.*
import kotlin.random.Random

private class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float,
    val baseAlpha: Float,
    val color: Color
)

private class DataPacket(
    var sourceIdx: Int,
    var targetIdx: Int,
    var progress: Float,
    val speed: Float,
    val color: Color
)

@Composable
fun BackgroundAnimationCanvas(
    settings: BackgroundSettings,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    // Infinite transition drives continuous time ticks
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundTransition")
    val timeProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "TimeProgress"
    )

    // Touch interaction coordinates
    var touchPos by remember { mutableStateOf<Offset?>(null) }
    var touchRippleRadius by remember { mutableFloatStateOf(0f) }

    // Particles for Constellation mode
    val particles = remember {
        val rand = Random(42)
        List(38) {
            val isEmerald = rand.nextFloat() > 0.65f
            Particle(
                x = rand.nextFloat() * 1000f,
                y = rand.nextFloat() * 2000f,
                vx = (rand.nextFloat() - 0.5f) * 0.9f,
                vy = (rand.nextFloat() - 0.5f) * 0.9f,
                radius = 2.0f + rand.nextFloat() * 2.8f,
                baseAlpha = 0.35f + rand.nextFloat() * 0.45f,
                color = if (isEmerald) Color(0xFF10B981) else Color(0xFF00D2FF)
            )
        }
    }

    // Data packets that travel between connected nodes
    val dataPackets = remember {
        val rand = Random(101)
        List(8) {
            DataPacket(
                sourceIdx = rand.nextInt(particles.size),
                targetIdx = rand.nextInt(particles.size),
                progress = rand.nextFloat(),
                speed = 0.004f + rand.nextFloat() * 0.007f,
                color = if (rand.nextBoolean()) Color(0xFF38BDF8) else Color(0xFF34D399)
            )
        }
    }

    // Interactive pointer handling
    val pointerModifier = if (settings.touchInteractive) {
        modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        touchPos = offset
                        touchRippleRadius = 10f
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> touchPos = offset },
                    onDragEnd = { touchPos = null },
                    onDragCancel = { touchPos = null },
                    onDrag = { change, _ ->
                        touchPos = change.position
                    }
                )
            }
    } else {
        modifier
    }

    Canvas(modifier = pointerModifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Base background fill tint
        val bgGradient = if (isDarkTheme) {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0A0F1D),
                    Color(0xFF0D1527),
                    Color(0xFF080D1A)
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF8FAFC),
                    Color(0xFFF1F5F9),
                    Color(0xFFE2E8F0)
                )
            )
        }
        drawRect(brush = bgGradient)

        val speed = settings.speedMultiplier
        val glow = settings.glowIntensity

        when (settings.mode) {
            BackgroundAnimationMode.CONSTELLATION -> {
                drawConstellationMode(
                    particles = particles,
                    dataPackets = dataPackets,
                    time = timeProgress * speed,
                    touchPos = touchPos,
                    glowIntensity = glow,
                    isDark = isDarkTheme,
                    width = width,
                    height = height
                )
            }
            BackgroundAnimationMode.WAVE_FIELD -> {
                drawWaveFieldMode(
                    time = timeProgress * speed,
                    touchPos = touchPos,
                    glowIntensity = glow,
                    isDark = isDarkTheme,
                    width = width,
                    height = height
                )
            }
            BackgroundAnimationMode.MATRIX_GRID -> {
                drawMatrixGridMode(
                    time = timeProgress * speed,
                    touchPos = touchPos,
                    glowIntensity = glow,
                    isDark = isDarkTheme,
                    width = width,
                    height = height
                )
            }
        }

        // Touch ripple highlight
        touchPos?.let { pos ->
            val rippleColor = if (isDarkTheme) Color(0xFF00D2FF).copy(alpha = 0.25f) else Color(0xFF0284C7).copy(alpha = 0.20f)
            drawCircle(
                color = rippleColor,
                radius = 65f * glow,
                center = pos
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.6f),
                radius = 4f,
                center = pos
            )
        }
    }
}

private fun DrawScope.drawConstellationMode(
    particles: List<Particle>,
    dataPackets: List<DataPacket>,
    time: Float,
    touchPos: Offset?,
    glowIntensity: Float,
    isDark: Boolean,
    width: Float,
    height: Float
) {
    val connectionDistanceThreshold = (width * 0.32f).coerceIn(120f, 260f)

    // Update particle positions
    for (p in particles) {
        p.x += p.vx * 1.5f
        p.y += p.vy * 1.5f

        // Repel or pull gently if near touchPos
        touchPos?.let { t ->
            val dx = p.x - t.x
            val dy = p.y - t.y
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < 220f && dist > 1f) {
                p.x += (dx / dist) * 1.8f
                p.y += (dy / dist) * 1.8f
            }
        }

        // Screen boundary bounce/wrap
        if (p.x < -20f) p.x = width + 20f
        if (p.x > width + 20f) p.x = -20f
        if (p.y < -20f) p.y = height + 20f
        if (p.y > height + 20f) p.y = -20f
    }

    // Draw connecting lines between close particles
    val lineBaseColor = if (isDark) Color(0xFF00D2FF) else Color(0xFF0284C7)
    for (i in particles.indices) {
        val p1 = particles[i]
        for (j in i + 1 until particles.size) {
            val p2 = particles[j]
            val dx = p1.x - p2.x
            val dy = p1.y - p2.y
            val dist = sqrt(dx * dx + dy * dy)

            if (dist < connectionDistanceThreshold) {
                val ratio = 1f - (dist / connectionDistanceThreshold)
                val alpha = (ratio * 0.28f * glowIntensity).coerceIn(0.02f, 0.45f)
                drawLine(
                    color = lineBaseColor.copy(alpha = alpha),
                    start = Offset(p1.x, p1.y),
                    end = Offset(p2.x, p2.y),
                    strokeWidth = 1.2f * ratio
                )
            }
        }
    }

    // Animate and draw data packets traveling along edges
    for (pkt in dataPackets) {
        pkt.progress += pkt.speed
        if (pkt.progress >= 1.0f) {
            pkt.progress = 0f
            pkt.sourceIdx = pkt.targetIdx
            pkt.targetIdx = (pkt.targetIdx + 3) % particles.size
        }

        val pStart = particles[pkt.sourceIdx]
        val pEnd = particles[pkt.targetIdx]
        val dx = pEnd.x - pStart.x
        val dy = pEnd.y - pStart.y
        val dist = sqrt(dx * dx + dy * dy)

        if (dist < connectionDistanceThreshold * 1.4f) {
            val curX = pStart.x + dx * pkt.progress
            val curY = pStart.y + dy * pkt.progress
            val pktAlpha = (0.75f * glowIntensity).coerceIn(0.2f, 0.95f)

            // Outer soft glow
            drawCircle(
                color = pkt.color.copy(alpha = pktAlpha * 0.4f),
                radius = 5.5f,
                center = Offset(curX, curY)
            )
            // Core bright dot
            drawCircle(
                color = if (isDark) Color.White.copy(alpha = pktAlpha) else pkt.color,
                radius = 2.4f,
                center = Offset(curX, curY)
            )
        }
    }

    // Draw particle nodes
    for (p in particles) {
        val pulse = sin(time * 0.05f + p.x * 0.01f) * 0.2f
        val nodeAlpha = ((p.baseAlpha + pulse) * glowIntensity).coerceIn(0.15f, 0.85f)

        // Outer halo
        drawCircle(
            color = p.color.copy(alpha = nodeAlpha * 0.35f),
            radius = p.radius * 2.2f,
            center = Offset(p.x, p.y)
        )
        // Core node
        drawCircle(
            color = p.color.copy(alpha = nodeAlpha),
            radius = p.radius,
            center = Offset(p.x, p.y)
        )
    }
}

private fun DrawScope.drawWaveFieldMode(
    time: Float,
    touchPos: Offset?,
    glowIntensity: Float,
    isDark: Boolean,
    width: Float,
    height: Float
) {
    val waveCount = 4
    val waveColors = if (isDark) {
        listOf(
            Color(0xFF00D2FF),
            Color(0xFF10B981),
            Color(0xFF8B5CF6),
            Color(0xFF38BDF8)
        )
    } else {
        listOf(
            Color(0xFF0284C7),
            Color(0xFF059669),
            Color(0xFF7C3AED),
            Color(0xFF0369A1)
        )
    }

    val baseY = height * 0.52f
    val stepX = 14f

    for (w in 0 until waveCount) {
        val waveColor = waveColors[w]
        val path = Path()
        val freq = 0.0035f * (w + 1)
        val speedFactor = 0.025f * (w + 1)
        val amplitude = 28f + (w * 18f)

        var isFirst = true
        var x = 0f
        while (x <= width + stepX) {
            val touchInfluence = touchPos?.let {
                val dx = x - it.x
                val dist = abs(dx)
                if (dist < 180f) sin((1f - dist / 180f) * Math.PI.toFloat()) * 30f else 0f
            } ?: 0f

            val y = baseY + sin(x * freq + time * speedFactor) * amplitude +
                    cos(x * freq * 0.5f - time * 0.01f) * (amplitude * 0.4f) + touchInfluence

            if (isFirst) {
                path.moveTo(x, y)
                isFirst = false
            } else {
                path.lineTo(x, y)
            }
            x += stepX
        }

        drawPath(
            path = path,
            color = waveColor.copy(alpha = 0.30f * glowIntensity),
            style = Stroke(width = 2.4f)
        )
    }

    // Telemetry vertical pulse bars
    val barCount = 14
    val barSpacing = width / (barCount + 1)
    for (b in 1..barCount) {
        val bx = b * barSpacing
        val bHeight = abs(sin(bx * 0.02f + time * 0.04f)) * 70f * glowIntensity
        val top = baseY - bHeight * 0.5f
        val bottom = baseY + bHeight * 0.5f

        val barColor = if (b % 2 == 0) Color(0xFF00D2FF) else Color(0xFF10B981)
        drawLine(
            color = barColor.copy(alpha = 0.22f * glowIntensity),
            start = Offset(bx, top),
            end = Offset(bx, bottom),
            strokeWidth = 2.5f
        )
    }
}

private fun DrawScope.drawMatrixGridMode(
    time: Float,
    touchPos: Offset?,
    glowIntensity: Float,
    isDark: Boolean,
    width: Float,
    height: Float
) {
    val gridSize = 48f
    val lineColor = if (isDark) Color(0xFF1E293B).copy(alpha = 0.45f) else Color(0xFFCBD5E1).copy(alpha = 0.45f)

    // Vertical grid lines
    var x = 0f
    while (x <= width) {
        drawLine(
            color = lineColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1f
        )
        x += gridSize
    }

    // Horizontal grid lines
    var y = 0f
    while (y <= height) {
        drawLine(
            color = lineColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1f
        )
        y += gridSize
    }

    // Pulsing data intersections
    val activeSpots = 16
    for (i in 0 until activeSpots) {
        val spotX = ((i * 7 + (time * 0.02f).toInt()) % (width / gridSize).toInt()) * gridSize
        val spotY = ((i * 13 + (time * 0.015f).toInt()) % (height / gridSize).toInt()) * gridSize

        val pulse = abs(sin(time * 0.05f + i))
        val dotColor = if (i % 3 == 0) Color(0xFF10B981) else Color(0xFF00D2FF)

        drawCircle(
            color = dotColor.copy(alpha = (pulse * 0.7f * glowIntensity).coerceIn(0.1f, 0.85f)),
            radius = 3.5f + pulse * 2f,
            center = Offset(spotX, spotY)
        )
    }
}
