package com.test.atlant.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.test.atlant.R
import com.test.atlant.data.model.Transaction
import com.test.atlant.databinding.TransactionItemBinding
import com.test.atlant.ui.util.SimpleSortedList
import com.test.atlant.ui.util.toBTCString
import com.test.atlant.ui.util.toSimpleString
import kotlinx.android.synthetic.main.transaction_item.view.*

class TransactionAdapter:
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val list: SortedList<Transaction>

    init {
        list = SimpleSortedList(Transaction::class.java, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransactionItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        holder.binding.hash.text = item.hash
        holder.binding.input.text = holder.binding.root.context.resources
            .getString(R.string.transaction_value, item.input.toBTCString())
        holder.binding.output.text = holder.binding.root.context.resources
            .getString(R.string.transaction_value, item.output.toBTCString())
        holder.binding.date.text = item.transactionDate.toSimpleString()
    }

    override fun getItemCount(): Int {
        return list.size()
    }

    fun updateData(transactions: List<Transaction>) {
        list.beginBatchedUpdates()
        if (transactions.isEmpty())list.clear()
        list.addAll(transactions)
        list.endBatchedUpdates()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var binding: TransactionItemBinding = DataBindingUtil.bind(itemView)!!
    }
}
