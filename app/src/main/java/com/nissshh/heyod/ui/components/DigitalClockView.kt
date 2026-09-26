package com.nissshh.heyod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.data.CalendarStyle
import com.nissshh.heyod.ui.theme.GeistMonoFamily
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun DigitalClockView(
    prefs: AodPreferences,
    modifier: Modifier = Modifier
) {
    if (!prefs.digitalClockEnabled) return

    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            delay(500)
        }
    }

    val hoursMinutes = currentTime.format(DateTimeFormatter.ofPattern("hh:mm"))
    val seconds = currentTime.format(DateTimeFormatter.ofPattern("ss"))
    val amPm = currentTime.format(DateTimeFormatter.ofPattern("a")).uppercase(Locale.getDefault())

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Digital Clock Numbers
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = hoursMinutes,
                fontFamily = GeistMonoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = prefs.digitalClockFontSizeSp.sp,
                color = Color(prefs.digitalClockColorHex),
                letterSpacing = (-1).sp
            )

            if (prefs.digitalClockShowSeconds) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = seconds,
                    fontFamily = GeistMonoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (prefs.digitalClockFontSizeSp * 0.45f).sp,
                    color = Color(prefs.analogSecondColorHex),
                    modifier = Modifier.padding(bottom = (prefs.digitalClockFontSizeSp * 0.08f).dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = amPm,
                    fontFamily = GeistMonoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = (prefs.digitalClockFontSizeSp * 0.3f).sp,
                    color = Color(prefs.digitalClockColorHex).copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = (prefs.digitalClockFontSizeSp * 0.12f).dp)
                )
            }
        }
    }
}

@Composable
fun CalendarStripView(
    prefs: AodPreferences,
    modifier: Modifier = Modifier
) {
    if (!prefs.calendarEnabled) return

    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentDate = LocalDate.now()
            delay(60_000L)
        }
    }

    val accentColor = Color(prefs.calendarAccentColorHex)
    val monthName = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val weekNumber = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfYear())

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fontSize = prefs.calendarFontSizeSp.sp

        when (prefs.calendarStyle) {
            CalendarStyle.WEEK_ROW -> {
                // Header: "September 2026 | Week 39"
                if (prefs.calendarShowMonth || prefs.calendarShowWeekNumber) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (prefs.calendarShowMonth) {
                            Text(
                                text = monthName,
                                fontFamily = GeistMonoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = fontSize,
                                color = accentColor
                            )
                        }
                        if (prefs.calendarShowMonth && prefs.calendarShowWeekNumber) {
                            Text(
                                text = "  |  ",
                                fontFamily = GeistMonoFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = fontSize,
                                color = Color.White.copy(alpha = 0.4f)
                            )
                        }
                        if (prefs.calendarShowWeekNumber) {
                            Text(
                                text = "Week $weekNumber",
                                fontFamily = GeistMonoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = fontSize,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Weekdays: M T W T F S S
                val daysOfWeekLetters = listOf("M", "T", "W", "T", "F", "S", "S")
                val currentDayOfWeek = currentDate.dayOfWeek.value // 1 (Mon) .. 7 (Sun)
                val mondayOfThisWeek = currentDate.minusDays((currentDayOfWeek - 1).toLong())

                Row(
                    modifier = Modifier.width(230.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    daysOfWeekLetters.forEach { letter ->
                        Box(
                            modifier = Modifier.width(26.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                fontFamily = GeistMonoFamily,
                                fontSize = (prefs.calendarFontSizeSp - 3).coerceAtLeast(9).sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .width(230.dp)
                        .height(0.8.dp)
                        .background(Color.White.copy(alpha = 0.12f))
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Days row: 21 22 23 24 25 [26] 27
                Row(
                    modifier = Modifier.width(230.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0..6) {
                        val day = mondayOfThisWeek.plusDays(i.toLong())
                        val isToday = day == currentDate

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .then(
                                    if (isToday) Modifier.background(accentColor, CircleShape)
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.dayOfMonth.toString(),
                                fontFamily = GeistMonoFamily,
                                fontSize = (prefs.calendarFontSizeSp - 2).coerceAtLeast(10).sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                color = if (isToday) Color.White else Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            CalendarStyle.COMPACT_DATE_ONLY -> {
                // Compact format: e.g. "Saturday, 26 September 2026"
                val fullFormatted = currentDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = fullFormatted,
                        fontFamily = GeistMonoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize,
                        color = accentColor
                    )
                    if (prefs.calendarShowWeekNumber) {
                        Text(
                            text = " (W$weekNumber)",
                            fontFamily = GeistMonoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = (prefs.calendarFontSizeSp - 1).coerceAtLeast(10).sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            CalendarStyle.MINIMAL_DAY_DATE -> {
                // Minimal format: e.g. "SAT, SEP 26"
                val minimalFormatted = currentDate.format(DateTimeFormatter.ofPattern("EEE, MMM d")).uppercase()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = minimalFormatted,
                        fontFamily = GeistMonoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = (prefs.calendarFontSizeSp + 1).sp,
                        letterSpacing = 1.sp,
                        color = accentColor
                    )
                    if (prefs.calendarShowWeekNumber) {
                        Text(
                            text = " • W$weekNumber",
                            fontFamily = GeistMonoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = (prefs.calendarFontSizeSp - 1).coerceAtLeast(10).sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
