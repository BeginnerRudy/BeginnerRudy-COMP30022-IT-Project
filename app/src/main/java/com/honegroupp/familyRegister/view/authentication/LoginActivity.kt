package com.honegroupp.familyRegister.view.authentication


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.utility.EmailPathSwitchUtil
import com.honegroupp.familyRegister.utility.IDoubleClickToExit

/**
 * This class is responsible for Login functionality.
 * use the AuthUI api
 * */
class LoginActivity : AppCompatActivity(),
                      IDoubleClickToExit {
    // This is the request code for sign in
    var RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
                .setTosAndPrivacyPolicyUrls(
                    getString(R.string.term_of_service_url),
                    getString(R.string.privacy_policy_url)).setIsSmartLockEnabled(
                    false)
                .build(),
            RC_SIGN_IN
        )
    }

    /**
     * Handle the result of authentication, receive information and upload
     * it to firebase*/
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed d
                val user = FirebaseAuth.getInstance().currentUser

                //set the userName and userContact
                var userContact = "Default"
                var userName = "Default"
                if (user != null) {
                    if (user.email != null) {
                        userContact = user.email.toString()
                        userName = user!!.displayName as String
                    }
                }

                val relativePath = EmailPathSwitchUtil.emailToPath(userContact)

                AuthenticationController
                    .storeUser(this, User(username = userName), relativePath)

            } else if (response == null) {
                // If the user press back button, exit the app
                finishAffinity()
            }
        }
    }

}
