package com.anas.eventizer.presentation.eventDetails.rvViewHolder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.anas.eventizer.databinding.EventImageRowItemBinding

class EventImagesViewHolder(val binding:EventImageRowItemBinding) : ViewHolder(binding.root) {


    fun bind(imageUrl:String){
        binding.img.load(imageUrl)
    }
}