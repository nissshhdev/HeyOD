package com.nissshh.heyod.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.nissshh.heyod.R
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.data.DialStyle
import com.nissshh.heyod.ui.theme.GeistMonoFamily
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClockView(
    prefs: AodPreferences,
    isSuspended: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (!prefs.analogClockEnabled) return

    val context = LocalContext.current
    val customTypeface = remember {
        try {
            ResourcesCompat.getFont(context, R.font.geist_mono)
        } catch (e: Exception) {
            null
        }
    }

    // High precision smooth continuous sweeping time
    var millisOffset by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(isSuspended) {
        if (!isSuspended) {
            while (true) {
                withFrameMillis { frameTimeMillis ->
                    millisOffset = System.currentTimeMillis()
                }
            }
        }
    }

    val now = LocalTime.now()
    val currentMillisInSecond = (millisOffset % 1000).toFloat()
    val secondContinuous = now.second + (currentMillisInSecond / 1000f)
    val minuteContinuous = now.minute + (secondContinuous / 60f)
    val hourContinuous = (now.hour % 12) + (minuteContinuous / 60f)

    val secondAngle = (secondContinuous * 6f) - 90f
    val minuteAngle = (minuteContinuous * 6f) - 90f
    val hourAngle = (hourContinuous * 30f) - 90f

    val hourColor = Color(prefs.analogHourColorHex)
    val minuteColor = Color(prefs.analogMinuteColorHex)
    val secondColor = Color(prefs.analogSecondColorHex)

    val amPmText = now.format(DateTimeFormatter.ofPattern("a")).uppercase(Locale.getDefault())

    Box(
        modifier = modifier
            .size(prefs.analogClockDiameterDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.width / 2f

            // 1. Draw Dial Elements (Numbers / Ticks)
            when (prefs.analogDialStyle) {
                DialStyle.NUMBERS_AND_TICKS -> {
                    // Draw outer ticks (60 minute markers)
                    for (i in 0 until 60) {
                        val angleRad = Math.toRadians((i * 6 - 90).toDouble())
                        val isHourTick = i % 5 == 0
                        val tickLen = if (isHourTick) radius * 0.12f else radius * 0.05f
                        val strokeW = if (isHourTick) 2.5f else 1.2f
                        val tickAlpha = if (isHourTick) 0.9f else 0.35f

                        val startX = center.x + ((radius - tickLen) * cos(angleRad)).toFloat()
                        val startY = center.y + ((radius - tickLen) * sin(angleRad)).toFloat()
                        val endX = center.x + (radius * cos(angleRad)).toFloat()
                        val endY = center.y + (radius * sin(angleRad)).toFloat()

                        drawLine(
                            color = Color.White.copy(alpha = tickAlpha),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )
                    }

                    // Draw Numbers 1..12
                    val paint = android.graphics.Paint().apply {
                        this.color = android.graphics.Color.WHITE
                        this.textSize = radius * 0.16f
                        this.textAlign = android.graphics.Paint.Align.CENTER
                        this.isAntiAlias = true
                        customTypeface?.let { typeface = it }
                    }

                    val numberRadius = radius * 0.73f
                    for (hour in 1..12) {
                        val angleRad = Math.toRadians((hour * 30 - 90).toDouble())
                        val nx = center.x + (numberRadius * cos(angleRad)).toFloat()
                        val ny = center.y + (numberRadius * sin(angleRad)).toFloat() + (paint.textSize / 3f)

                        drawContext.canvas.nativeCanvas.drawText(
                            hour.toString(),
                            nx,
                            ny,
                            paint
                        )
                    }
                }

                DialStyle.MINIMAL_TICKS_ONLY -> {
                    for (i in 0 until 12) {
                        val angleRad = Math.toRadians((i * 30 - 90).toDouble())
                        val tickLen = radius * 0.15f
                        val startX = center.x + ((radius - tickLen) * cos(angleRad)).toFloat()
                        val startY = center.y + ((radius - tickLen) * sin(angleRad)).toFloat()
                        val endX = center.x + (radius * cos(angleRad)).toFloat()
                        val endY = center.y + (radius * sin(angleRad)).toFloat()

                        drawLine(
                            color = Color.White.copy(alpha = 0.85f),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                DialStyle.DOTS -> {
                    for (i in 0 until 12) {
                        val angleRad = Math.toRadians((i * 30 - 90).toDouble())
                        val dx = center.x + ((radius - 12f) * cos(angleRad)).toFloat()
                        val dy = center.y + ((radius - 12f) * sin(angleRad)).toFloat()

                        drawCircle(
                            color = Color.White.copy(alpha = 0.75f),
                            radius = 3f,
                            center = Offset(dx, dy)
                        )
                    }
                }
            }

            // 2. Draw Hour Hand
            val hourRad = Math.toRadians(hourAngle.toDouble())
            val hourLen = radius * 0.48f
            drawLine(
                color = hourColor,
                start = center - Offset((cos(hourRad) * 12f).toFloat(), (sin(hourRad) * 12f).toFloat()),
                end = Offset(
                    center.x + (hourLen * cos(hourRad)).toFloat(),
                    center.y + (hourLen * sin(hourRad)).toFloat()
                ),
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )

            // 3. Draw Minute Hand
            val minRad = Math.toRadians(minuteAngle.toDouble())
            val minLen = radius * 0.68f
            drawLine(
                color = minuteColor,
                start = center - Offset((cos(minRad) * 16f).toFloat(), (sin(minRad) * 16f).toFloat()),
                end = Offset(
                    center.x + (minLen * cos(minRad)).toFloat(),
                    center.y + (minLen * sin(minRad)).toFloat()
                ),
                strokeWidth = 4.5f,
                cap = StrokeCap.Round
            )

            // 4. Draw Continuous Smooth Second Hand
            val secRad = Math.toRadians(secondAngle.toDouble())
            val secLen = radius * 0.82f
            val secTail = radius * 0.22f

            // Second hand tail
            drawLine(
                color = secondColor,
                start = center - Offset((cos(secRad) * secTail).toFloat(), (sin(secRad) * secTail).toFloat()),
                end = Offset(
                    center.x + (secLen * cos(secRad)).toFloat(),
                    center.y + (secLen * sin(secRad)).toFloat()
                ),
                strokeWidth = 2.2f,
                cap = StrokeCap.Round
            )

            // Center Pin Hub
            drawCircle(
                color = secondColor,
                radius = 5.5f,
                center = center
            )
            drawCircle(
                color = Color.Black,
                radius = 2.2f,
                center = center
            )
        }

        // Center AM/PM indicator text (as seen in Screenshot 2 & 3)
        Box(
            modifier = Modifier.padding(bottom = (prefs.analogClockDiameterDp * 0.12f).dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = amPmText,
                fontFamily = GeistMonoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}
