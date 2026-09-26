package com.nissshh.heyod.ui.aod

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.ui.components.AnalogClockView
import com.nissshh.heyod.ui.components.BatteryIndicatorView
import com.nissshh.heyod.ui.components.CalendarStripView
import com.nissshh.heyod.ui.components.DigitalClockView
import com.nissshh.heyod.ui.components.NotificationGridView
import com.nissshh.heyod.ui.components.NotificationItem
import kotlinx.coroutines.delay
import kotlin.random.Random

import com.nissshh.heyod.ui.components.MediaPlaybackState

@Composable
fun AodScreen(
    prefs: AodPreferences,
    notifications: List<NotificationItem> = emptyList(),
    mediaState: MediaPlaybackState = MediaPlaybackState(),
    onPlayPauseToggle: () -> Unit = {},
    onSkipNext: () -> Unit = {},
    onSkipPrevious: () -> Unit = {},
    isSuspended: Boolean = false,
    onDoubleTapToExit: () -> Unit = {}
) {
    // OLED Burn-in mitigation: Periodic subtle pixel shift (±3 pixels every 60s)
    var pixelShiftX by remember { mutableIntStateOf(0) }
    var pixelShiftY by remember { mutableIntStateOf(0) }

    LaunchedEffect(prefs.burnInProtection) {
        if (prefs.burnInProtection) {
            while (true) {
                delay(60_000L) // every 1 minute
                pixelShiftX = Random.nextInt(-3, 4)
                pixelShiftY = Random.nextInt(-3, 4)
            }
        } else {
            pixelShiftX = 0
            pixelShiftY = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onDoubleTapToExit()
                    }
                )
            }
    ) {
        // Obscured / Pocket overlay: when suspended, turn off pixels completely
        if (isSuspended) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
            return@Box
        }

        // Sleek, well-proportioned AOD layout with OLED burn-in pixel-shifting
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = pixelShiftX,
                        y = pixelShiftY
                    )
                }
        ) {
            // Main Content Stack: Centered high-priority glanceable information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp, bottom = 120.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Digital Clock (Top Primary Glance)
                if (prefs.digitalClockEnabled) {
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(x = prefs.digitalClockOffsetX, y = prefs.digitalClockOffsetY)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        DigitalClockView(prefs = prefs)
                    }
                }

                // 2. Calendar (Subtle, contextual date & schedule display)
                if (prefs.calendarEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(x = 0, y = prefs.calendarOffsetY)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        CalendarStripView(prefs = prefs)
                    }
                }

                // 3. Analog Clock (Elegant Sweep Dial)
                if (prefs.analogClockEnabled) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(x = prefs.analogClockOffsetX, y = prefs.analogClockOffsetY)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        AnalogClockView(prefs = prefs, isSuspended = isSuspended)
                    }
                }

                // 4. Battery Indicator (Clean pill battery & status)
                if (prefs.batteryEnabled) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(x = prefs.batteryOffsetX, y = prefs.batteryOffsetY)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        BatteryIndicatorView(prefs = prefs)
                    }
                }

                // 5. Notification Grid & Badges (Clean system app icons pack)
                if (prefs.notificationGridEnabled) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(x = prefs.notificationGridOffsetX, y = prefs.notificationGridOffsetY)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        NotificationGridView(
                            prefs = prefs,
                            notifications = notifications
                        )
                    }
                }
            }

            // 6. Music / Album Playback (Gracefully docked towards the bottom)
            if (prefs.musicPlaybackEnabled) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp)
                        .offset {
                            IntOffset(x = prefs.musicOffsetX, y = prefs.musicOffsetY)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    com.nissshh.heyod.ui.components.MusicPlaybackView(
                        prefs = prefs,
                        mediaState = mediaState,
                        onPlayPauseToggle = onPlayPauseToggle,
                        onSkipNext = onSkipNext,
                        onSkipPrevious = onSkipPrevious
                    )
                }
            }
        }
    }
}
