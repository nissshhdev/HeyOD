package com.nissshh.heyod

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.data.PreferencesRepository
import com.nissshh.heyod.service.AodOverlayService
import com.nissshh.heyod.ui.aod.AodPreviewActivity
import com.nissshh.heyod.ui.companion.CompanionSettingsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var preferencesRepository: PreferencesRepository
    private var isIgnoringBatteryOptimizations by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesRepository = PreferencesRepository(applicationContext)

        checkBatteryOptimizationStatus()
        startAodServiceIfPermitted()

        setContent {
            com.nissshh.heyod.ui.theme.HeyODTheme {
                val prefs by preferencesRepository.aodPreferencesFlow.collectAsState(initial = AodPreferences())
                var showLayoutEditor by remember { mutableStateOf(false) }

                val hasOverlayPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(this)
                } else true

                val notificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
                val hasNotificationPermission = notificationListeners?.contains(packageName) == true

                if (showLayoutEditor) {
                    com.nissshh.heyod.ui.companion.LayoutEditorScreen(
                        prefs = prefs,
                        onUpdatePrefs = { transform ->
                            CoroutineScope(Dispatchers.IO).launch {
                                preferencesRepository.updatePreferences(transform)
                            }
                        },
                        onClose = { showLayoutEditor = false }
                    )
                } else {
                    CompanionSettingsScreen(
                        prefs = prefs,
                        onUpdatePrefs = { transform ->
                            CoroutineScope(Dispatchers.IO).launch {
                                preferencesRepository.updatePreferences(transform)
                            }
                        },
                        onLaunchPreview = {
                            startActivity(Intent(this, AodPreviewActivity::class.java))
                        },
                        onOpenLayoutEditor = {
                            showLayoutEditor = true
                        },
                        onRequestNotificationAccess = {
                            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                        },
                        onRequestOverlayPermission = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:$packageName")
                                )
                                startActivity(intent)
                            }
                        },
                        onRequestUnrestrictedBattery = {
                            requestUnrestrictedBattery()
                        },
                        hasOverlayPermission = hasOverlayPermission,
                        hasNotificationPermission = hasNotificationPermission,
                        isBatteryUnrestricted = isIgnoringBatteryOptimizations
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkBatteryOptimizationStatus()
        startAodServiceIfPermitted()
    }

    private fun checkBatteryOptimizationStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
            isIgnoringBatteryOptimizations = powerManager?.isIgnoringBatteryOptimizations(packageName) == true
        } else {
            isIgnoringBatteryOptimizations = true
        }
    }

    private fun requestUnrestrictedBattery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            } catch (e: Exception) {
                // Fallback to system battery settings
                val fallbackIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(fallbackIntent)
            }
        }
    }

    private fun startAodServiceIfPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            return
        }
        val intent = Intent(this, AodOverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent)
        } else {
            startService(intent)
        }
    }
}
