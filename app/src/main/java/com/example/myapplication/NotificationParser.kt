package com.example.myapplication

object NotificationParser {
    fun parse(content: String): TransactionDTO? {
        val regex = Regex("Shop:([A-Za-z0-9]+)@Amount:([A-Za-z]+)(\\d+\\.\\d{2})\\s+with\\s+([A-Za-z]+)")
        val match = regex.find(content) ?: return null
        val (shop, currency, amount, bank) = match.destructured
        return TransactionDTO(shop, currency,amount.toDouble(), bank)
    }
}