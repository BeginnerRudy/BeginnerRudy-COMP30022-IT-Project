package com.honegroupp.familyRegister.backend

import com.google.firebase.auth.FirebaseAuth
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.myapplication.HomeActivity

class FirebaseAuthenticationManager {
    companion object{
        fun login(email:String, password: String): String{
            var result = ""
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() {

                    if (it.isSuccessful) {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        result = uid
                    } else {
                        result = AuthenticationController.FAILURE
                    }
                }

            return result
        }
    }
}