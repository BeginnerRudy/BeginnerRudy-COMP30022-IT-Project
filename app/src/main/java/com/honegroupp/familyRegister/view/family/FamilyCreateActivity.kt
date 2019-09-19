package com.honegroupp.familyRegister.view.family

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.FamilyController
import kotlinx.android.synthetic.main.create_family_main.*


class FamilyCreateActivity : AppCompatActivity() {

    lateinit var currUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_family_main)

        //get User ID
        currUid= intent.getStringExtra("UserID")

        clickConfirm(familyCreateConfirm)

    }

    private fun clickConfirm(buttonConfirm: Button) {
        buttonConfirm.setOnClickListener {
            val isValid = FamilyController.validateCreateFamilyInput(
                this,
                edit_text_family_name,
                edit_text_family_password,
                edit_text_family_password_again
            )

            if (isValid) {
                FamilyController.createFamily(
                    this,
                    edit_text_family_name,
                    edit_text_family_password,
                    currUid
                )

            }
        }
    }


}


