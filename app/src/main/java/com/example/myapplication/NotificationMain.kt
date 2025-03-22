package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class NotificationMain @Inject constructor(
    var transactionRepository: TransactionRepository
) : Application() {

    suspend fun handle(notificationText: String) {
        val transaction = NotificationParser.parse(notificationText)
        transaction?.let {
            transactionRepository.saveTransaction(it)
        } ?: run {

            println("Transaction is null!")
        }
    }

    companion object
}