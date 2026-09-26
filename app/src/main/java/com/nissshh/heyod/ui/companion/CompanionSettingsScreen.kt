package com.nissshh.heyod.ui.companion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.data.CalendarStyle
import com.nissshh.heyod.data.DialStyle
import com.nissshh.heyod.ui.theme.GeistMonoFamily

val PaletteColors = listOf(
    0xFFFFFFFF, // Pure White
    0xFFE11D48, // Crimson Red (Accent from reference screenshots)
    0xFF38BDF8, // Cyan Sky
    0xFF22C55E, // Emerald Green
    0xFFF59E0B, // Amber Gold
    0xFFA855F7, // Purple Orchid
    0xFFF43F5E  // Rose
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanionSettingsScreen(
    prefs: AodPreferences,
    onUpdatePrefs: ((AodPreferences) -> AodPreferences) -> Unit,
    onLaunchPreview: () -> Unit,
    onOpenLayoutEditor: () -> Unit,
    onRequestNotificationAccess: () -> Unit,
    onRequestOverlayPermission: () -> Unit,
    onRequestUnrestrictedBattery: () -> Unit,
    hasOverlayPermission: Boolean,
    hasNotificationPermission: Boolean,
    isBatteryUnrestricted: Boolean
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "HeyOD",
                            fontFamily = GeistMonoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Settings",
                            fontFamily = GeistMonoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color(0xFFE11D48)
                        )
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = onOpenLayoutEditor,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFF1E293B),
                            contentColor = Color(0xFF38BDF8)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Icon(Icons.Default.DashboardCustomize, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit Layout", fontFamily = GeistMonoFamily, fontSize = 11.sp)
                    }

                    FilledTonalButton(
                        onClick = onLaunchPreview,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFFE11D48),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Preview", fontFamily = GeistMonoFamily, fontSize = 11.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF000000)
                )
            )
        },
        containerColor = Color(0xFF000000) // Pure AMOLED Black Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF000000)) // Pure AMOLED Black
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Permission & Battery Banner if missing
            if (!hasOverlayPermission || !hasNotificationPermission || !isBatteryUnrestricted) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF080808)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE11D48).copy(alpha = 0.5f)))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            "Recommended System Permissions",
                            fontFamily = GeistMonoFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE11D48),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        if (!hasOverlayPermission) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Display over lock screen", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.LightGray)
                                TextButton(onClick = onRequestOverlayPermission) {
                                    Text("Grant", color = Color(0xFF38BDF8), fontFamily = GeistMonoFamily, fontSize = 12.sp)
                                }
                            }
                        }
                        if (!hasNotificationPermission) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Notification Icons & Badges", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.LightGray)
                                TextButton(onClick = onRequestNotificationAccess) {
                                    Text("Grant", color = Color(0xFF38BDF8), fontFamily = GeistMonoFamily, fontSize = 12.sp)
                                }
                            }
                        }
                        if (!isBatteryUnrestricted) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Unrestricted Battery Usage", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.LightGray)
                                    Text("Prevents Android OS from stopping AOD background service", fontFamily = GeistMonoFamily, fontSize = 10.sp, color = Color.Gray)
                                }
                                TextButton(onClick = onRequestUnrestrictedBattery) {
                                    Text("Enable", color = Color(0xFF22C55E), fontFamily = GeistMonoFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 1. Digital Clock Section
            SettingSectionCard(title = "Digital Clock", icon = Icons.Default.Schedule) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Digital Clock", fontFamily = GeistMonoFamily, color = Color.White)
                    Switch(
                        checked = prefs.digitalClockEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(digitalClockEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                if (prefs.digitalClockEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Font Size: ${prefs.digitalClockFontSizeSp} sp", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.digitalClockFontSizeSp.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(digitalClockFontSizeSp = v.toInt()) } },
                        valueRange = 24f..64f
                    )

                    Text("Vertical Shift (Y Offset): ${prefs.digitalClockOffsetY} px", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.digitalClockOffsetY.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(digitalClockOffsetY = v.toInt()) } },
                        valueRange = -200f..200f
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show Seconds & AM/PM", fontFamily = GeistMonoFamily, fontSize = 13.sp, color = Color.LightGray)
                        Checkbox(
                            checked = prefs.digitalClockShowSeconds,
                            onCheckedChange = { v -> onUpdatePrefs { it.copy(digitalClockShowSeconds = v) } }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Color Accent", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    ColorPickerRow(
                        selectedColor = prefs.digitalClockColorHex,
                        onSelectColor = { color -> onUpdatePrefs { it.copy(digitalClockColorHex = color) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Calendar Customisation Section (New Dedicated Customisation)
            SettingSectionCard(title = "Calendar Customisation", icon = Icons.Default.CalendarToday) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Calendar", fontFamily = GeistMonoFamily, color = Color.White)
                    Switch(
                        checked = prefs.calendarEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(calendarEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                if (prefs.calendarEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Calendar Layout Style", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = prefs.calendarStyle == CalendarStyle.WEEK_ROW,
                            onClick = { onUpdatePrefs { it.copy(calendarStyle = CalendarStyle.WEEK_ROW) } },
                            label = { Text("Week Strip", fontFamily = GeistMonoFamily, fontSize = 10.sp) }
                        )
                        FilterChip(
                            selected = prefs.calendarStyle == CalendarStyle.COMPACT_DATE_ONLY,
                            onClick = { onUpdatePrefs { it.copy(calendarStyle = CalendarStyle.COMPACT_DATE_ONLY) } },
                            label = { Text("Full Date", fontFamily = GeistMonoFamily, fontSize = 10.sp) }
                        )
                        FilterChip(
                            selected = prefs.calendarStyle == CalendarStyle.MINIMAL_DAY_DATE,
                            onClick = { onUpdatePrefs { it.copy(calendarStyle = CalendarStyle.MINIMAL_DAY_DATE) } },
                            label = { Text("Minimal Day", fontFamily = GeistMonoFamily, fontSize = 10.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Font Size: ${prefs.calendarFontSizeSp} sp", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.calendarFontSizeSp.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(calendarFontSizeSp = v.toInt()) } },
                        valueRange = 10f..20f
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show Month & Year Header", fontFamily = GeistMonoFamily, fontSize = 13.sp, color = Color.LightGray)
                        Checkbox(
                            checked = prefs.calendarShowMonth,
                            onCheckedChange = { v -> onUpdatePrefs { it.copy(calendarShowMonth = v) } }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show Week Number (e.g. Week 39)", fontFamily = GeistMonoFamily, fontSize = 13.sp, color = Color.LightGray)
                        Checkbox(
                            checked = prefs.calendarShowWeekNumber,
                            onCheckedChange = { v -> onUpdatePrefs { it.copy(calendarShowWeekNumber = v) } }
                        )
                    }

                    Text("Vertical Shift (Y Offset): ${prefs.calendarOffsetY} px", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.calendarOffsetY.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(calendarOffsetY = v.toInt()) } },
                        valueRange = -150f..150f
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Calendar Highlight & Badge Accent", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    ColorPickerRow(
                        selectedColor = prefs.calendarAccentColorHex,
                        onSelectColor = { color -> onUpdatePrefs { it.copy(calendarAccentColorHex = color) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Analog Clock Section
            SettingSectionCard(title = "Analog Clock (Continuous Sweep)", icon = Icons.Default.AccessTime) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Analog Clock", fontFamily = GeistMonoFamily, color = Color.White)
                    Switch(
                        checked = prefs.analogClockEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(analogClockEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                if (prefs.analogClockEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Clock Diameter: ${prefs.analogClockDiameterDp} dp", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.analogClockDiameterDp.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(analogClockDiameterDp = v.toInt()) } },
                        valueRange = 140f..280f
                    )

                    Text("Vertical Shift (Y Offset): ${prefs.analogClockOffsetY} px", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.analogClockOffsetY.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(analogClockOffsetY = v.toInt()) } },
                        valueRange = -200f..200f
                    )

                    Text("Dial Style", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DialStyle.values().forEach { style ->
                            FilterChip(
                                selected = prefs.analogDialStyle == style,
                                onClick = { onUpdatePrefs { it.copy(analogDialStyle = style) } },
                                label = {
                                    Text(
                                        when (style) {
                                            DialStyle.NUMBERS_AND_TICKS -> "Full Numbers"
                                            DialStyle.MINIMAL_TICKS_ONLY -> "Minimal Ticks"
                                            DialStyle.DOTS -> "Dots"
                                        },
                                        fontFamily = GeistMonoFamily,
                                        fontSize = 11.sp
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Second Hand Color (Sweeping Hand)", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    ColorPickerRow(
                        selectedColor = prefs.analogSecondColorHex,
                        onSelectColor = { color -> onUpdatePrefs { it.copy(analogSecondColorHex = color) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Battery Status Section
            SettingSectionCard(title = "Battery Indicator", icon = Icons.Default.BatteryChargingFull) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Battery Status", fontFamily = GeistMonoFamily, color = Color.White)
                    Switch(
                        checked = prefs.batteryEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(batteryEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                if (prefs.batteryEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Vertical Shift (Y Offset): ${prefs.batteryOffsetY} px", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.batteryOffsetY.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(batteryOffsetY = v.toInt()) } },
                        valueRange = -150f..150f
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show 'Charging...' Label", fontFamily = GeistMonoFamily, fontSize = 13.sp, color = Color.LightGray)
                        Checkbox(
                            checked = prefs.batteryShowChargingText,
                            onCheckedChange = { v -> onUpdatePrefs { it.copy(batteryShowChargingText = v) } }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Battery Icon Color", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    ColorPickerRow(
                        selectedColor = prefs.batteryColorHex,
                        onSelectColor = { color -> onUpdatePrefs { it.copy(batteryColorHex = color) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Notification Grid Section
            SettingSectionCard(title = "Notification Grid & Badges", icon = Icons.Default.Notifications) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Notification Grid", fontFamily = GeistMonoFamily, color = Color.White)
                    Switch(
                        checked = prefs.notificationGridEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(notificationGridEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                if (prefs.notificationGridEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Vertical Shift (Y Offset): ${prefs.notificationGridOffsetY} px", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.notificationGridOffsetY.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(notificationGridOffsetY = v.toInt()) } },
                        valueRange = -150f..150f
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Icon Visual Style", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !prefs.notificationMonochromatic,
                            onClick = { onUpdatePrefs { it.copy(notificationMonochromatic = false) } },
                            label = { Text("Default System App Icons Pack", fontFamily = GeistMonoFamily, fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = prefs.notificationMonochromatic,
                            onClick = { onUpdatePrefs { it.copy(notificationMonochromatic = true) } },
                            label = { Text("Monochrome Transparent", fontFamily = GeistMonoFamily, fontSize = 11.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Badge Accent Color", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    ColorPickerRow(
                        selectedColor = prefs.notificationBadgeColorHex,
                        onSelectColor = { color -> onUpdatePrefs { it.copy(notificationBadgeColorHex = color) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Music & Album Playback Section
            SettingSectionCard(title = "Music & Album Playback", icon = Icons.Default.MusicNote) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Music Playback Widget", fontFamily = GeistMonoFamily, color = Color.White)
                    Switch(
                        checked = prefs.musicPlaybackEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(musicPlaybackEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                if (prefs.musicPlaybackEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show Album Cover Art", fontFamily = GeistMonoFamily, fontSize = 13.sp, color = Color.LightGray)
                        Checkbox(
                            checked = prefs.musicShowAlbumCover,
                            onCheckedChange = { v -> onUpdatePrefs { it.copy(musicShowAlbumCover = v) } }
                        )
                    }

                    Text("Vertical Shift (Y Offset): ${prefs.musicOffsetY} px", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    Slider(
                        value = prefs.musicOffsetY.toFloat(),
                        onValueChange = { v -> onUpdatePrefs { it.copy(musicOffsetY = v.toInt()) } },
                        valueRange = -150f..150f
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Controls & Play Button Accent Color", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    ColorPickerRow(
                        selectedColor = prefs.musicAccentColorHex,
                        onSelectColor = { color -> onUpdatePrefs { it.copy(musicAccentColorHex = color) } }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Battery & OLED Protection + Unrestricted Background Setting
            SettingSectionCard(title = "Battery & OLED Efficiency", icon = Icons.Default.Shield) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Unrestricted Battery Mode", fontFamily = GeistMonoFamily, color = Color.White)
                        Text(
                            if (isBatteryUnrestricted) "Active: Android will not kill HeyOD AOD"
                            else "Standard: Android battery optimization may interrupt AOD",
                            fontFamily = GeistMonoFamily,
                            fontSize = 11.sp,
                            color = if (isBatteryUnrestricted) Color(0xFF22C55E) else Color.Gray
                        )
                    }
                    Button(
                        onClick = onRequestUnrestrictedBattery,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBatteryUnrestricted) Color(0xFF14241B) else Color(0xFFE11D48),
                            contentColor = if (isBatteryUnrestricted) Color(0xFF4ADE80) else Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            if (isBatteryUnrestricted) "Granted" else "Configure",
                            fontFamily = GeistMonoFamily,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Burn-In Pixel Shift", fontFamily = GeistMonoFamily, color = Color.White)
                        Text("Periodically shifts content by ±3px to save OLED pixels", fontFamily = GeistMonoFamily, fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = prefs.burnInProtection,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(burnInProtection = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Pocket Mode Sensor", fontFamily = GeistMonoFamily, color = Color.White)
                        Text("Pauses sweep animations when phone is in pocket or covered", fontFamily = GeistMonoFamily, fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = prefs.pocketModeSensorEnabled,
                        onCheckedChange = { v -> onUpdatePrefs { it.copy(pocketModeSensorEnabled = v) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE11D48), checkedTrackColor = Color(0xFF330510))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7. Reset Layout to Default
            SettingSectionCard(title = "Layout & Arrangement", icon = Icons.Default.Tune) {
                Text(
                    "Restore elements to their standard balanced proportions and zero out custom drag offsets.",
                    fontFamily = GeistMonoFamily,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = {
                        onUpdatePrefs {
                            it.copy(
                                digitalClockOffsetX = 0, digitalClockOffsetY = 0,
                                calendarOffsetY = 0,
                                analogClockOffsetX = 0, analogClockOffsetY = 0,
                                batteryOffsetX = 0, batteryOffsetY = 0,
                                notificationGridOffsetX = 0, notificationGridOffsetY = 0,
                                musicOffsetX = 0, musicOffsetY = 0
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8))
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset Layout Alignment to Default", fontFamily = GeistMonoFamily, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun SettingSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0A0C)), // True AMOLED Black card background
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.08f)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontFamily = GeistMonoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun ColorPickerRow(
    selectedColor: Long,
    onSelectColor: (Long) -> Unit
) {
    val scroll = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .horizontalScroll(scroll),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PaletteColors.forEach { colorHex ->
            val isSelected = selectedColor == colorHex
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(colorHex))
                    .clickable { onSelectColor(colorHex) }
                    .then(
                        if (isSelected) Modifier.border(2.5.dp, Color.White, CircleShape)
                        else Modifier.border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = if (colorHex == 0xFFFFFFFF) Color.Black else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
