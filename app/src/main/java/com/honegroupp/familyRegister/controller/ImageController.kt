package com.honegroupp.familyRegister.controller

import android.net.Uri
import com.honegroupp.familyRegister.backend.FirebaseStorageManager
import com.honegroupp.familyRegister.view.item.ItemUploadActivity

/**
 * This class is responsible for controller the event related to item.
 *
 * */
class ImageController {
    companion object {

        /**
         * This method is repsonsible for uploading image to the firebase storage
         *
         * */
        fun uploadImages(
            allImageUri: ArrayList<Uri>,
            activity: ItemUploadActivity
        ) {
            FirebaseStorageManager
                .uploadItemImageToFirebase(allImageUri, activity)
        }

    }
}