package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.utility.EmailPathSwitch
import kotlinx.android.synthetic.main.activity_account.*

class HelpFeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpfeedback)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_helpfeedback_activity)
        toolbar.title = getString(R.string.help_and_feedback)
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        toolbar.setNavigationOnClickListener{
            finish()
        }



    }
}
