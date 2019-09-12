package com.honegroupp.familyRegister.view.authentication


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.AuthenticationController
import com.honegroupp.myapplication.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        // Navigate to the category fragment
        login_button_login.setOnClickListener {
            login()
        }

        // Navigate to the register fragment
        register_button_login.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

    }

    private fun login() {

        val email = UserName_edit_login.text.toString()
        val password = Password_edit_login.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or Password cannot be empty!", Toast.LENGTH_SHORT)
                .show()
        } else {
            val feedback = AuthenticationController.login(email, password, this)
        }
    }

    fun invalidAccountAndPassword() {
        Toast.makeText(this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show()
    }

    fun goToLogin(uid: String) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}


