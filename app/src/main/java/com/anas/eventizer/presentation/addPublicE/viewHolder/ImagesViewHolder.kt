package com.anas.eventizer.presentation.addPublicE.viewHolder

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.anas.eventizer.databinding.EventImageRowItemBinding

class ImagesViewHolder(
    val binding:EventImageRowItemBinding
) : ViewHolder(binding.root) {

    fun bind(imageUri:Uri){
        binding.img.setImageURI(imageUri)
    }
}