package com.test.atlant.data.model

import com.test.atlant.ui.util.SortedListItem
import java.util.*

data class Transaction(val hash: String,
                       val input: Double,
                       val output: Double,
                       val transactionDate: Date,
                       val sortDate: Long)
    : SortedListItem<Transaction>{

    override fun areContentsTheSame(another: Transaction): Boolean = input == another.input
            && output == another.output
            && transactionDate == another.transactionDate

    override fun areItemsTheSame(another: Transaction): Boolean = hash == another.hash

    override fun compareTo(another: Transaction): Int = another.sortDate.compareTo(sortDate)
}