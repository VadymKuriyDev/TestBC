package com.test.atlant.ui.util

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList

class SimpleSortedList<T : SortedListItem<T>> constructor(
    klass: Class<T>,
    adapter: RecyclerView.Adapter<*>
) : SortedList<T>(klass, object : Callback<T>() {

    override fun compare(o1: T, o2: T): Int {
        return o1.compareTo(o2)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int) {
        adapter.notifyItemRangeChanged(position, count)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override  fun areItemsTheSame(item1: T, item2: T): Boolean {
        return item1.areItemsTheSame(item2)
    }
})
