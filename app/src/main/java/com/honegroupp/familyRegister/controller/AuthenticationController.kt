package com.honegroupp.familyRegister.controller

import com.honegroupp.familyRegister.model.Account
import com.honegroupp.familyRegister.model.User

class AuthenticationController{
    companion object{
        const val FAILURE = "FAILURE"

        fun login(email: String, password: String): String{

            val user = User(Account(email, password))
            return user.account.login()
        }
    }
}