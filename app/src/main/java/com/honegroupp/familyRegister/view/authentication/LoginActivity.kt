package com.honegroupp.familyRegister.view.authentication


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.google.firebase.database.FirebaseDatabase
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.myapplication.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        // Navigate to the category fragment
        login_button_login.setOnClickListener {

            val email = UserName_edit_login.text.toString()
            val password = Password_edit_login.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or Password cannot be empty!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                login(email, password)
            }
        }

        // Navigate to the register fragment
        register_button_login.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

    }

    fun login(email: String, password: String) {
        val feedback = AuthenticationController.login(email, password)

        if (feedback == AuthenticationController.FAILURE) {
            Toast.makeText(this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show()
        } else {
            val uid = feedback
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
