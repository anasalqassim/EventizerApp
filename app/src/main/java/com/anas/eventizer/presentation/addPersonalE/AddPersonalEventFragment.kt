package com.anas.eventizer.presentation.addPersonalE

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.data.remote.dto.PersonalEventDto
import com.anas.eventizer.databinding.FragmentAddPersonalEventBinding
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "AddPersonalEventFragment"
@AndroidEntryPoint
class AddPersonalEventFragment : Fragment() {

    private val viewModel: AddPersonalEventViewModel by viewModels()
    private lateinit var _binding:FragmentAddPersonalEventBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentAddPersonalEventBinding.inflate(layoutInflater, container, false)
        binding.apply {

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val personal = PersonalEventDto()
        addNewPersonalEvent(personal)

    }

    private fun addNewPersonalEvent(personalEventDto: PersonalEventDto){
        viewModel.addPersonalEvent(personalEventDto)


        viewModel.addingStateFlow.onEach { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d(TAG, "error ${result.massage}")
                    println()
                }
                is Resource.Loading -> {
                    println()
                }
                is Resource.Success -> {
                    Log.d(TAG, "done ${result.data}")
                    println()
                }

            }
        }.launchIn(lifecycleScope)

    }



}