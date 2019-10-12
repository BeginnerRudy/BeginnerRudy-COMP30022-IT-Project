package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ViewFamilyController

class ViewFamilyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_family)
        // get uid
        val uid = intent.getStringExtra("UserID")

        // display all family member
        ViewFamilyController.showAllMembers(uid, this)

        // change family password

        // change family name
    }

}

