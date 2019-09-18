package com.honegroupp.familyRegister.view.family

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.FamilyController
import kotlinx.android.synthetic.main.create_family_main.*
import kotlinx.android.synthetic.main.join_family_main.*


class FamilyJoinActivity : AppCompatActivity() {
    lateinit var currUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_family_main)

        //get User ID
        currUid = intent.getStringExtra("UserID")


        // TODO whether the user could join a family

        joinFamily(familyJoinConfirm)
    }

    private fun joinFamily(button: Button) {
        button.setOnClickListener {
            FamilyController.validateJoinFamilyInput(
                this,
                edit_text_family_name_input,
                edit_text_family_password_input,
                currUid
            )
        }
    }
}
