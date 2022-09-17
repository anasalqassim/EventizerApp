package com.anas.eventizer.presentation.userLogin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.databinding.FragmentUserLoginBinding
import com.anas.eventizer.utils.EmailAndPasswordValidator
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "UserLoginFragment"
@AndroidEntryPoint
class UserLoginFragment : Fragment(),OnClickListener {

    private lateinit var _binding:FragmentUserLoginBinding
    private val binding get() = _binding

    private lateinit var emailTxtField:TextInputLayout
    private lateinit var pwdTxtField:TextInputLayout


    private val viewModel: UserLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)

        setListeners()
        initComponents()

        return binding.root
    }

    private fun initComponents() {
        emailTxtField = binding.emailTxtField
        pwdTxtField = binding.pwdTxtField

    }

    private fun setListeners() {
        binding.loginBtn.setOnClickListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginUiStateFlow.onEach{
                loginUiState ->
            when{
                loginUiState.errorMsg != null -> {
                    Log.d(TAG , "the error is ${loginUiState.errorMsg}")
                }
                loginUiState.isLoading -> {
                    Log.d(TAG , "Loading...")
                }
                loginUiState.loginResult -> {
                    Log.d(TAG , "hello $loginUiState")
                }
            }
        }.launchIn(lifecycleScope)




    }

    private fun startLoginFlow(email:String, pwd:String){
        viewModel.loginUserByEmailAndPwd(email, pwd)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.loginBtn -> {
                val email = emailTxtField.editText?.text.toString()
                val pwd = pwdTxtField.editText?.text.toString()
                if (!EmailAndPasswordValidator.checkEmailFormat(email)){
                    emailTxtField.error = resources.getString(R.string.inValid_email_format)
                }else{
                    emailTxtField.error = null
                }
                if (emailTxtField.error == null){
                    startLoginFlow(email, pwd)
                }

            }
        }
    }


}