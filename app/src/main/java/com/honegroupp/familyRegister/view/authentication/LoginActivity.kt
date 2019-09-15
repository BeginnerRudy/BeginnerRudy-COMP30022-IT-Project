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
import com.honegroupp.myapplication.HomeActivity
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), IDoubleClickToExit {
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
                Log.d("UID", user!!.uid)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                // ...
            } else if (response == null) {
                finishAffinity()
            }
        }
    }
}
