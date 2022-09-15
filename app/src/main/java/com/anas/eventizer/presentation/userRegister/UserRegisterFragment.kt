package com.anas.eventizer.presentation.userRegister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.data.remote.dto.UsersDto
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "UserRegisterFragment"
@AndroidEntryPoint
class UserRegisterFragment : Fragment() {



    private val viewModel: UserRegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = UsersDto()
        startRegisterByEmailAndPwdFlow("anas1@gmail.com",
            "Qwe123qwe",
            user)



        viewModel.authResultStateFlow.onEach { registerUiState ->
            when{
                registerUiState.errorMsg != null -> {
                    Log.d(TAG , "the error is ${registerUiState.errorMsg}")
                }
                registerUiState.isLoading -> {
                    Log.d(TAG , "Loading...")
                }
                registerUiState.registerResult -> {
                    Log.d(TAG , "the data is $registerUiState")
                }
            }

        }.launchIn(lifecycleScope)

    }

    private fun startRegisterByEmailAndPwdFlow(email:String,pwd:String,user:UsersDto){
        viewModel.registerUserByEmailAndPwd(email, pwd, user)
    }



}