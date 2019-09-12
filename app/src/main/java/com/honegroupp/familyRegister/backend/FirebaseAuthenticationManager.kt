package com.honegroupp.familyRegister.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.familyRegister.view.authentication.LoginActivity
import com.honegroupp.myapplication.HomeActivity

class FirebaseAuthenticationManager {
    companion object {
        fun login(email: String, password: String, loginActivity: LoginActivity) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        loginActivity.goToLogin(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    } else {
                        loginActivity.invalidAccountAndPassword()
                    }
                }
        }
    }
}