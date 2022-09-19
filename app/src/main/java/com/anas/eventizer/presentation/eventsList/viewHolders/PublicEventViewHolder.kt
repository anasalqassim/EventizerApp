package com.anas.eventizer.presentation.eventsList.viewHolders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.anas.eventizer.databinding.PublicEventRowItemBinding
import com.anas.eventizer.presentation.eventsList.PublicEventItemUiState
import javax.inject.Inject

class PublicEventViewHolder @Inject constructor (
    private val binding:PublicEventRowItemBinding
) : ViewHolder(binding.root) {

    fun bind(state: PublicEventItemUiState){
        binding.imageView.load(state.eventPicsUrl[0])
    }

}