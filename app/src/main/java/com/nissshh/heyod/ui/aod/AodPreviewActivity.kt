package com.nissshh.heyod.ui.aod

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nissshh.heyod.data.AodPreferences
import com.nissshh.heyod.data.PreferencesRepository
import com.nissshh.heyod.service.HeyODNotificationListener
import com.nissshh.heyod.service.MediaPlaybackManager

class AodPreviewActivity : ComponentActivity() {

    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var mediaPlaybackManager: MediaPlaybackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesRepository = PreferencesRepository(applicationContext)
        mediaPlaybackManager = MediaPlaybackManager(applicationContext)

        // Android 8.0+ / 9.0+ / 10+ / 14+ Lockscreen & AOD flags
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val km = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            km?.requestDismissKeyguard(this, null)
        }

        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Native Fullscreen & Amoled black immersive mode
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        setContent {
            val prefs by preferencesRepository.aodPreferencesFlow.collectAsState(initial = AodPreferences())
            val notifications by HeyODNotificationListener.notificationsFlow.collectAsState()
            val mediaState by mediaPlaybackManager.mediaState.collectAsState()

            AodScreen(
                prefs = prefs,
                notifications = notifications,
                mediaState = mediaState,
                onPlayPauseToggle = {
                    mediaPlaybackManager.togglePlayPause()
                },
                onSkipNext = {
                    mediaPlaybackManager.skipNext()
                },
                onSkipPrevious = {
                    mediaPlaybackManager.skipPrevious()
                },
                isSuspended = false,
                onDoubleTapToExit = {
                    finish()
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlaybackManager.release()
    }
}
