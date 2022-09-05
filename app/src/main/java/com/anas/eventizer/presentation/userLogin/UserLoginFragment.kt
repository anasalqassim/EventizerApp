package com.anas.eventizer.presentation.userLogin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.utils.EmailAndPasswordValidator
import com.anas.eventizer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val TAG = "UserLoginFragment"
@AndroidEntryPoint
class UserLoginFragment : Fragment() {


    private val viewModel: UserLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO handle the different of wrong email and pwd in the viewModel
        startLoginFlow("anas@anas.com" , "Qwe123qwe")



    }

    private fun startLoginFlow(email:String, pwd:String){
        viewModel.loginUserByEmailAndPwd(email, pwd)
        lifecycleScope.launchWhenCreated {
            viewModel.authResultStateFlow.collect{
                    authResult ->
                when(authResult){
                    is Resource.Error -> {
                        Log.d(TAG , "the error is ${authResult.massage}")
                    }
                    is Resource.Loading -> {
                        Log.d(TAG , "Loading")
                    }
                    is Resource.Success -> {
                        Log.d(TAG , "hello ${authResult.data!!.user}")
                    }
                }
            }
        }

    }


}