package com.example.familyRegister.core

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import com.example.familyRegister.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_register.view.email_edittext_register
import kotlinx.android.synthetic.main.fragment_register.view.password_edittext_register
import android.widget.Toast.makeText as makeText1

/**
 * This class is just  for fake registration in order to create default categories for new user
 *
 *
 * */

class RegisterFragment : Fragment() {

    var database = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Navigate to the category fragment
        view.register_button_register.setOnClickListener {
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    (activity as NavigationHost).navigateTo(RegisterFragment(), false)
                    //return@addOnCompleteListener
                }

                //if successful
                Log.d("Register", "Successfully created  user with uid: ${it.result?.user?.uid}")
                (activity as NavigationHost).navigateTo(CategoryFragment(), false)
            }

        }

        view.already_have_account_textview_register.setOnClickListener(){
            (activity as NavigationHost).navigateTo(LoginFragment(), false)
        }

        return view
    }



}