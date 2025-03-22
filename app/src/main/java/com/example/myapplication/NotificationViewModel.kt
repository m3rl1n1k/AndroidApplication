package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class NotificationViewModel : ViewModel() {
    private val _notificationText = MutableLiveData<String>()
    val notificationText: LiveData<String> get() = _notificationText

    fun updateNotificationText(newText: String) {
        _notificationText.value = newText
    }
}