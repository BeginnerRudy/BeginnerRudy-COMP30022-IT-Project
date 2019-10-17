package com.honegroupp.familyRegister.backend

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.utility.CompressionUtil
import com.honegroupp.familyRegister.utility.FilePathUtil
import com.honegroupp.familyRegister.utility.ImageRotateUtil
import com.honegroupp.familyRegister.view.home.UserEditActivity
import com.honegroupp.familyRegister.view.item.ItemEditActivity
import com.honegroupp.familyRegister.view.item.ItemUploadActivity

class FirebaseStorageManager {
    companion object {
        private const val uploadPath = " "

        fun uploadToFirebase(
            allImageUri: ArrayList<Uri>,
            categoryName: String,
            activity: ItemUploadActivity
        ) {
            var numSuccess = 0
            for (uri in allImageUri) {


                //get firebase storage reference
                val ref =
                        FirebaseStorage.getInstance()
                            .reference
                            .child(uploadPath + System.currentTimeMillis())

                //convert first image in list to bitmap
                val path = FilePathUtil.getFilePathFromContentUri(uri, activity)
                val orientation =
                        ImageRotateUtil.getCameraPhotoOrientation(path!!)
                            .toFloat()
                val bitmap = MediaStore.Images.Media
                    .getBitmap(activity.contentResolver, uri)

                //decrease the resolution
                val scaledBitmap = CompressionUtil.scaleDown(bitmap, true)

                //correct the orientation of the bitmap
                val orientedScaledBitmap =
                        ImageRotateUtil.rotateBitmap(scaledBitmap, orientation)

                //compress the image
                val data = CompressionUtil.compressImage(orientedScaledBitmap)

                //upload the compressed image
                ref.putBytes(data)
                    .addOnSuccessListener {

                        //add item logic
                        ref.downloadUrl
                            .addOnCompleteListener() { taskSnapshot ->
                                numSuccess += 1
                                var url = taskSnapshot.result
                                activity.imagePathList.add(url.toString())
                                if (numSuccess == allImageUri.size) {
                                    //Create Item And Upload
                                    activity.uploadItem(categoryName)

                                    activity.toast(
                                        activity.getString(R.string.upload_success) + numSuccess.toString() + "/" + allImageUri.size.toString(),
                                        Toast.LENGTH_SHORT
                                    )
                                } else {
                                    activity.toast(
                                        activity.getString(R.string.upload_complete) + " " + numSuccess.toString() + "/" + allImageUri.size.toString(),
                                        Toast.LENGTH_SHORT
                                    )
                                }
                            }
                    }
            }
            activity.toast(
                activity.getString(R.string.uploading) + " " + numSuccess.toString() + "/" + allImageUri.size.toString(),
                Toast.LENGTH_SHORT
            )
        }


        fun uploadEditToFirebase(
            currItem: Item,
            activity: ItemEditActivity
        ) {
            val uploadPath = " "
            var numSuccess = 0
            for (uri in activity.allImageUri) {

                //get firebase storage reference
                val ref =
                        FirebaseStorage.getInstance()
                            .reference
                            .child(uploadPath + System.currentTimeMillis())

                //convert first image in list to bitmap
                val path = FilePathUtil.getFilePathFromContentUri(uri, activity)
                val orientation =
                        ImageRotateUtil.getCameraPhotoOrientation(path!!)
                            .toFloat()
                val bitmap = MediaStore.Images.Media
                    .getBitmap(activity.contentResolver, uri)

                //decrease the resolution
                val scaledBitmap = CompressionUtil.scaleDown(bitmap, true)

                //correct the orientation of the bitmap
                val orientedScaledBitmap =
                        ImageRotateUtil.rotateBitmap(scaledBitmap, orientation)

                //compress the image
                val data = CompressionUtil.compressImage(orientedScaledBitmap)

                val rootPath = "/"
                val databaseRef =
                        FirebaseDatabase.getInstance().getReference(rootPath)

                //upload the compressed image
                ref.putBytes(data)
                    .addOnSuccessListener {

                        //add item logic
                        ref.downloadUrl.addOnCompleteListener { taskSnapshot ->
                            var url = taskSnapshot.result
                            activity.detailImageUrls.add(url.toString())
                            numSuccess += 1

                            if (numSuccess == activity.allImageUri.size) {
                                databaseRef
                                    .child(FirebaseDatabaseManager.FAMILY_PATH)
                                    .child(activity.currFamilyId)
                                    .child("items")
                                    .child(activity.itemKey)
                                    .child("imageURLs")
                                    .setValue(activity.detailImageUrls)
                                activity.toast(
                                    activity.getString(R.string.upload_success) + numSuccess.toString() + "/" + activity.allImageUri.size.toString(),
                                    Toast.LENGTH_SHORT
                                )
                                Log.d(
                                    "eeeenimgupload",
                                    activity.detailImageUrls.toString())
                            } else {
                                activity.toast(
                                    activity.getString(R.string.upload_complete) + " " + numSuccess.toString() + "/" + activity.allImageUri.size.toString(),
                                    Toast.LENGTH_SHORT
                                )
                                Log.d(
                                    "eeeenimg",
                                    activity.detailImageUrls.toString())
                            }
                        }
                    }
            }
            if (activity.allImageUri.size > 0) {
                activity.toast(
                    activity.getString(R.string.uploading) + " " + numSuccess.toString() + "/" + activity.allImageUri.size.toString(),
                    Toast.LENGTH_SHORT
                )
            } else {
                activity.toast(
                    activity.getString(R.string.upload_success),
                    Toast.LENGTH_SHORT)
            }
        }

        fun uploadUserImageToFirebase(
            imageUri: Uri,
            activity: UserEditActivity
        ) {

            //get firebase storage reference
            val ref =
                    FirebaseStorage.getInstance()
                        .reference
                        .child(uploadPath + System.currentTimeMillis())

            //convert first image in list to bitmap
            val path =
                    FilePathUtil.getFilePathFromContentUri(imageUri, activity)
            val orientation =
                    ImageRotateUtil.getCameraPhotoOrientation(path!!).toFloat()
            val bitmap = MediaStore.Images.Media
                .getBitmap(activity.contentResolver, imageUri)

            //decrease the resolution
            val scaledBitmap = CompressionUtil.scaleDown(bitmap, true)

            //correct the orientation of the bitmap
            val orientedScaledBitmap =
                    ImageRotateUtil.rotateBitmap(scaledBitmap, orientation)

            //compress the image
            val data = CompressionUtil.compressImage(orientedScaledBitmap)

            //upload the compressed image
            ref.putBytes(data)
                .addOnSuccessListener {

                    //add item logic
                    ref.downloadUrl.addOnCompleteListener() { taskSnapshot ->

                        //when complete, upload the user
                        var url = taskSnapshot.result
                        activity.uploadUser(url!!.toString())
                    }
                }

        }
    }
}