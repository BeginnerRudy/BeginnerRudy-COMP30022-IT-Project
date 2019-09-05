package com.example.familyRegister.core

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.familyRegister.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.Password_edit_login
import kotlinx.android.synthetic.main.fragment_login.view.UserName_edit_login
import kotlinx.android.synthetic.main.fragment_register.view.*

class LoginFragment: Fragment() {

    companion object {
         val fakeInitialValue = ""
        lateinit var uid: String
    }

    var database = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Navigate to the category fragment
        view.login_button_login.setOnClickListener {

            val email = UserName_edit_login.text.toString()
            val password = Password_edit_login.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Log.d("login","text is empty")
                (activity as NavigationHost).navigateTo(LoginFragment(), false )
            }else{
                Log.d("login","not empty")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(){
                    if (it.isSuccessful){
                        uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        (activity as NavigationHost).navigateTo(CategoryFragment(), false )
                    }
                    else{
                        (activity as NavigationHost).navigateTo(LoginFragment(), false )
                    }
                }
            }
        }

        // Navigate to the register fragment
        view.register_button_login.setOnClickListener {
            (activity as NavigationHost).navigateTo(RegisterFragment(), false)
        }

        return view
    }
}