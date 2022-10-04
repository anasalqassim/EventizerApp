package com.anas.eventizer.presentation.supportsList.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.anas.eventizer.databinding.SupportRowItemBinding
import com.anas.eventizer.domain.models.Support
import com.anas.eventizer.presentation.supportsList.viewHolder.SupportViewHolder
import com.anas.eventizer.utils.observer

@SuppressLint("NotifyDataSetChanged")
class SupportsListAdapter : Adapter<SupportViewHolder>() {

    var supports:List<Support> by observer(listOf()){
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder {
        val binding = SupportRowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return SupportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        val support = supports[position]
        holder.bind(support)

    }

    override fun getItemCount(): Int = supports.size
}