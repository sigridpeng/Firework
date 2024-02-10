package com.classam.firework.firework

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FireworkOrb(
    colorLine: Color = Color.Green,
    colorOrb: Color = Color.Cyan,
    lineWidth: Dp = 1.dp,
    orbNumber: Int = 24,
    orbLayer: Int = 2,
    orbRadius: Dp = 2.dp,
    width: Dp = 300.dp,
    height: Dp = 500.dp
) {
    var elapsedTime by remember { mutableStateOf(0L) }
    var alphaValue by remember { mutableStateOf(1f) }
    var isBurst by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    var fixY = 0f

    if (!isFinished) {
        LaunchedEffect(Unit) {
            while (!isFinished) {
                delay(50) // Every 0.05 seconds.
                elapsedTime += 50 // Accumulate time in increments of 50 milliseconds.
            }
        }

        Canvas(
            modifier = Modifier
                .width(width)
                .height(height)
                .background(Color.Transparent)
        ) {
            val centerX = size.width / 2f
            val baseY = size.height
            val duration = 4000L // The duration of the fireworks is 4 seconds.
            val radius = (width / 5).toPx() // The radius of the fireworks.

            // Calculate the position of the fireworks at different time points,
            // reaching the highest point at the third second, hence at 3/4 of the height.
            val currentY = if (isBurst) fixY else baseY - (elapsedTime.toFloat() / duration) * baseY
            // The initial radius of the orb's trajectory at the start of the burst.
            val radiusOffset = (elapsedTime.toFloat() / duration) * radius

            // Draw a vertical line.
            drawLine(
                color = colorLine.copy(alpha = alphaValue),
                start = Offset(centerX, baseY),
                end = if (isBurst) Offset(centerX, fixY) else Offset(centerX, currentY),
                strokeWidth = lineWidth.toPx()
            )
            // The vertical line fades out after the burst.
            if (isBurst) {
                alphaValue = 1f - (4 * (radiusOffset - radius) / radius).coerceIn(0f, 1f)
            }

            // Burst at the third second.
            if (elapsedTime >= 3000) {
                if (elapsedTime.toInt() == 3000) {
                    fixY = currentY
                    isBurst = true
                }

                repeat(orbLayer) { orbLayer ->
                    repeat(orbNumber) { index ->
                        val angle = index * 360 / orbNumber
                        val pointX =
                            centerX + radiusOffset * (orbLayer + 1) * cos(Math.toRadians(angle.toDouble())).toFloat()
                        val pointY =
                            fixY + radiusOffset * (orbLayer + 1) * sin(Math.toRadians(angle.toDouble())).toFloat()

                        // The orb fades out after the burst.
                        val alpha = 1f - (3 * (radiusOffset - radius) / radius).coerceIn(0f, 1f)
                        drawCircle(
                            color = colorOrb.copy(alpha = alpha),
                            center = Offset(pointX, pointY),
                            radius = orbRadius.toPx()
                        )
                        if (alpha <= 0f) {
                            isFinished = true
                        }
                    }
                }
            }
        }
    }
}