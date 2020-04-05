package com.orioltobar.androidklean.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH>() {

    private var items: List<T> = emptyList()

    private var onItemClicked: (T) -> Unit = {}

    fun setOnClickListener(listener: (T) -> Unit) {
        onItemClicked = listener
    }

    fun updateItems(newItems: List<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.update(item)
        holder.itemView.setOnClickListener { onItemClicked.invoke(item) }
    }
}