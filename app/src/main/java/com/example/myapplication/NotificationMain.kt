package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotificationMain : Application() {
    companion object {
        fun handel(notificationText: String) {
            NotificationParser.parse(notificationText)
        }
    }
}