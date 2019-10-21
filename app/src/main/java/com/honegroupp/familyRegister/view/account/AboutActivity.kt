package com.honegroupp.familyRegister.view.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import kotlinx.android.synthetic.main.activity_about.*
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ComponentActivity.ExtraData




class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_about_activity)
        toolbar.title = getString(R.string.about)
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        toolbar.setNavigationOnClickListener{
            finish()
        }

        //click button to view the term of service
        view_term_of_service.setOnClickListener{
            val uri = Uri.parse(getString(R.string.term_of_service_url))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

        }

        //click button to view the privacy policy
        view_privacy_policy.setOnClickListener{
            val uri = Uri.parse(getString(R.string.privacy_policy_url))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


    }
}
