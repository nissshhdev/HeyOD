package com.nissshh.heyod.ui.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.ui.theme.GeistMonoFamily

@Composable
fun BatteryIndicatorView(
    prefs: AodPreferences,
    modifier: Modifier = Modifier
) {
    if (!prefs.batteryEnabled) return

    val context = LocalContext.current
    var batteryLevel by remember { mutableIntStateOf(85) }
    var isCharging by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                intent?.let {
                    val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    if (level >= 0 && scale > 0) {
                        batteryLevel = ((level.toFloat() / scale.toFloat()) * 100).toInt()
                    }
                    isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val stickyIntent = context.registerReceiver(receiver, filter)
        stickyIntent?.let {
            val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            if (level >= 0 && scale > 0) {
                batteryLevel = ((level.toFloat() / scale.toFloat()) * 100).toInt()
            }
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
        }

        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (ignored: Exception) {}
        }
    }

    val batteryColor = Color(prefs.batteryColorHex)
    val accentColor = Color(prefs.analogSecondColorHex)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Battery Body Canvas
        Canvas(modifier = Modifier.size(width = 24.dp, height = 12.dp)) {
            val strokeW = 1.6f
            val corner = 2.5f
            val bodyWidth = size.width - 3.5f
            val bodyHeight = size.height

            // Battery shell outline
            drawRoundRect(
                color = batteryColor,
                topLeft = Offset(0f, 0f),
                size = Size(bodyWidth, bodyHeight),
                cornerRadius = CornerRadius(corner, corner),
                style = Stroke(width = strokeW)
            )

            // Positive terminal cap
            val capWidth = 2.2f
            val capHeight = bodyHeight * 0.44f
            drawRoundRect(
                color = batteryColor,
                topLeft = Offset(bodyWidth + 1.2f, (bodyHeight - capHeight) / 2f),
                size = Size(capWidth, capHeight),
                cornerRadius = CornerRadius(1.2f, 1.2f)
            )

            // Inside Fill Level
            val innerPadding = 2.2f
            val fillMaxW = bodyWidth - (innerPadding * 2f)
            val fillActualW = (fillMaxW * (batteryLevel / 100f)).coerceAtLeast(0f)
            val fillH = bodyHeight - (innerPadding * 2f)

            if (fillActualW > 0) {
                drawRoundRect(
                    color = if (batteryLevel <= 15) accentColor else batteryColor,
                    topLeft = Offset(innerPadding, innerPadding),
                    size = Size(fillActualW, fillH),
                    cornerRadius = CornerRadius(1.2f, 1.2f)
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Percentage Text
        Text(
            text = "$batteryLevel%",
            fontFamily = GeistMonoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = batteryColor
        )

        // Charging status indicator (Vibrant Green as requested)
        if (isCharging && prefs.batteryShowChargingText) {
            val chargingGreen = Color(0xFF22C55E) // Bright Emerald Green
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Charging",
                tint = chargingGreen,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Charging...",
                fontFamily = GeistMonoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = chargingGreen
            )
        }
    }
}
