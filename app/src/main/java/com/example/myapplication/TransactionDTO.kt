package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionDTO(
    val shop: String,
    val currency: String,
    val amount: Double,
    val bank: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
