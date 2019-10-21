package com.honegroupp.familyRegister.view.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.honegroupp.familyRegister.R


/**
 * This class is for "Help and feedback" page
 * */
class HelpFeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpfeedback)

        //set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_helpfeedback_activity)
        toolbar.title = getString(R.string.help_and_feedback)
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }


    }
}
