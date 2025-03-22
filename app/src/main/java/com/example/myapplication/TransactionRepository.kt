package com.example.myapplication

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val api: TransactionApi
) {
    suspend fun saveTransaction(transaction: TransactionDTO) {
        if (NetworkChecker.isOnline()) {
            try {
                api.sendTransaction(transaction)
            } catch (e: Exception) {
                transactionDao.insert(transaction)
            }
        } else {
            transactionDao.insert(transaction)
        }
    }

    suspend fun syncPendingTransactions() {
        if (NetworkChecker.isOnline()) {
            val pending = transactionDao.getAll()
            for (transaction in pending) {
                try {
                    api.sendTransaction(transaction)
                    transactionDao.deleteById(transaction.id)
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
    }

    companion object
}