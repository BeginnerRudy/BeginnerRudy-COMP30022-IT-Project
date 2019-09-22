package com.honegroupp.familyRegister.controller

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.provider.ContactsContract
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.EmailPathSwitch
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Hash
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.home.HomeActivity
import com.honegroupp.familyRegister.view.item.ItemUploadActivity


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
            familyId: EditText,
            password: EditText,
            uid: String
        ) {
            //The password is encrypted using SHA256
            val hashValue :String = Hash.applyHash(password.text.toString())
            val family = Family(familyId.text.toString(), hashValue, uid)
            family.members.add(uid)
            family.store(mActivity, uid)

            Toast.makeText(mActivity, "Family Created Successfully", Toast.LENGTH_SHORT).show()


        }


        /**
         * This methods is responsible for validating family id and its password.
         * TODO This method might not be in controller.
         * */
        fun validateJoinFamilyInput(
            mActivity: AppCompatActivity,
            familyId: EditText,
            password: EditText,
            uid: String
        ) {

            //email
            val familyIdInput = familyId.text.toString()

            //convert email to path
            val familyRelativePath = EmailPathSwitch.emailToPath(familyIdInput)
            val familyPasswordInput = password.text.toString()
            val inputHashValue = Hash.applyHash(familyPasswordInput)

            Family.validateJoinFamilyInput(mActivity, familyIdInput, familyPasswordInput, uid)
        }


        /**
         * This method is to navigate button click from mActivity to destination
         *
         * */
        fun buttonClick(mActivity: AppCompatActivity, button: Button, destination: Class<*>, uid: String) {
            button.setOnClickListener {
                val intent = Intent(mActivity, destination)
                intent.putExtra("UserID", uid)
                mActivity.startActivity(intent)
            }
        }
    }
}