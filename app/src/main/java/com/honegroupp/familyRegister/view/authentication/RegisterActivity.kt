package com.honegroupp.familyRegister.view.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * This class is just  for fake registration in order to create default categories for new user
 *
 *
 * */

class RegisterActivity : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        // Navigate to the category fragment
        register_button_register.setOnClickListener {
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Register", "Successfully created  user with uid: ${it.result?.user?.uid}")
                    Toast.makeText(this,"Register Successed",Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
                else{
                    //if no successful
                    Toast.makeText(this,"Register Failed",Toast.LENGTH_SHORT).show()
                }
            }

        }

        already_have_account_textview_register.setOnClickListener(){
            navigateToLogin()
        }

    }

    fun navigateToLogin(){
        val naviToLogin = Intent(this, LoginActivity::class.java)
        startActivity(naviToLogin)
    }



}