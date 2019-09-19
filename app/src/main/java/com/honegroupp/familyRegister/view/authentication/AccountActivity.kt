package com.honegroupp.familyRegister.view.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.honegroupp.familyRegister.R

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Configure the toolbar setting
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "ACCOUNT"
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        setSupportActionBar(toolbar)


    }
}
