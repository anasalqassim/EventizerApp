package com.anas.eventizer.presentation.addPublicE.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.anas.eventizer.databinding.EventImageRowItemBinding
import com.anas.eventizer.presentation.addPublicE.viewHolder.ImagesViewHolder
import com.anas.eventizer.utils.observer

@SuppressLint("NotifyDataSetChanged")
class ImagesAdapter : Adapter<ImagesViewHolder>() {

    var imageURIs:List<Uri> by observer(listOf()){
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = EventImageRowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(imageUri = imageURIs[position])
    }

    override fun getItemCount(): Int = imageURIs.size
}