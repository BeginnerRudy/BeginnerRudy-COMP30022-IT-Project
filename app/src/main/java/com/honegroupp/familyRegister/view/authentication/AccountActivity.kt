package com.honegroupp.familyRegister.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

import com.honegroupp.familyRegister.utility.EmailPathSwitch
import kotlinx.android.synthetic.main.activity_account.*

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.honegroupp.familyRegister.R


class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Configure the toolbar setting
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.account_capital)
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        setSupportActionBar(toolbar)

        //get the uid from intent
        val uid = intent.getStringExtra("UserID")

        changePassword.setOnClickListener {

            Toast.makeText(this,
                getString(R.string.reset_email_message),
                Toast.LENGTH_LONG).show()

            var mAuth: FirebaseAuth? = null
            mAuth = FirebaseAuth.getInstance()
            mAuth!!.sendPasswordResetEmail(EmailPathSwitch.pathToEmail(uid))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        Toast.makeText(this, getString(R.string.password_rest_success),Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "ERROR",Toast.LENGTH_LONG).show()
                    }
                }
        }
        reAuthTesting.setOnClickListener{


            val user = FirebaseAuth.getInstance().currentUser

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.

            val password = enterPassword.text.toString()

            val credential: AuthCredential = EmailAuthProvider
                .getCredential(EmailPathSwitch.pathToEmail(uid), password)



// Prompt the user to re-provide their sign-in credentials
//            user?.reauthenticate(credential)
//                ?.addOnCompleteListener {
//
//                }

            user?.reauthenticate(credential)
                ?.addOnCompleteListener(OnCompleteListener<Void> { task ->
//                    DialogUtils.dismissProgressDialog()
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
                    }
                })
        }


    }
}
