package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ViewFamilyController

class ViewFamilyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_family)
        // get uid
        val uid = intent.getStringExtra("UserID")

        // display all family member and family info
        ViewFamilyController.showAllMembersAndInfo(uid, this)


        findViewById<ImageButton>(R.id.btn_family_setting).setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.family_menu)
            popupMenu.show()
        }
        // change family name

        // change family password

    }

}

