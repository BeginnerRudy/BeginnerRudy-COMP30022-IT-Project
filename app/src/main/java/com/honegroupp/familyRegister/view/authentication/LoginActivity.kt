package com.honegroupp.familyRegister.view.authentication


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.honegroupp.myapplication.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        val fakeInitialValue = ""
        lateinit var uid: String
    }

    var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        // Navigate to the category fragment
        login_button_login.setOnClickListener {

            val email = UserName_edit_login.text.toString()
            val password = Password_edit_login.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Log.d("login", "text is empty")

            } else {
                Log.d("login", "not empty")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() {
                        if (it.isSuccessful) {
                            uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Password or Username is not correct", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Navigate to the register fragment
        register_button_login.setOnClickListener {
            val goToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(goToRegister)
        }

    }
}
