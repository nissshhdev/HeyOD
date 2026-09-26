package com.nissshh.heyod.service

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.view.KeyEvent
import com.nissshh.heyod.ui.components.MediaPlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaPlaybackManager(private val context: Context) {

    private var sessionManager: MediaSessionManager? = null
    private var activeController: MediaController? = null
    private val componentName = ComponentName(context, HeyODNotificationListener::class.java)

    private val _mediaState = MutableStateFlow(
        MediaPlaybackState(
            trackTitle = "Pony",
            artistName = "Ginuwine",
            albumTitle = "The Bachelor",
            isPlaying = true,
            albumArtBitmap = null
        )
    )
    val mediaState: StateFlow<MediaPlaybackState> = _mediaState.asStateFlow()

    private val sessionListener = MediaSessionManager.OnActiveSessionsChangedListener { controllers ->
        updateActiveController(controllers)
    }

    private val controllerCallback = object : MediaController.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackState?) {
            updateFromCurrentController()
        }

        override fun onMetadataChanged(metadata: MediaMetadata?) {
            updateFromCurrentController()
        }

        override fun onSessionDestroyed() {
            activeController = null
            findAndAttachController()
        }
    }

    init {
        try {
            sessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager
            sessionManager?.addOnActiveSessionsChangedListener(sessionListener, componentName)
            findAndAttachController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun refresh() {
        findAndAttachController()
    }

    private fun findAndAttachController() {
        try {
            val controllers = sessionManager?.getActiveSessions(componentName)
            updateActiveController(controllers)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateActiveController(controllers: List<MediaController>?) {
        try {
            activeController?.unregisterCallback(controllerCallback)

            // Select active playing controller or first available
            val controller = controllers?.firstOrNull { it.playbackState?.state == PlaybackState.STATE_PLAYING }
                ?: controllers?.firstOrNull()

            activeController = controller
            controller?.registerCallback(controllerCallback)
            updateFromCurrentController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateFromCurrentController() {
        val controller = activeController
        if (controller != null) {
            val metadata = controller.metadata
            val playbackState = controller.playbackState

            val title = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)
                ?: metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)
                ?: "Pony"

            val artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST)
                ?: metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)
                ?: "Ginuwine"

            val album = metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM)
                ?: "The Bachelor"

            val isPlaying = playbackState?.state == PlaybackState.STATE_PLAYING

            val artBitmap: Bitmap? = metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
                ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)
                ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON)

            _mediaState.value = MediaPlaybackState(
                trackTitle = title,
                artistName = artist,
                albumTitle = album,
                isPlaying = isPlaying,
                albumArtBitmap = artBitmap
            )
        } else {
            // Default song as requested: "Pony" by Ginuwine
            val current = _mediaState.value
            _mediaState.value = current.copy(
                trackTitle = if (current.trackTitle == "Starboy") "Pony" else current.trackTitle,
                artistName = if (current.artistName == "The Weeknd") "Ginuwine" else current.artistName,
                albumTitle = if (current.albumTitle == "Starboy") "The Bachelor" else current.albumTitle
            )
        }
    }

    fun togglePlayPause() {
        val controller = activeController
        if (controller != null) {
            val state = controller.playbackState?.state
            if (state == PlaybackState.STATE_PLAYING) {
                controller.transportControls.pause()
            } else {
                controller.transportControls.play()
            }
        } else {
            // Toggle local demo state
            val current = _mediaState.value
            _mediaState.value = current.copy(isPlaying = !current.isPlaying)
        }
    }

    fun skipNext() {
        val controller = activeController
        if (controller != null) {
            controller.transportControls.skipToNext()
        } else {
            // Cycle track
            _mediaState.value = _mediaState.value.copy(
                trackTitle = "Pony",
                artistName = "Ginuwine",
                albumTitle = "The Bachelor"
            )
        }
    }

    fun skipPrevious() {
        val controller = activeController
        if (controller != null) {
            controller.transportControls.skipToPrevious()
        } else {
            _mediaState.value = _mediaState.value.copy(
                trackTitle = "Pony",
                artistName = "Ginuwine",
                albumTitle = "The Bachelor"
            )
        }
    }

    fun release() {
        try {
            activeController?.unregisterCallback(controllerCallback)
            sessionManager?.removeOnActiveSessionsChangedListener(sessionListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
