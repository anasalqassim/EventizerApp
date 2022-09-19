package com.anas.eventizer.presentation.addPublicE

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.data.remote.dto.PublicEventDto
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "AddPublicEventFragment"
@AndroidEntryPoint
class AddPublicEventFragment : Fragment() {


    private val viewModel: AddPublicEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_public_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val links = listOf("https://t4.ftcdn.net/jpg/02/89/38/41/360_F_289384108_fyDlnvX03W7WDaFPfQ6aPY8KgLTY93Yf.jpg","https://images.unsplash.com/photo-1503249023995-51b0f3778ccf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGQlMjBwaG90b3N8ZW58MHx8MHx8&w=1000&q=80")
        for (x in 0..100){
            val publicEventDto = PublicEventDto(eventPicsUrls = links)
            viewModel.addPublicEvent(publicEventDto)
        }
        viewModel.addingStateFlow.onEach {
            when(it){
                is Resource.Error ->{
                    Log.d(TAG, "onViewCreated: ${it.massage}")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "loading: ")
                }
                else -> {
                    Log.d(TAG, "onViewCreated: dsfsdjfns")
                }
            }
        }.launchIn(lifecycleScope)
    }


}