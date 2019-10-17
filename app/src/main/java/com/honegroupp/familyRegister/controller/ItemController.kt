package com.honegroupp.familyRegister.controller

import android.content.Context
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.TypedArrayUtils.getText
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.backend.FirebaseStorageManager
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class is responsible for controller the event related to item.
 *
 * */
class ItemController {
    companion object {

        //TODO 1 user could create one item each time

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

        /**
         * This method is repsonsible for uploading image to the firebase storage
         *
         * */
        fun uploadImageToStorage(
            allImageUri: ArrayList<Uri>,
            categoryName: String,
            activity: ItemUploadActivity
        ) {
            Item.uploadImageToFirebaseStorage(allImageUri, categoryName, activity)
        }
    }
}