package com.example.myapplication


import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): TransactionDatabase =
        Room.databaseBuilder(context, TransactionDatabase::class.java, "transactions.db").build()

    @Provides
    fun provideDao(db: TransactionDatabase) = db.transactionDao()

    @Provides
    @Singleton
    fun provideApi(): TransactionApi =
        Retrofit.Builder().baseUrl("http://localhost:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransactionApi::class.java)
}