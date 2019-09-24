package com.honegroupp.familyRegister.view.family

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.FamilyController
import kotlinx.android.synthetic.main.create_family_main.*

/**
 * This class is responsible for user's creating family event.
 *
 * @author Renjie Meng
 * */
class FamilyCreateActivity : AppCompatActivity() {

    lateinit var currUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_family_main)

        //get User ID
        currUid= intent.getStringExtra("UserID")

        // Define the logic when the user click the button for creating family
        clickConfirm(familyCreateConfirm)

    }

    /**
     * This function is responsible for defining the logic of the confirm creating family button.
     * */
    private fun clickConfirm(buttonConfirm: Button) {
        buttonConfirm.setOnClickListener {
            // First, check whether the user input is valid
            val isValid = FamilyController.validateCreateFamilyInput(
                this,
                edit_text_family_name,
                edit_text_family_password,
                edit_text_family_password_again
            )

            // If the user input is valid, create a family as the user specified for him/her.
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


