package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.Switch as Switch
import androidx.core.content.edit
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

class MainActivity : AppCompatActivity() {



    private val notificationViewModel: NotificationViewModel by viewModels()
    private lateinit var notificationTextView: TextView

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val notificationText = intent?.getStringExtra("notification_text")
            notificationText?.let {
                notificationViewModel.updateNotificationText(it)
            } ?: run {
                Log.e("NotificationReceiver", "No notification text found")
            }
        }
    }

    @SuppressLint(
        "UnspecifiedRegisterReceiverFlag", "MissingInflatedId", "SetTextI18n",
        "UseSwitchCompatOrMaterialCode", "ImplicitSamInstance"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val btnOpenSettings = findViewById<Button>(R.id.btnOpenSettings)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnBorn = findViewById<Button>(R.id.callNotification)
        val readNotificationSwitch = findViewById<Switch>(R.id.readNotificationSwitch)

        notificationTextView = findViewById(R.id.notificationTextView)

        // save switch  state
        val state : Boolean = saveState(readNotificationSwitch)

        // ✅ Restore switch state correctly
        readNotificationSwitch.isChecked = loadState()

        // ✅ Observe notifications **outside of the switch condition**
        notificationViewModel.notificationText.observe(this) { notificationText ->
            if (readNotificationSwitch.isChecked) {
                val oldText = notificationTextView.text.toString()
                notificationTextView.text = "$oldText\n$notificationText"
                NotificationMain.handle(notificationText)
            }
        }

        // ✅ Start the service dynamically when the switch is turned on
        readNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveState(readNotificationSwitch)
            if (isChecked) {
                startNotificationListenerService()
            } else {
                stopService(Intent(this, NotificationService::class.java))
            }
        }

        // ✅ Clear notifications properly
        btnClear.setOnClickListener {
            notificationTextView.text = null
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.cancelAll()
        }

        // ✅ Send test notifications
        btnBorn.setOnClickListener {
            for (i in 1..1) {
                sendTestNotification(i)
            }
        }

        // ✅ Open notification settings
        btnOpenSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }

        // ✅ Register receiver
        val filter = IntentFilter("com.example.notificationsreader.NOTIFICATION")
        registerReceiver(notificationReceiver, filter)

        instance = this

        val workRequest = OneTimeWorkRequestBuilder<TransactionSyncWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveState(@SuppressLint("UseSwitchCompatOrMaterialCode") switch: Switch): Boolean {
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        sharedPreferences.edit() {
            putBoolean("switch_state", switch.isChecked)
        } // ✅ APPLY CHANGES
        return switch.isChecked
    }

    private fun loadState(): Boolean {
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        return sharedPreferences.getBoolean("switch_state", false) // Default: false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
    }

    private fun sendTestNotification(id: Int) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android Oreo and above
        val channelId = "test_channel"
        val channelName = "Test Notifications"
        val channelDescription = "This is a test notification channel"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = channelDescription
        notificationManager.createNotificationChannel(channel)

        // Create the notification
        val notification = Notification.Builder(this, "test_channel")
            .setContentTitle("Biedronka")
            .setContentText("PLN10.88 with Paribas")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        // Send the notification
        notificationManager.notify(id, notification)
    }

    private fun startNotificationListenerService() {
        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainActivity
            private set
    }
}


