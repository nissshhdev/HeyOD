package com.nissshh.heyod.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.nissshh.heyod.ui.components.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeyODNotificationListener : NotificationListenerService() {

    companion object {
        private val _notificationsFlow = MutableStateFlow<List<NotificationItem>>(emptyList())
        val notificationsFlow: StateFlow<List<NotificationItem>> = _notificationsFlow.asStateFlow()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        refreshActiveNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        refreshActiveNotifications()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        refreshActiveNotifications()
    }

    private fun refreshActiveNotifications() {
        try {
            val active = activeNotifications ?: return
            val groupedByPackage = active
                .filter { !it.isOngoing }
                .groupBy { it.packageName }

            val items = groupedByPackage.mapNotNull { (pkg, sbns) ->
                try {
                    val appInfo = packageManager.getApplicationInfo(pkg, 0)
                    val iconDrawable = packageManager.getApplicationIcon(appInfo)
                    NotificationItem(
                        id = pkg,
                        packageName = pkg,
                        count = sbns.size,
                        iconDrawable = iconDrawable
                    )
                } catch (e: Exception) {
                    null
                }
            }
            _notificationsFlow.value = items
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
