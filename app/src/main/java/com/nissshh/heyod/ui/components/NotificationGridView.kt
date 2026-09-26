package com.nissshh.heyod.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.ui.theme.GeistMonoFamily

data class NotificationItem(
    val id: String,
    val packageName: String,
    val count: Int = 1,
    val iconDrawable: Drawable? = null
)

fun drawableToSafeBitmap(drawable: Drawable, targetSize: Int = 96): Bitmap {
    return try {
        val w = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else targetSize
        val h = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else targetSize
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } catch (e: Exception) {
        Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888)
    }
}

@Composable
fun NotificationGridView(
    prefs: AodPreferences,
    notifications: List<NotificationItem>,
    modifier: Modifier = Modifier
) {
    if (!prefs.notificationGridEnabled) return

    val context = LocalContext.current
    val pm = context.packageManager

    // Fallback realistic system packages if no active notifications currently posted
    val displayList = remember(notifications) {
        if (notifications.isNotEmpty()) {
            notifications
        } else {
            val samplePackages = listOf(
                "com.google.android.dialer" to 1, // Phone
                "com.google.android.gm" to 2, // Gmail
                "com.google.android.apps.messaging" to 3 // Messages
            )
            samplePackages.map { (pkg, cnt) ->
                val drawable = try {
                    pm.getApplicationIcon(pkg)
                } catch (e: Exception) {
                    try {
                        pm.getDefaultActivityIcon()
                    } catch (ex: Exception) {
                        null
                    }
                }
                NotificationItem(
                    id = pkg,
                    packageName = pkg,
                    count = cnt,
                    iconDrawable = drawable
                )
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        displayList.take(5).forEachIndexed { index, item ->
            NotificationBadgeIcon(
                item = item,
                prefs = prefs
            )
            if (index < displayList.size - 1) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
private fun NotificationBadgeIcon(
    item: NotificationItem,
    prefs: AodPreferences
) {
    val badgeColor = Color(prefs.notificationBadgeColorHex)
    val isMonochrome = prefs.notificationMonochromatic

    Box(
        modifier = Modifier.size(46.dp),
        contentAlignment = Alignment.Center
    ) {
        // App Icon Container
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isMonochrome) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.05f))
                .border(
                    width = 1.dp,
                    color = if (isMonochrome) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.iconDrawable != null) {
                val bitmap = remember(item.iconDrawable) {
                    drawableToSafeBitmap(item.iconDrawable, 96).asImageBitmap()
                }
                Image(
                    bitmap = bitmap,
                    contentDescription = item.packageName,
                    modifier = Modifier.size(26.dp),
                    // If isMonochrome is false, preserve 100% original full-color system app icon pack!
                    colorFilter = if (isMonochrome) ColorFilter.tint(Color.White.copy(alpha = 0.9f)) else null
                )
            }
        }

        // Notification Count Badge (Top-Right)
        if (item.count > 0) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .background(badgeColor, CircleShape)
                    .border(1.2.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (item.count > 9) "9+" else item.count.toString(),
                    fontFamily = GeistMonoFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
