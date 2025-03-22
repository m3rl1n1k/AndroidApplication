package com.example.myapplication

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = sbn.packageName
            val notification = sbn.notification
            val title = notification.extras.getString("android.title")
            val text = notification.extras.getString("android.text")

            // Check if title and text are null
            if (title != null && text != null) {
                // Log notification details or send them to your app's UI
                Log.d("NotificationListener", "Notification from $packageName: $title - $text")

                val allowedPackages = listOf("com.example.myapplication")
                if (sbn.packageName in allowedPackages) {
                    // You can update the UI here or send a broadcast if needed
                    sendNotificationToUI("Shop:$title@Amount:$text")
                } else {

                }
            } else {
                // Handle case where title or text is null
                Log.d("NotificationListener", "Received notification with null title or text")
            }
        }
    }

    private fun sendNotificationToUI(notificationText: String) {
        val intent = Intent("com.example.notificationsreader.NOTIFICATION")
        intent.putExtra("notification_text", notificationText)
        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn?.let {
            // Handle notification removal if needed
            Log.d("NotificationListener", "Notification removed: ${sbn.packageName}")
        }
    }
}