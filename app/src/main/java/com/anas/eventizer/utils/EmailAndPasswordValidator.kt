package com.anas.eventizer.utils

import android.util.Patterns
import java.util.regex.Pattern

object EmailAndPasswordValidator {
    //TODO COMPLETE IT
    private val emailPattern: Pattern = Patterns.EMAIL_ADDRESS
    private const val PASSWORD_MIN_LENGTH = 8

    enum class PasswordCases{
        SHORT_PASSWORD,
        NO_SPECIAL_CHAR,
        NO_CAPITAL_CHAR,
        VALID_PWD
    }

    fun checkEmailFormat(email:String):Boolean{

        return email.contains(emailPattern.toRegex())
    }

     fun checkPwdForm(pwd: String): PasswordCases {

        return if (pwd.length < PASSWORD_MIN_LENGTH){
            PasswordCases.SHORT_PASSWORD
        }else if (!pwd.contains(Regex("[`!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?~]"))){
            PasswordCases.NO_SPECIAL_CHAR
        }else if (!pwd.contains(Regex("[A-Z]"))){
            PasswordCases.NO_CAPITAL_CHAR
        }else{
            PasswordCases.VALID_PWD
        }
    }

}