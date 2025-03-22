package com.example.myapplication

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import javax.inject.Inject

class TransactionSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: TransactionRepository

    override suspend fun doWork(): Result {
        repository.syncPendingTransactions()
        return Result.success()
    }
}