package com.anas.eventizer.presentation.eventsList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.anas.eventizer.databinding.PublicEventRowItemBinding
import com.anas.eventizer.presentation.eventsList.PublicEventItemUiState
import com.anas.eventizer.presentation.eventsList.PublicEventsUiState
import com.anas.eventizer.presentation.eventsList.viewHolders.PublicEventViewHolder
import javax.inject.Inject

class PublicEventsRvAdapter constructor(
    private val publicEventItemUiStates: List<PublicEventItemUiState>
) : Adapter<PublicEventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicEventViewHolder {
        val binding = PublicEventRowItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return PublicEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PublicEventViewHolder, position: Int) {
        val state = publicEventItemUiStates[position]
        holder.bind(state)
    }

    override fun getItemCount(): Int = publicEventItemUiStates.size
}