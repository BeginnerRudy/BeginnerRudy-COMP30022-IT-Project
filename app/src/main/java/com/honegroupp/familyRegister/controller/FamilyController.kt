package com.honegroupp.familyRegister.controller

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Hash


/**
 * This class is responsible for controller the event related to family.
 * There are create, join and view a family.
 *
 * */
class FamilyController {
    companion object {

        /**
         * This method is responsible for validating user input for creating a family
         *
         * */
        fun validateCreateFamilyInput(
            mContext: Context,
            familyId: EditText,
            password: EditText,
            passwordAgain: EditText
        ): Boolean {
            // check if the family name is empty
            if (familyId.text.toString().trim() == "") {
                Toast.makeText(
                    mContext,
                    mContext.getString(R.string.type_family_name),
                    Toast.LENGTH_SHORT
                ).show()
                familyId.text = null

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
                Toast.makeText(mContext, "Please enter the password again", Toast.LENGTH_SHORT)
                    .show()
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
        fun createFamily(
            mActivity: AppCompatActivity,
            familyName: EditText,
            password: EditText,
            uid: String,
            username: String
        ) {
            //The password is encrypted using SHA256
            val hashValue: String = Hash.applyHash(password.text.toString())
            val family = Family(
                familyName = familyName.text.toString(),
                familyId = uid,
                password = hashValue
            )

            // Add the user to the family members
            family.members.add(uid)

            // Initialize the default categories for the new family
            family.categories.add(Category("Letter"))
            family.categories.add(Category("Photo"))
            family.categories.add(Category("Instrument"))
            family.categories.add(Category("Others"))

            // Store the family to the database
            family.store(mActivity, uid, username)

            // Show a toast to remind the user
            Toast.makeText(mActivity, "Family Created Successfully", Toast.LENGTH_SHORT).show()
        }


        /**
         * This methods is responsible for joining family.
         * */
        fun joinFamily(
            mActivity: AppCompatActivity,
            familyId: EditText,
            password: EditText,
            uid: String,
            username:String
        ) {
            // Extract input as String
            val familyIdInput = familyId.text.toString()
            val familyPasswordInput = password.text.toString()

            // Join family
            Family.joinFamily(mActivity, familyIdInput, familyPasswordInput, uid, username)
        }


        /**
         * This method is to navigate button click from mActivity to destination
         *
         * */
        fun buttonClick(
            mActivity: AppCompatActivity,
            button: Button,
            destination: Class<*>,
            uid: String,
            username: String
        ) {
            button.setOnClickListener {
                val intent = Intent(mActivity, destination)
                intent.putExtra("UserID", uid)
                intent.putExtra("UserName", username)
                mActivity.startActivity(intent)
            }
        }
    }
}