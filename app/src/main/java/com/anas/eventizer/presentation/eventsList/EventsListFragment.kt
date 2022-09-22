package com.anas.eventizer.presentation.eventsList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anas.eventizer.R
import com.anas.eventizer.databinding.FragmentEventsListBinding
import com.anas.eventizer.presentation.eventsList.adapters.PublicEventsRvAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "EventsListFragment"
@AndroidEntryPoint
class EventsListFragment : Fragment() {


    private val viewModel: EventsListViewModel by viewModels()

    private lateinit var _binding:FragmentEventsListBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tomorrow = Calendar.getInstance()
        tomorrow.set(Calendar.DAY_OF_MONTH,20)
        Log.d(TAG, "onCreate: ${tomorrow[Calendar.DAY_OF_MONTH]}")
        viewModel.getPublicEvents(tomorrow)
    }

    private fun init() {

        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider)
        binding.publicEventsRv.apply {
            this.setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
            if (dividerDrawable != null) {
                dividerItemDecoration.setDrawable(dividerDrawable)
            }
            this.addItemDecoration(dividerItemDecoration)
            this.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setListeners(){
        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            when{
                checkedIds[0] == binding.todayChip.id ->{
                    binding.publicEventsRv.adapter = PublicEventsRvAdapter(emptyList())

                    val todayDate = Calendar.getInstance()

                    viewModel.getPublicEvents(todayDate)
                }
                checkedIds[0] == binding.tomorrowChip.id ->{
                    binding.publicEventsRv.adapter = PublicEventsRvAdapter(emptyList())

                    val tomorrow = Calendar.getInstance()
                    tomorrow.roll(Calendar.DAY_OF_MONTH,1)

                    viewModel.getPublicEvents(tomorrow)
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsListBinding.inflate(inflater, container, false)
        init()
        setListeners()
      return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            viewModel.publicEventsStateFlow.collect { result ->
                when{
                    result.errorMsg.isNotEmpty()-> {
                        Log.d(TAG, "  ${result.errorMsg}")
                    }
                    result.isLoading -> {
                        Log.d(TAG, "Loading.. the page gonna load ")
                    }
                    result.events.isNotEmpty() -> {
                        Log.d(TAG, "onViewCreated: ${result.events}")

                        binding.publicEventsRv.adapter =
                            PublicEventsRvAdapter(result.events)
                    }
                }
            }
        }

    }



}