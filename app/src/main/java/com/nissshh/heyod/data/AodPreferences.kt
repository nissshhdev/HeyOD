package com.nissshh.heyod.data

data class AodPreferences(
    // Digital Clock
    val digitalClockEnabled: Boolean = true,
    val digitalClockFontSizeSp: Int = 38,
    val digitalClockColorHex: Long = 0xFFFFFFFF,
    val digitalClockOffsetY: Int = 0,
    val digitalClockOffsetX: Int = 0,
    val digitalClockShowSeconds: Boolean = true,

    // Calendar Customisation
    val calendarEnabled: Boolean = true,
    val calendarAccentColorHex: Long = 0xFFE11D48, // Crimson highlight for current day & month
    val calendarShowWeekNumber: Boolean = true,
    val calendarShowMonth: Boolean = true,
    val calendarFontSizeSp: Int = 13,
    val calendarStyle: CalendarStyle = CalendarStyle.WEEK_ROW, // Default layout matches current live setting with Week Strip
    val calendarOffsetY: Int = 0,

    // Analog Clock
    val analogClockEnabled: Boolean = true,
    val analogClockDiameterDp: Int = 200,
    val analogClockOffsetY: Int = 0,
    val analogClockOffsetX: Int = 0,
    val analogHourColorHex: Long = 0xFFFFFFFF,
    val analogMinuteColorHex: Long = 0xFFFFFFFF,
    val analogSecondColorHex: Long = 0xFFE11D48, // Accent Crimson/Red
    val analogDialStyle: DialStyle = DialStyle.NUMBERS_AND_TICKS,

    // Battery
    val batteryEnabled: Boolean = true,
    val batteryOffsetY: Int = 0,
    val batteryOffsetX: Int = 0,
    val batteryColorHex: Long = 0xFFFFFFFF,
    val batteryShowChargingText: Boolean = true,

    // Notification Grid (Defaults to system/default app icons pack)
    val notificationGridEnabled: Boolean = true,
    val notificationGridOffsetY: Int = 0,
    val notificationGridOffsetX: Int = 0,
    val notificationMonochromatic: Boolean = false, // Default is System Default App Icons pack
    val notificationBadgeColorHex: Long = 0xFFE11D48,

    // Music / Album Playback
    val musicPlaybackEnabled: Boolean = true,
    val musicOffsetY: Int = 0,
    val musicOffsetX: Int = 0,
    val musicAccentColorHex: Long = 0xFFE11D48,
    val musicShowAlbumCover: Boolean = true,

    // General & Power
    val burnInProtection: Boolean = true,
    val brightnessPercent: Int = 15,
    val pocketModeSensorEnabled: Boolean = true
)

enum class DialStyle {
    NUMBERS_AND_TICKS,
    MINIMAL_TICKS_ONLY,
    DOTS
}

enum class CalendarStyle {
    WEEK_ROW,
    COMPACT_DATE_ONLY,
    MINIMAL_DAY_DATE
}
