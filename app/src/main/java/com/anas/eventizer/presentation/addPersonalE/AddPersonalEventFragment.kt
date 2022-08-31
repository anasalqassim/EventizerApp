package com.anas.eventizer.presentation.addPersonalE

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.anas.eventizer.R
import com.anas.eventizer.databinding.FragmentAddPersonalEventBinding
import dagger.hilt.android.AndroidEntryPoint

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

    private fun addNewPersonalEvent(eName:String){



    }



}