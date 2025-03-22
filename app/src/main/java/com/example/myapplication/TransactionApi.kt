package com.example.myapplication
import retrofit2.http.Body
import retrofit2.http.POST
interface TransactionApi {
    @POST("/api/v1/transaction/new")
    suspend fun sendTransaction(@Body transaction: TransactionDTO)
}
