package com.honegroupp.familyRegister.controller

import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item

/**
 * This class is responsible for controller the event related to item.
 *
 * */
class ItemController {
    companion object {

        /**
         * This methods is responsible for creating a item and upload it to the database.
         *
         * */
            fun createItem(
                mContext: AppCompatActivity,
                itemName: EditText,
                itemDescription: EditText,
                itemMaterial: EditText,
                itemLocation: EditText,
                uid: String,
                categoryName: String,
                imageURLs: ArrayList<String>,
                isPublic: Boolean,
                date: String
            ) {

            val item = Item(
                itemName = itemName.text.toString(),
                itemDescription = itemDescription.text.toString(),
                itemMaterial = itemMaterial.text.toString(),
                itemLocation = itemLocation.text.toString(),
                itemOwnerUID = uid,
                imageURLs = imageURLs,
                isPublic = isPublic,
                date = date
            )
            item.store(uid, categoryName)

            Toast.makeText(
                mContext,
                mContext.getText(R.string.Item_create_success),
                Toast.LENGTH_SHORT).show()
        }
    }
}