package com.anas.eventizer.presentation.registerEventSupporter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anas.eventizer.databinding.FragmentRegisterEventSupporterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterEventSupporterFragment : Fragment() {

    private val viewModel: RegisterEventSupporterViewModel by viewModels()
    private lateinit var _binding: FragmentRegisterEventSupporterBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterEventSupporterBinding.inflate(inflater,container,false)



        return binding.root
    }



}