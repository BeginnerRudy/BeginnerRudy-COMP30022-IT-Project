package com.honegroupp.familyRegister.view.authentication


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.honegroupp.familyRegister.IDoubleClickToExit
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.home.HomeActivity

/**
 * This class is responsible for Login functionality.
 *
 * */

class LoginActivity : AppCompatActivity(), IDoubleClickToExit {
    // This is the request code for sign in
    var RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://en.wikipedia.org/wiki/SLD_resolution",
                    "https://example.com/privacy.html"
                )
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed d
                val user = FirebaseAuth.getInstance().currentUser

                // TODO Async task here, for better performance
                AuthenticationController.storeUser(User(user!!.displayName as String), user!!.uid)

//                pass user id to next activity
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("UserID", user.uid)

                startActivity(intent)
            } else if (response == null) {
                // If the user press back button, exit the app
                finishAffinity()
            }
        }
    }
}
