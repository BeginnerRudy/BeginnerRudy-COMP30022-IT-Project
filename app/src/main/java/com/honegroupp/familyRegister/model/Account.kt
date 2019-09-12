package com.honegroupp.familyRegister.model

import com.google.firebase.database.PropertyName
import com.honegroupp.familyRegister.backend.FirebaseAuthenticationManager
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.familyRegister.view.authentication.LoginActivity

/**
 * This class is responsible for storing data and business logic for Account
 *
 * @param email the email user typed in
 * @param password the password user typed in
 *
 *
 * @author Renjie Meng
 * */

data class Account(
    @set:PropertyName("email")
    @get:PropertyName("email")
    var email: String,
    @set:PropertyName("password")
    @get:PropertyName("password")
    var password: String
) {
    /*This constructor has no parameter, which is used to create Account while retrieve data from database*/
    constructor() : this("", "")

    fun login(loginActivity: LoginActivity) {
        FirebaseAuthenticationManager.login(email, password, loginActivity)
    }

}