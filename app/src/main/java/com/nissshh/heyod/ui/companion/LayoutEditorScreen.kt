package com.nissshh.heyod.ui.companion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.ui.components.AnalogClockView
import com.nissshh.heyod.ui.components.BatteryIndicatorView
import com.nissshh.heyod.ui.components.CalendarStripView
import com.nissshh.heyod.ui.components.DigitalClockView
import com.nissshh.heyod.ui.components.MusicPlaybackView
import com.nissshh.heyod.ui.components.NotificationGridView
import com.nissshh.heyod.ui.theme.GeistMonoFamily
import kotlin.math.roundToInt

enum class EditableElement(val displayName: String) {
    DIGITAL_CLOCK("Digital Clock"),
    CALENDAR("Calendar"),
    ANALOG_CLOCK("Analog Clock"),
    BATTERY("Battery"),
    NOTIFICATIONS("Notifications"),
    MUSIC("Music Playback")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutEditorScreen(
    prefs: AodPreferences,
    onUpdatePrefs: ((AodPreferences) -> AodPreferences) -> Unit,
    onClose: () -> Unit
) {
    var selectedElement by remember { mutableStateOf(EditableElement.DIGITAL_CLOCK) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Layout Canvas Editor",
                            fontFamily = GeistMonoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            "Drag to move • Sliders to resize & recolor",
                            fontFamily = GeistMonoFamily,
                            fontSize = 11.sp,
                            color = Color(0xFFE11D48)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // Reset all offsets
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
                    }) {
                        Text("Reset", fontFamily = GeistMonoFamily, color = Color.Gray, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            EditorControlPanel(
                selectedElement = selectedElement,
                onSelectElement = { selectedElement = it },
                prefs = prefs,
                onUpdatePrefs = onUpdatePrefs
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // Visual outline indicating phone screen boundary
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                // Main Upper & Mid Stack
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Digital Clock
                    if (prefs.digitalClockEnabled) {
                        SelectableEditorItem(
                            isSelected = selectedElement == EditableElement.DIGITAL_CLOCK,
                            onSelect = { selectedElement = EditableElement.DIGITAL_CLOCK },
                            offsetX = prefs.digitalClockOffsetX,
                            offsetY = prefs.digitalClockOffsetY,
                            onDrag = { dx, dy ->
                                onUpdatePrefs {
                                    it.copy(
                                        digitalClockOffsetX = it.digitalClockOffsetX + dx.roundToInt(),
                                        digitalClockOffsetY = it.digitalClockOffsetY + dy.roundToInt()
                                    )
                                }
                            }
                        ) {
                            DigitalClockView(prefs = prefs)
                        }
                    }

                    // 2. Calendar
                    if (prefs.calendarEnabled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        SelectableEditorItem(
                            isSelected = selectedElement == EditableElement.CALENDAR,
                            onSelect = { selectedElement = EditableElement.CALENDAR },
                            offsetX = 0,
                            offsetY = prefs.calendarOffsetY,
                            onDrag = { _, dy ->
                                onUpdatePrefs {
                                    it.copy(calendarOffsetY = it.calendarOffsetY + dy.roundToInt())
                                }
                            }
                        ) {
                            CalendarStripView(prefs = prefs)
                        }
                    }

                    // 3. Analog Clock
                    if (prefs.analogClockEnabled) {
                        Spacer(modifier = Modifier.height(20.dp))
                        SelectableEditorItem(
                            isSelected = selectedElement == EditableElement.ANALOG_CLOCK,
                            onSelect = { selectedElement = EditableElement.ANALOG_CLOCK },
                            offsetX = prefs.analogClockOffsetX,
                            offsetY = prefs.analogClockOffsetY,
                            onDrag = { dx, dy ->
                                onUpdatePrefs {
                                    it.copy(
                                        analogClockOffsetX = it.analogClockOffsetX + dx.roundToInt(),
                                        analogClockOffsetY = it.analogClockOffsetY + dy.roundToInt()
                                    )
                                }
                            }
                        ) {
                            AnalogClockView(prefs = prefs, isSuspended = false)
                        }
                    }

                    // 4. Battery
                    if (prefs.batteryEnabled) {
                        Spacer(modifier = Modifier.height(20.dp))
                        SelectableEditorItem(
                            isSelected = selectedElement == EditableElement.BATTERY,
                            onSelect = { selectedElement = EditableElement.BATTERY },
                            offsetX = prefs.batteryOffsetX,
                            offsetY = prefs.batteryOffsetY,
                            onDrag = { dx, dy ->
                                onUpdatePrefs {
                                    it.copy(
                                        batteryOffsetX = it.batteryOffsetX + dx.roundToInt(),
                                        batteryOffsetY = it.batteryOffsetY + dy.roundToInt()
                                    )
                                }
                            }
                        ) {
                            BatteryIndicatorView(prefs = prefs)
                        }
                    }

                    // 5. Notifications
                    if (prefs.notificationGridEnabled) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SelectableEditorItem(
                            isSelected = selectedElement == EditableElement.NOTIFICATIONS,
                            onSelect = { selectedElement = EditableElement.NOTIFICATIONS },
                            offsetX = prefs.notificationGridOffsetX,
                            offsetY = prefs.notificationGridOffsetY,
                            onDrag = { dx, dy ->
                                onUpdatePrefs {
                                    it.copy(
                                        notificationGridOffsetX = it.notificationGridOffsetX + dx.roundToInt(),
                                        notificationGridOffsetY = it.notificationGridOffsetY + dy.roundToInt()
                                    )
                                }
                            }
                        ) {
                            NotificationGridView(prefs = prefs, notifications = emptyList())
                        }
                    }
                }

                // 6. Music Playback (Docked towards Bottom)
                if (prefs.musicPlaybackEnabled) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                    ) {
                        SelectableEditorItem(
                            isSelected = selectedElement == EditableElement.MUSIC,
                            onSelect = { selectedElement = EditableElement.MUSIC },
                            offsetX = prefs.musicOffsetX,
                            offsetY = prefs.musicOffsetY,
                            onDrag = { dx, dy ->
                                onUpdatePrefs {
                                    it.copy(
                                        musicOffsetX = it.musicOffsetX + dx.roundToInt(),
                                        musicOffsetY = it.musicOffsetY + dy.roundToInt()
                                    )
                                }
                            }
                        ) {
                            MusicPlaybackView(prefs = prefs)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectableEditorItem(
    isSelected: Boolean,
    onSelect: () -> Unit,
    offsetX: Int,
    offsetY: Int,
    onDrag: (Float, Float) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX, offsetY) }
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFFE11D48) else Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(if (isSelected) Color(0xFFE11D48).copy(alpha = 0.08f) else Color.Transparent)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onSelect() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount.x, dragAmount.y)
                    }
                )
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun EditorControlPanel(
    selectedElement: EditableElement,
    onSelectElement: (EditableElement) -> Unit,
    prefs: AodPreferences,
    onUpdatePrefs: ((AodPreferences) -> AodPreferences) -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        color = Color(0xFF0A0A0C),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Element Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EditableElement.values().forEach { elem ->
                    val isCur = elem == selectedElement
                    FilterChip(
                        selected = isCur,
                        onClick = { onSelectElement(elem) },
                        label = { Text(elem.displayName, fontFamily = GeistMonoFamily, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE11D48),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Divider(color = Color.White.copy(alpha = 0.08f), modifier = Modifier.padding(bottom = 12.dp))

            // Dynamic Controls according to selected element
            when (selectedElement) {
                EditableElement.DIGITAL_CLOCK -> {
                    Text("Digital Clock Resize & Color", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    SliderRow(
                        label = "Font Size",
                        value = prefs.digitalClockFontSizeSp.toFloat(),
                        range = 20f..70f,
                        unit = "sp",
                        onValueChange = { v -> onUpdatePrefs { it.copy(digitalClockFontSizeSp = v.toInt()) } }
                    )
                    PaletteColorRow(
                        selectedColor = prefs.digitalClockColorHex,
                        onSelectColor = { c -> onUpdatePrefs { it.copy(digitalClockColorHex = c) } }
                    )
                }

                EditableElement.CALENDAR -> {
                    Text("Calendar Text Size & Accent", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    SliderRow(
                        label = "Font Size",
                        value = prefs.calendarFontSizeSp.toFloat(),
                        range = 10f..22f,
                        unit = "sp",
                        onValueChange = { v -> onUpdatePrefs { it.copy(calendarFontSizeSp = v.toInt()) } }
                    )
                    PaletteColorRow(
                        selectedColor = prefs.calendarAccentColorHex,
                        onSelectColor = { c -> onUpdatePrefs { it.copy(calendarAccentColorHex = c) } }
                    )
                }

                EditableElement.ANALOG_CLOCK -> {
                    Text("Analog Clock Resize & Hand Colors", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    SliderRow(
                        label = "Diameter",
                        value = prefs.analogClockDiameterDp.toFloat(),
                        range = 140f..320f,
                        unit = "dp",
                        onValueChange = { v -> onUpdatePrefs { it.copy(analogClockDiameterDp = v.toInt()) } }
                    )
                    Text("Second Hand Color:", fontFamily = GeistMonoFamily, fontSize = 11.sp, color = Color.LightGray)
                    PaletteColorRow(
                        selectedColor = prefs.analogSecondColorHex,
                        onSelectColor = { c -> onUpdatePrefs { it.copy(analogSecondColorHex = c) } }
                    )
                }

                EditableElement.BATTERY -> {
                    Text("Battery Icon Color", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    PaletteColorRow(
                        selectedColor = prefs.batteryColorHex,
                        onSelectColor = { c -> onUpdatePrefs { it.copy(batteryColorHex = c) } }
                    )
                }

                EditableElement.NOTIFICATIONS -> {
                    Text("Notification Badge Accent Color", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    PaletteColorRow(
                        selectedColor = prefs.notificationBadgeColorHex,
                        onSelectColor = { c -> onUpdatePrefs { it.copy(notificationBadgeColorHex = c) } }
                    )
                }

                EditableElement.MUSIC -> {
                    Text("Music Accent & Album Controls", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.Gray)
                    PaletteColorRow(
                        selectedColor = prefs.musicAccentColorHex,
                        onSelectColor = { c -> onUpdatePrefs { it.copy(musicAccentColorHex = c) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderRow(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    unit: String,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label: ${value.toInt()} $unit", fontFamily = GeistMonoFamily, fontSize = 12.sp, color = Color.White, modifier = Modifier.width(130.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PaletteColorRow(
    selectedColor: Long,
    onSelectColor: (Long) -> Unit
) {
    val palette = listOf(0xFFFFFFFF, 0xFFE11D48, 0xFF38BDF8, 0xFF22C55E, 0xFFF59E0B, 0xFFA855F7, 0xFFF43F5E)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        palette.forEach { c ->
            val isSelected = selectedColor == c
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(c))
                    .border(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable { onSelectColor(c) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = if (c == 0xFFFFFFFF) Color.Black else Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
