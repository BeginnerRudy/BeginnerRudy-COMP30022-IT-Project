package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ViewFamilyController

class ViewFamilyActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_family)
        // get uid
        uid = intent.getStringExtra("UserID")

        // display all family member and family info
        ViewFamilyController.showAllMembersAndInfo(uid, this)


        findViewById<ImageButton>(R.id.btn_family_setting).setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.family_menu)

            // change family name
            popupMenu.setOnMenuItemClickListener(this)

            // change family password
            popupMenu.show()
        }

    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        when (p0!!.itemId) {
            R.id.change_family_name -> {
                val familyNameChangeDialog = FamilyNameChangeDialog(uid)
                familyNameChangeDialog.show(supportFragmentManager, "Location Change Dialog")
                return true
            }
            R.id.change_family_pws -> {
                val familyNameChangeDialog = FamilyNameChangeDialog("Change pwd")
                familyNameChangeDialog.show(supportFragmentManager, "Location Change Dialog")
                return true
            }
        }

        return false
    }

}

