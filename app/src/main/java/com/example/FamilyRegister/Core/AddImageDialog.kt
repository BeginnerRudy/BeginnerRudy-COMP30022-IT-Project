package com.example.FamilyRegister.Core

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.FamilyRegister.Model.ItemUpload
import com.example.FamilyRegister.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_add_image.view.*


class AddImageDialog(val uploadPath: String) : AppCompatDialogFragment() {
    val FILE_CHOOSER = 123
    var selected_img_uri: Uri? = null
    lateinit var firebaseStore: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var databaseRef: DatabaseReference
    lateinit var mView: View
    var uploadTask: StorageTask<UploadTask.TaskSnapshot>? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val mBuilder = AlertDialog.Builder(activity)
        mView = activity!!.layoutInflater.inflate(R.layout.dialog_add_image, null)

        // build the view
        mBuilder.setView(mView)
            .setTitle("Add New Item Image")
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i -> })

        // set logic on the dialog
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference(uploadPath)
        databaseRef = FirebaseDatabase.getInstance().getReference(uploadPath)

        mView.btn_choose_img.setOnClickListener {
            selectImageInAlbum()
        }

        mView.btn_upload.setOnClickListener {
            if (uploadTask != null && uploadTask!!.isInProgress) {
                toast("ItemUpload in progress", Toast.LENGTH_SHORT)
            } else {
                uploadImage()
            }
        }


        // return the dialog
        return mBuilder.create()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadImage() {
        when {
            // If the user press upload button before enter a file name, remind the user of this
            mView.edit_txt_image_name.text.toString().trim() == "" -> toast(
                "Please Name the Image, Before Uploading",
                Toast.LENGTH_SHORT
            )
            selected_img_uri != null -> {
                val fileName = mView.edit_txt_image_name.text.toString()
                val ref = FirebaseStorage.getInstance().reference.child(uploadPath + fileName)

                // ItemUpload image and its title to Firebase Storage
                uploadTask = ref.putFile(selected_img_uri!!)
                    .addOnSuccessListener {
                        Log.d("Image Uploader", "Successfully uploaded image: ${it.metadata?.path}")

                        Handler().postDelayed({
                            mView.progress_bar.setProgress(0, false)
                        }, 500)

                        toast("ItemUpload Successful", Toast.LENGTH_SHORT)

                        // ItemUpload image title and its download url to Firebase Real-time Database
                        ref.downloadUrl.addOnCompleteListener() { taskSnapshot ->

                            var url = taskSnapshot.result
//                            Log.d("url by ref", "url =" + url.toString())
                            var description = mView.edit_txt_description.text.toString()

                            if (description.trim() == "") {
                                description = "None"
                            }


                            val upload = ItemUpload(
                                mView.edit_txt_image_name.text.toString().trim(),
                                url.toString(),
                                description
                            )

                            // Store to database
                            val uploadID = databaseRef.push().key.toString()
                            // create a new entry in our database with uploadID, and set data to our upload object
                            databaseRef.child(uploadID).setValue(upload)
                        }

                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                        mView.progress_bar.setProgress(progress.toInt(), true)
                    }

            }
            else -> toast("Please ItemUpload an Image", Toast.LENGTH_SHORT)
        }
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(intent, FILE_CHOOSER)
        }
    }

    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(intent1, FILE_CHOOSER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FILE_CHOOSER -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
//                    img_view.setImageURI(data.data)
//                    Use Picasso here since we late would upload images to Firebase with Picasso
                    selected_img_uri = data.data
                    Picasso.get().load(selected_img_uri).into(mView.img_view)

                }
            }
            else -> {
                toast("Unrecognized request code", Toast.LENGTH_SHORT)
            }
        }
    }

    fun AddImageDialog.toast(mes: String, duration: Int) {
        Toast.makeText(activity, mes, duration).show()
    }

}
