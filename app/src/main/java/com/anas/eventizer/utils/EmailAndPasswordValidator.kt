package com.anas.eventizer.utils

import com.anas.eventizer.domain.useCase.LoginUserByEmailAndPwdUC

object EmailAndPasswordValidator {
    //TODO COMPLETE IT
    const val EMAIL_PATTERN = ""
    const val PASSWORD_MIN_LENGTH = 8

    enum class PasswordCases{
        SHORT_PASSWORD,NO_SPECIAL_CHAR,NO_CAPITAL_CHAR,VALID_PWD
    }

    fun checkEmailFormat(email:String):Boolean{
        return email.contains(Regex(EMAIL_PATTERN))
    }

    private fun checkPwdForm(pwd: String): PasswordCases {

        return if (pwd.length < 4){
            PasswordCases.SHORT_PASSWORD
        }else if (!pwd.contains(Regex("/[`!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]/"))){
            PasswordCases.NO_SPECIAL_CHAR
        }else if (pwd.contains(Regex("[A-Z]"))){
            PasswordCases.NO_CAPITAL_CHAR
        }else{
            PasswordCases.VALID_PWD
        }
    }

}