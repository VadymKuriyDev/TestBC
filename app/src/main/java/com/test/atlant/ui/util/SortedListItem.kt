package com.test.atlant.ui.util

interface SortedListItem<T> : Comparable<T> {

    fun areContentsTheSame(another: T): Boolean

    fun areItemsTheSame(another: T): Boolean

    override fun compareTo(another: T): Int
}
