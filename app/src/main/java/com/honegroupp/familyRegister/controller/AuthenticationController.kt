package com.honegroupp.familyRegister.controller

import com.honegroupp.familyRegister.model.Account
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.authentication.LoginActivity

class AuthenticationController {
    companion object {
        const val FAILURE = "FAILURE"

        fun login(email: String, password: String, loginActivity: LoginActivity) {
            val account = Account(email, password)
            account.login(loginActivity)
        }
    }
}