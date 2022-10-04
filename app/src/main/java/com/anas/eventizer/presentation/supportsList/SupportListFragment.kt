package com.anas.eventizer.presentation.supportsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.anas.eventizer.databinding.FragmentSupportListBinding
import com.anas.eventizer.domain.models.Support
import com.anas.eventizer.presentation.supportsList.adapter.SupportsListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportListFragment : BottomSheetDialogFragment() {



    private val viewModel: SupportListViewModel by viewModels()

    private lateinit var _binding:FragmentSupportListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportListBinding.inflate(
            inflater,
            container,
            false
        )


        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supports = mutableListOf<Support>()
        val adapter = SupportsListAdapter()

        for (x in 0..20){
            val support = Support(supportName = "ahmed", supportCategory = "mgahoe")
            supports.add(support)
        }
        adapter.supports = supports

        binding.supportsRv.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(),2)
            setHasFixedSize(true)
        }


    }


}