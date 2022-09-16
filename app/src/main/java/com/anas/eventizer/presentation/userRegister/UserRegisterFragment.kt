package com.anas.eventizer.presentation.userRegister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anas.eventizer.R
import com.anas.eventizer.data.remote.dto.UsersDto
import com.anas.eventizer.databinding.FragmentAddPersonalEventBinding
import com.anas.eventizer.databinding.FragmentUserRegisterBinding
import com.anas.eventizer.utils.EmailAndPasswordValidator
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "UserRegisterFragment"
@AndroidEntryPoint
class UserRegisterFragment : Fragment(),OnClickListener {

    private lateinit var emailTxtField:TextInputLayout
    private lateinit var pwdTxtField:TextInputLayout
    private lateinit var registerBtn:Button



    private val viewModel: UserRegisterViewModel by viewModels()
    private lateinit var _binding: FragmentUserRegisterBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRegisterBinding.inflate(inflater,container,false)

        initComponents()

        return binding.root
    }

    private fun initComponents(){

        emailTxtField = binding.emailTxtField
        pwdTxtField = binding.pwdTxtField
        registerBtn = binding.registerBtn
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        viewModel.authResultStateFlow.onEach { registerUiState ->
            when{
                registerUiState.errorMsg != null -> {
                    registerBtn.isEnabled = true
                    Log.d(TAG , "the error is ${registerUiState.errorMsg}")
                }
                registerUiState.isLoading -> {
                    Log.d(TAG , "Loading...")
                    registerBtn.isEnabled = false
                }
                registerUiState.registerResult -> {
                    registerBtn.isEnabled = true
                    Log.d(TAG , "the data is $registerUiState")
                }
            }

        }.launchIn(lifecycleScope)

    }

    private fun setListeners() {
        registerBtn.setOnClickListener(this)

    }

    private fun startRegisterByEmailAndPwdFlow(email:String,pwd:String,user:UsersDto){
        viewModel.registerUserByEmailAndPwd(email, pwd, user)
    }

    override fun onClick(v: View?) {
        when (v) {
            registerBtn -> {
                val email = emailTxtField.editText?.text.toString()
                val pwd = pwdTxtField.editText?.text.toString()
                if (!EmailAndPasswordValidator.checkEmailFormat(email)){
                    emailTxtField.error = resources.getString(R.string.inValid_email_format)
                }else{
                    emailTxtField.error = null
                }

                when (EmailAndPasswordValidator.checkPwdForm(pwd)){
                    EmailAndPasswordValidator.PasswordCases.NO_CAPITAL_CHAR ->{
                        pwdTxtField.error = resources.getString(R.string.no_cap_letter)
                    }
                    EmailAndPasswordValidator.PasswordCases.SHORT_PASSWORD ->{
                        pwdTxtField.error = resources.getString(R.string.short_pwd)
                    }
                    EmailAndPasswordValidator.PasswordCases.NO_SPECIAL_CHAR ->{
                        pwdTxtField.error = resources.getString(R.string.no_special_char)
                    }
                    else ->{
                        pwdTxtField.error = null
                    }
                }

                if (emailTxtField.error == null && pwdTxtField.error == null){
                    startRegisterByEmailAndPwdFlow(email,pwd, UsersDto("personal"))
                }

            }
        }

    }


}