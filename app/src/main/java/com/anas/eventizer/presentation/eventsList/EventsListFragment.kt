package com.anas.eventizer.presentation.eventsList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "EventsListFragment"
@AndroidEntryPoint
class EventsListFragment : Fragment() {


    private val viewModel: EventsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPublicEvents()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.publicEventsStateFlow.onEach { result ->
            when(result){
                is Resource.Error -> {
                    Log.d(TAG, "there was an error : ${result.massage}")
                }
                is Resource.Loading -> {
                    Log.d(TAG, "Loading.. ")
                }
                is Resource.Success -> {
                    val states = result.data

                }
            }


        }.launchIn(lifecycleScope)
    }



}