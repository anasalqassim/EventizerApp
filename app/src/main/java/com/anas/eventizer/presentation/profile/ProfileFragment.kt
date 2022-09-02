package com.anas.eventizer.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "ProfileFragment"
@AndroidEntryPoint
class ProfileFragment : Fragment() {



    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun getPersonalEvents(userId:String,refresh:Boolean){

        viewModel.initPersonalEvent(userId,refresh)

        viewModel.personalEventsLiveData.onEach { result ->
            when(result){
                is Resource.Error ->{
                    Log.d(TAG , "error is ${result.massage}")
                }
                is Resource.Loading ->{

                }
                is Resource.Success ->{
                    Log.d(TAG , "data is ${result.data}")
                }

            }
        }.launchIn(lifecycleScope)

    }

}