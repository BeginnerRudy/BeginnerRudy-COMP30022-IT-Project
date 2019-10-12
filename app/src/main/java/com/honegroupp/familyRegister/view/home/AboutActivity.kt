package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import kotlinx.android.synthetic.main.activity_about.*
import android.content.Intent
import android.net.Uri


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

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
