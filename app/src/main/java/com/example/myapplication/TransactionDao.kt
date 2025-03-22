package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionDTO)

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionDTO>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Int)
}
