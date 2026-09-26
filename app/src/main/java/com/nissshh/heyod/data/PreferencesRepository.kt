package com.nissshh.heyod.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore by preferencesDataStore(name = "heyod_preferences")

class PreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val DIGITAL_CLOCK_ENABLED = booleanPreferencesKey("digital_clock_enabled")
        val DIGITAL_CLOCK_FONT_SIZE = intPreferencesKey("digital_clock_font_size")
        val DIGITAL_CLOCK_COLOR = longPreferencesKey("digital_clock_color")
        val DIGITAL_CLOCK_OFFSET_Y = intPreferencesKey("digital_clock_offset_y")
        val DIGITAL_CLOCK_OFFSET_X = intPreferencesKey("digital_clock_offset_x")
        val DIGITAL_CLOCK_SHOW_SECONDS = booleanPreferencesKey("digital_clock_show_seconds")

        val CALENDAR_ENABLED = booleanPreferencesKey("calendar_enabled")
        val CALENDAR_ACCENT_COLOR = longPreferencesKey("calendar_accent_color")
        val CALENDAR_SHOW_WEEK_NUMBER = booleanPreferencesKey("calendar_show_week_number")
        val CALENDAR_SHOW_MONTH = booleanPreferencesKey("calendar_show_month")
        val CALENDAR_FONT_SIZE = intPreferencesKey("calendar_font_size")
        val CALENDAR_STYLE = stringPreferencesKey("calendar_style")
        val CALENDAR_OFFSET_Y = intPreferencesKey("calendar_offset_y")

        val ANALOG_CLOCK_ENABLED = booleanPreferencesKey("analog_clock_enabled")
        val ANALOG_CLOCK_DIAMETER = intPreferencesKey("analog_clock_diameter")
        val ANALOG_CLOCK_OFFSET_Y = intPreferencesKey("analog_clock_offset_y")
        val ANALOG_CLOCK_OFFSET_X = intPreferencesKey("analog_clock_offset_x")
        val ANALOG_HOUR_COLOR = longPreferencesKey("analog_hour_color")
        val ANALOG_MINUTE_COLOR = longPreferencesKey("analog_minute_color")
        val ANALOG_SECOND_COLOR = longPreferencesKey("analog_second_color")
        val ANALOG_DIAL_STYLE = stringPreferencesKey("analog_dial_style")

        val BATTERY_ENABLED = booleanPreferencesKey("battery_enabled")
        val BATTERY_OFFSET_Y = intPreferencesKey("battery_offset_y")
        val BATTERY_OFFSET_X = intPreferencesKey("battery_offset_x")
        val BATTERY_COLOR = longPreferencesKey("battery_color")
        val BATTERY_SHOW_CHARGING_TEXT = booleanPreferencesKey("battery_show_charging_text")

        val NOTIFICATION_GRID_ENABLED = booleanPreferencesKey("notification_grid_enabled")
        val NOTIFICATION_GRID_OFFSET_Y = intPreferencesKey("notification_grid_offset_y")
        val NOTIFICATION_GRID_OFFSET_X = intPreferencesKey("notification_grid_offset_x")
        val NOTIFICATION_MONOCHROMATIC = booleanPreferencesKey("notification_monochromatic")
        val NOTIFICATION_BADGE_COLOR = longPreferencesKey("notification_badge_color")

        val MUSIC_PLAYBACK_ENABLED = booleanPreferencesKey("music_playback_enabled")
        val MUSIC_OFFSET_Y = intPreferencesKey("music_offset_y")
        val MUSIC_OFFSET_X = intPreferencesKey("music_offset_x")
        val MUSIC_ACCENT_COLOR = longPreferencesKey("music_accent_color")
        val MUSIC_SHOW_ALBUM_COVER = booleanPreferencesKey("music_show_album_cover")

        val BURN_IN_PROTECTION = booleanPreferencesKey("burn_in_protection")
        val BRIGHTNESS_PERCENT = intPreferencesKey("brightness_percent")
        val POCKET_MODE_ENABLED = booleanPreferencesKey("pocket_mode_enabled")
    }

    val aodPreferencesFlow: Flow<AodPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val default = AodPreferences()
            AodPreferences(
                digitalClockEnabled = preferences[PreferencesKeys.DIGITAL_CLOCK_ENABLED] ?: default.digitalClockEnabled,
                digitalClockFontSizeSp = preferences[PreferencesKeys.DIGITAL_CLOCK_FONT_SIZE] ?: default.digitalClockFontSizeSp,
                digitalClockColorHex = preferences[PreferencesKeys.DIGITAL_CLOCK_COLOR] ?: default.digitalClockColorHex,
                digitalClockOffsetY = preferences[PreferencesKeys.DIGITAL_CLOCK_OFFSET_Y] ?: default.digitalClockOffsetY,
                digitalClockOffsetX = preferences[PreferencesKeys.DIGITAL_CLOCK_OFFSET_X] ?: default.digitalClockOffsetX,
                digitalClockShowSeconds = preferences[PreferencesKeys.DIGITAL_CLOCK_SHOW_SECONDS] ?: default.digitalClockShowSeconds,

                calendarEnabled = preferences[PreferencesKeys.CALENDAR_ENABLED] ?: default.calendarEnabled,
                calendarAccentColorHex = preferences[PreferencesKeys.CALENDAR_ACCENT_COLOR] ?: default.calendarAccentColorHex,
                calendarShowWeekNumber = preferences[PreferencesKeys.CALENDAR_SHOW_WEEK_NUMBER] ?: default.calendarShowWeekNumber,
                calendarShowMonth = preferences[PreferencesKeys.CALENDAR_SHOW_MONTH] ?: default.calendarShowMonth,
                calendarFontSizeSp = preferences[PreferencesKeys.CALENDAR_FONT_SIZE] ?: default.calendarFontSizeSp,
                calendarStyle = try {
                    CalendarStyle.valueOf(preferences[PreferencesKeys.CALENDAR_STYLE] ?: default.calendarStyle.name)
                } catch (e: Exception) {
                    default.calendarStyle
                },
                calendarOffsetY = preferences[PreferencesKeys.CALENDAR_OFFSET_Y] ?: default.calendarOffsetY,

                analogClockEnabled = preferences[PreferencesKeys.ANALOG_CLOCK_ENABLED] ?: default.analogClockEnabled,
                analogClockDiameterDp = preferences[PreferencesKeys.ANALOG_CLOCK_DIAMETER] ?: default.analogClockDiameterDp,
                analogClockOffsetY = preferences[PreferencesKeys.ANALOG_CLOCK_OFFSET_Y] ?: default.analogClockOffsetY,
                analogClockOffsetX = preferences[PreferencesKeys.ANALOG_CLOCK_OFFSET_X] ?: default.analogClockOffsetX,
                analogHourColorHex = preferences[PreferencesKeys.ANALOG_HOUR_COLOR] ?: default.analogHourColorHex,
                analogMinuteColorHex = preferences[PreferencesKeys.ANALOG_MINUTE_COLOR] ?: default.analogMinuteColorHex,
                analogSecondColorHex = preferences[PreferencesKeys.ANALOG_SECOND_COLOR] ?: default.analogSecondColorHex,
                analogDialStyle = try {
                    DialStyle.valueOf(preferences[PreferencesKeys.ANALOG_DIAL_STYLE] ?: default.analogDialStyle.name)
                } catch (e: Exception) {
                    DialStyle.NUMBERS_AND_TICKS
                },

                batteryEnabled = preferences[PreferencesKeys.BATTERY_ENABLED] ?: default.batteryEnabled,
                batteryOffsetY = preferences[PreferencesKeys.BATTERY_OFFSET_Y] ?: default.batteryOffsetY,
                batteryOffsetX = preferences[PreferencesKeys.BATTERY_OFFSET_X] ?: default.batteryOffsetX,
                batteryColorHex = preferences[PreferencesKeys.BATTERY_COLOR] ?: default.batteryColorHex,
                batteryShowChargingText = preferences[PreferencesKeys.BATTERY_SHOW_CHARGING_TEXT] ?: default.batteryShowChargingText,

                notificationGridEnabled = preferences[PreferencesKeys.NOTIFICATION_GRID_ENABLED] ?: default.notificationGridEnabled,
                notificationGridOffsetY = preferences[PreferencesKeys.NOTIFICATION_GRID_OFFSET_Y] ?: default.notificationGridOffsetY,
                notificationGridOffsetX = preferences[PreferencesKeys.NOTIFICATION_GRID_OFFSET_X] ?: default.notificationGridOffsetX,
                notificationMonochromatic = preferences[PreferencesKeys.NOTIFICATION_MONOCHROMATIC] ?: false, // System app icons pack by default
                notificationBadgeColorHex = preferences[PreferencesKeys.NOTIFICATION_BADGE_COLOR] ?: default.notificationBadgeColorHex,

                musicPlaybackEnabled = preferences[PreferencesKeys.MUSIC_PLAYBACK_ENABLED] ?: default.musicPlaybackEnabled,
                musicOffsetY = preferences[PreferencesKeys.MUSIC_OFFSET_Y] ?: default.musicOffsetY,
                musicOffsetX = preferences[PreferencesKeys.MUSIC_OFFSET_X] ?: default.musicOffsetX,
                musicAccentColorHex = preferences[PreferencesKeys.MUSIC_ACCENT_COLOR] ?: default.musicAccentColorHex,
                musicShowAlbumCover = preferences[PreferencesKeys.MUSIC_SHOW_ALBUM_COVER] ?: default.musicShowAlbumCover,

                burnInProtection = preferences[PreferencesKeys.BURN_IN_PROTECTION] ?: default.burnInProtection,
                brightnessPercent = preferences[PreferencesKeys.BRIGHTNESS_PERCENT] ?: default.brightnessPercent,
                pocketModeSensorEnabled = preferences[PreferencesKeys.POCKET_MODE_ENABLED] ?: default.pocketModeSensorEnabled
            )
        }

    suspend fun updatePreferences(transform: (AodPreferences) -> AodPreferences) {
        context.dataStore.edit { preferences ->
            val default = AodPreferences()
            val current = AodPreferences(
                digitalClockEnabled = preferences[PreferencesKeys.DIGITAL_CLOCK_ENABLED] ?: default.digitalClockEnabled,
                digitalClockFontSizeSp = preferences[PreferencesKeys.DIGITAL_CLOCK_FONT_SIZE] ?: default.digitalClockFontSizeSp,
                digitalClockColorHex = preferences[PreferencesKeys.DIGITAL_CLOCK_COLOR] ?: default.digitalClockColorHex,
                digitalClockOffsetY = preferences[PreferencesKeys.DIGITAL_CLOCK_OFFSET_Y] ?: default.digitalClockOffsetY,
                digitalClockOffsetX = preferences[PreferencesKeys.DIGITAL_CLOCK_OFFSET_X] ?: default.digitalClockOffsetX,
                digitalClockShowSeconds = preferences[PreferencesKeys.DIGITAL_CLOCK_SHOW_SECONDS] ?: default.digitalClockShowSeconds,

                calendarEnabled = preferences[PreferencesKeys.CALENDAR_ENABLED] ?: default.calendarEnabled,
                calendarAccentColorHex = preferences[PreferencesKeys.CALENDAR_ACCENT_COLOR] ?: default.calendarAccentColorHex,
                calendarShowWeekNumber = preferences[PreferencesKeys.CALENDAR_SHOW_WEEK_NUMBER] ?: default.calendarShowWeekNumber,
                calendarShowMonth = preferences[PreferencesKeys.CALENDAR_SHOW_MONTH] ?: default.calendarShowMonth,
                calendarFontSizeSp = preferences[PreferencesKeys.CALENDAR_FONT_SIZE] ?: default.calendarFontSizeSp,
                calendarStyle = try {
                    CalendarStyle.valueOf(preferences[PreferencesKeys.CALENDAR_STYLE] ?: default.calendarStyle.name)
                } catch (e: Exception) {
                    default.calendarStyle
                },
                calendarOffsetY = preferences[PreferencesKeys.CALENDAR_OFFSET_Y] ?: default.calendarOffsetY,

                analogClockEnabled = preferences[PreferencesKeys.ANALOG_CLOCK_ENABLED] ?: default.analogClockEnabled,
                analogClockDiameterDp = preferences[PreferencesKeys.ANALOG_CLOCK_DIAMETER] ?: default.analogClockDiameterDp,
                analogClockOffsetY = preferences[PreferencesKeys.ANALOG_CLOCK_OFFSET_Y] ?: default.analogClockOffsetY,
                analogClockOffsetX = preferences[PreferencesKeys.ANALOG_CLOCK_OFFSET_X] ?: default.analogClockOffsetX,
                analogHourColorHex = preferences[PreferencesKeys.ANALOG_HOUR_COLOR] ?: default.analogHourColorHex,
                analogMinuteColorHex = preferences[PreferencesKeys.ANALOG_MINUTE_COLOR] ?: default.analogMinuteColorHex,
                analogSecondColorHex = preferences[PreferencesKeys.ANALOG_SECOND_COLOR] ?: default.analogSecondColorHex,
                analogDialStyle = try {
                    DialStyle.valueOf(preferences[PreferencesKeys.ANALOG_DIAL_STYLE] ?: default.analogDialStyle.name)
                } catch (e: Exception) {
                    DialStyle.NUMBERS_AND_TICKS
                },

                batteryEnabled = preferences[PreferencesKeys.BATTERY_ENABLED] ?: default.batteryEnabled,
                batteryOffsetY = preferences[PreferencesKeys.BATTERY_OFFSET_Y] ?: default.batteryOffsetY,
                batteryOffsetX = preferences[PreferencesKeys.BATTERY_OFFSET_X] ?: default.batteryOffsetX,
                batteryColorHex = preferences[PreferencesKeys.BATTERY_COLOR] ?: default.batteryColorHex,
                batteryShowChargingText = preferences[PreferencesKeys.BATTERY_SHOW_CHARGING_TEXT] ?: default.batteryShowChargingText,

                notificationGridEnabled = preferences[PreferencesKeys.NOTIFICATION_GRID_ENABLED] ?: default.notificationGridEnabled,
                notificationGridOffsetY = preferences[PreferencesKeys.NOTIFICATION_GRID_OFFSET_Y] ?: default.notificationGridOffsetY,
                notificationGridOffsetX = preferences[PreferencesKeys.NOTIFICATION_GRID_OFFSET_X] ?: default.notificationGridOffsetX,
                notificationMonochromatic = preferences[PreferencesKeys.NOTIFICATION_MONOCHROMATIC] ?: false,
                notificationBadgeColorHex = preferences[PreferencesKeys.NOTIFICATION_BADGE_COLOR] ?: default.notificationBadgeColorHex,

                burnInProtection = preferences[PreferencesKeys.BURN_IN_PROTECTION] ?: default.burnInProtection,
                brightnessPercent = preferences[PreferencesKeys.BRIGHTNESS_PERCENT] ?: default.brightnessPercent,
                pocketModeSensorEnabled = preferences[PreferencesKeys.POCKET_MODE_ENABLED] ?: default.pocketModeSensorEnabled
            )
            val updated = transform(current)

            preferences[PreferencesKeys.DIGITAL_CLOCK_ENABLED] = updated.digitalClockEnabled
            preferences[PreferencesKeys.DIGITAL_CLOCK_FONT_SIZE] = updated.digitalClockFontSizeSp
            preferences[PreferencesKeys.DIGITAL_CLOCK_COLOR] = updated.digitalClockColorHex
            preferences[PreferencesKeys.DIGITAL_CLOCK_OFFSET_Y] = updated.digitalClockOffsetY
            preferences[PreferencesKeys.DIGITAL_CLOCK_OFFSET_X] = updated.digitalClockOffsetX
            preferences[PreferencesKeys.DIGITAL_CLOCK_SHOW_SECONDS] = updated.digitalClockShowSeconds

            preferences[PreferencesKeys.CALENDAR_ENABLED] = updated.calendarEnabled
            preferences[PreferencesKeys.CALENDAR_ACCENT_COLOR] = updated.calendarAccentColorHex
            preferences[PreferencesKeys.CALENDAR_SHOW_WEEK_NUMBER] = updated.calendarShowWeekNumber
            preferences[PreferencesKeys.CALENDAR_SHOW_MONTH] = updated.calendarShowMonth
            preferences[PreferencesKeys.CALENDAR_FONT_SIZE] = updated.calendarFontSizeSp
            preferences[PreferencesKeys.CALENDAR_STYLE] = updated.calendarStyle.name
            preferences[PreferencesKeys.CALENDAR_OFFSET_Y] = updated.calendarOffsetY

            preferences[PreferencesKeys.ANALOG_CLOCK_ENABLED] = updated.analogClockEnabled
            preferences[PreferencesKeys.ANALOG_CLOCK_DIAMETER] = updated.analogClockDiameterDp
            preferences[PreferencesKeys.ANALOG_CLOCK_OFFSET_Y] = updated.analogClockOffsetY
            preferences[PreferencesKeys.ANALOG_CLOCK_OFFSET_X] = updated.analogClockOffsetX
            preferences[PreferencesKeys.ANALOG_HOUR_COLOR] = updated.analogHourColorHex
            preferences[PreferencesKeys.ANALOG_MINUTE_COLOR] = updated.analogMinuteColorHex
            preferences[PreferencesKeys.ANALOG_SECOND_COLOR] = updated.analogSecondColorHex
            preferences[PreferencesKeys.ANALOG_DIAL_STYLE] = updated.analogDialStyle.name

            preferences[PreferencesKeys.BATTERY_ENABLED] = updated.batteryEnabled
            preferences[PreferencesKeys.BATTERY_OFFSET_Y] = updated.batteryOffsetY
            preferences[PreferencesKeys.BATTERY_OFFSET_X] = updated.batteryOffsetX
            preferences[PreferencesKeys.BATTERY_COLOR] = updated.batteryColorHex
            preferences[PreferencesKeys.BATTERY_SHOW_CHARGING_TEXT] = updated.batteryShowChargingText

            preferences[PreferencesKeys.NOTIFICATION_GRID_ENABLED] = updated.notificationGridEnabled
            preferences[PreferencesKeys.NOTIFICATION_GRID_OFFSET_Y] = updated.notificationGridOffsetY
            preferences[PreferencesKeys.NOTIFICATION_GRID_OFFSET_X] = updated.notificationGridOffsetX
            preferences[PreferencesKeys.NOTIFICATION_MONOCHROMATIC] = updated.notificationMonochromatic
            preferences[PreferencesKeys.NOTIFICATION_BADGE_COLOR] = updated.notificationBadgeColorHex

            preferences[PreferencesKeys.MUSIC_PLAYBACK_ENABLED] = updated.musicPlaybackEnabled
            preferences[PreferencesKeys.MUSIC_OFFSET_Y] = updated.musicOffsetY
            preferences[PreferencesKeys.MUSIC_OFFSET_X] = updated.musicOffsetX
            preferences[PreferencesKeys.MUSIC_ACCENT_COLOR] = updated.musicAccentColorHex
            preferences[PreferencesKeys.MUSIC_SHOW_ALBUM_COVER] = updated.musicShowAlbumCover

            preferences[PreferencesKeys.BURN_IN_PROTECTION] = updated.burnInProtection
            preferences[PreferencesKeys.BRIGHTNESS_PERCENT] = updated.brightnessPercent
            preferences[PreferencesKeys.POCKET_MODE_ENABLED] = updated.pocketModeSensorEnabled
        }
    }
}
