package com.anas.eventizer.presentation.eventDetails.rvViewHolder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.anas.eventizer.databinding.SupportRowItemBinding
import com.anas.eventizer.domain.models.Support

class SupportsViewHolder(val binding: SupportRowItemBinding) : ViewHolder(binding.root) {



    fun bind(support: Support){
        binding.supportImage.load(support.supportPics.first())
        binding.supportTitleTv.text = support.supportName
    }
}