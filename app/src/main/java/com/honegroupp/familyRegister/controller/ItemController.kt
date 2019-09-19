package com.honegroupp.familyRegister.controller

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User

/**
 * This class is responsible for controller the event related to family.
 * There are create, join and view a family.
 *
 * */
class   ItemController {
    companion object {

        //TODO 1 user could create one item each time

        /**
         * This methods is responsible for creating a item and upload it to the database.
         *
         * */
        fun createItem(
            mContext: AppCompatActivity,
            itemName: EditText,
            uid: String,
            imageURLs: ArrayList<String>
        ) {
            val item = Item(itemName.text.toString(), uid, imageURLs)
            item.store(uid)

            Toast.makeText(mContext, "Family Created Successfully", Toast.LENGTH_SHORT).show()
            // Go back to the previous activity
            mContext.finish()
        }

    }
}