package com.anas.eventizer.presention.addPersonalE

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.anas.eventizer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPersonalEventFragment : Fragment() {

    private val viewModel: AddPersonalEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_personal_event, container, false)
    }



}