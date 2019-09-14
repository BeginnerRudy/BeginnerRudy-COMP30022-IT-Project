package com.honegroupp.familyRegister.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.myapplication.HomeActivity

class FirebaseAuthenticationManager {
    companion object {
        fun login(email: String, password: String): String {
            var result = "HASOIASHDUASHDHSIAUDHSHDDHASUDHUASHD"



//            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).then(fun onSuccess(){})
//                .addOnCompleteListener {
//
//                    result = if (it.isSuccessful) {
//                        FirebaseAuth.getInstance().currentUser?.uid.toString()
//                    } else {
//                        AuthenticationController.FAILURE
//                    }
//                }
//
//            Log.d("Print out", result)
            return result
        }
    }
}