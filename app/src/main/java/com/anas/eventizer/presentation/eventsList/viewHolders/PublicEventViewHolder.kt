package com.anas.eventizer.presentation.eventsList.viewHolders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.SnapHelper
import coil.load
import com.anas.eventizer.databinding.EventImageRowItemBinding
import com.anas.eventizer.databinding.PublicEventRowItemBinding
import com.anas.eventizer.presentation.eventsList.PublicEventItemUiState
import com.anas.eventizer.presentation.eventsList.adapters.ImagesAdapter
import javax.inject.Inject

class PublicEventViewHolder @Inject constructor (
    private val binding:PublicEventRowItemBinding
) : ViewHolder(binding.root) {

    fun bind(state: PublicEventItemUiState){

        val layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.HORIZONTAL,
            false)
        binding.imagesRv.layoutManager = layoutManager
        binding.imagesRv.setHasFixedSize(false)
        val snapHelper = PagerSnapHelper()
        if (binding.imagesRv.onFlingListener == null){
            snapHelper.attachToRecyclerView(binding.imagesRv)
        }
        binding.imagesRv.adapter = ImagesAdapter(state.eventPicsUrl)



        binding.titleTv.text = state.title


    }

}

internal class ImagesViewHolder(private val binding:EventImageRowItemBinding)
    :ViewHolder(binding.root){
        fun bind(imageUrl:String){
            binding.img.load(imageUrl)
        }
    }

