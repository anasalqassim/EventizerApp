package com.anas.eventizer.presentation.eventDetails.rvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.anas.eventizer.databinding.SupportRowItemBinding
import com.anas.eventizer.domain.models.Support
import com.anas.eventizer.presentation.eventDetails.rvViewHolder.SupportsViewHolder

class SupportsAdapter(private val supports: List<Support>) : Adapter<SupportsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportsViewHolder {
        val binding = SupportRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SupportsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SupportsViewHolder, position: Int) {
        val support = supports[position]
        holder.bind(support)
    }

    override fun getItemCount(): Int  = supports.size
}