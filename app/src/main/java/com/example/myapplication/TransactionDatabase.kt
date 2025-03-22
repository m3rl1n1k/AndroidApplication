package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransactionDTO::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}