package com.anas.eventizer.presentation.eventsList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.ViewPager2
import com.anas.eventizer.databinding.EventImageRowItemBinding
import com.anas.eventizer.databinding.PublicEventRowItemBinding
import com.anas.eventizer.presentation.eventsList.PublicEventItemUiState
import com.anas.eventizer.presentation.eventsList.PublicEventsUiState
import com.anas.eventizer.presentation.eventsList.viewHolders.ImagesViewHolder
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

internal class ImagesAdapter(private val imageUrls:List<String>): Adapter<ImagesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = EventImageRowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val imgUrl = imageUrls[position]
        holder.bind(imgUrl)
    }

    override fun getItemCount(): Int = imageUrls.size

}

