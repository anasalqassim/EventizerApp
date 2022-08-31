package com.anas.eventizer.presentation.addPublicE

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anas.eventizer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPublicEventFragment : Fragment() {


    private val viewModel: AddPublicEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_public_event, container, false)
    }


}