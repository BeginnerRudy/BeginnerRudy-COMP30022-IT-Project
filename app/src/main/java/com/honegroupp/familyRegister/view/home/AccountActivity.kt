package com.honegroupp.familyRegister.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

import com.honegroupp.familyRegister.utility.EmailPathSwitch
import kotlinx.android.synthetic.main.activity_account.*

import com.honegroupp.familyRegister.R


class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Configure the toolbar setting
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.account_capital)
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        toolbar.setNavigationOnClickListener{
            finish()
        }


        //get the uid from intent
        val uid = intent.getStringExtra("UserID")


        //Handling change user password
        changePassword.setOnClickListener {
            Toast.makeText(this,
                getString(R.string.reset_email_message),
                Toast.LENGTH_LONG).show()

            var mAuth: FirebaseAuth? = null
            mAuth = FirebaseAuth.getInstance()
            mAuth!!.sendPasswordResetEmail(EmailPathSwitch.pathToEmail(uid))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //nothing
                    } else {
                        Toast.makeText(this, "ERROR",Toast.LENGTH_LONG).show()
                    }
                }
        }

        name.setOnClickListener{
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("UserID", uid)
            startActivity(intent)
        }



    }
}
