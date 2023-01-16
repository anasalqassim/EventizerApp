package com.anas.eventizer.presentation.eventDetails.rvAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.anas.eventizer.databinding.EventImageRowItemBinding
import com.anas.eventizer.presentation.eventDetails.rvViewHolder.EventImagesViewHolder

class EventImagesAdapter(private val eventImagesUrl:List<String>) : Adapter<EventImagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventImagesViewHolder {
        val binding = EventImageRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventImagesViewHolder, position: Int) {
       holder.bind(eventImagesUrl[position])
    }

    override fun getItemCount(): Int = eventImagesUrl.size
}