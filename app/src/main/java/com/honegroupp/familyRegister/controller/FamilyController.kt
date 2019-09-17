package com.honegroupp.familyRegister.controller

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Family


/**
 * This class is responsible for controller the event related to family.
 * There are create, join and view a family.
 *
 * */
class FamilyController {
    companion object {

        //TODO 1 user could only create one family

        //TODO user currently in family has no right to create family

        /**
         * This method is responsible for validating user input for creating a family
         *
         * */
        fun validateCreateFamilyInput(
            mContext: Context,
            familyname: EditText,
            password: EditText,
            passwordAgain: EditText
        ): Boolean {
            // check if the family name is empty
            if (familyname.text.toString().trim() == "") {
                Toast.makeText(mContext, mContext.getString(R.string.family_name), Toast.LENGTH_SHORT).show()
                familyname.text = null

                return false
            }
            // check if first password entered
            else if (password.text.toString() == "") {
                Toast.makeText(mContext, "Please enter the password", Toast.LENGTH_SHORT).show()
                passwordAgain.text = null
                return false
            }

            // check if repeated password entered
            else if (passwordAgain.text.toString() == "") {
                Toast.makeText(mContext, "Please enter the password again", Toast.LENGTH_SHORT).show()
                return false
            }

            // check are these two passwords identical
            else if (password.text.toString() != passwordAgain.text.toString()) {
                Toast.makeText(mContext, "Two passwords are not same", Toast.LENGTH_SHORT).show()
                password.text = null
                passwordAgain.text = null
                return false
            }

            // Pass all the test
            return true
        }

        /**
         * This methods is responsible for creating a family and upload it to the database.
         *
         * */
        fun createFamily(mContext: AppCompatActivity,
                         familyname: EditText,
                         password: EditText,
                         uid: String){
            val family = Family(familyname.text.toString(), password.text.toString(), uid)
            family.members.add(uid)
            family.store()

            Toast.makeText(mContext, "Family Created Successfully", Toast.LENGTH_SHORT).show()
            // Go back to the previous activity
            mContext.finish()
        }

    }
}