package com.honegroupp.familyRegister.view.item

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_upload_page.*
import android.app.Activity
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.honegroupp.familyRegister.controller.ItemController.Companion.createItem


class ItemUploadActivity : AppCompatActivity(){
    val GALLERY_REQUEST_CODE = 123
    var imagePathList = ArrayList<String>()
    lateinit var uid :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_upload_page)
        uid = intent.getStringExtra("UserID")

        itemChooseImage.setOnClickListener {
            selectImageInAlbum()
        }

        addItemConfirm.setOnClickListener{

//            need to CHECK empty logic
            createItem(this,item_name_input,uid,imagePathList)
        }
    }

    //use the phone API to get thr image from the album
    private fun selectImageInAlbum() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {



                    //data.getData returns the content URI for the selected Image
                    val selectedImage = data!!.data
                    itemImage.setImageURI(selectedImage)

                    //upload image to firebase storage
                    if (selectedImage != null) {
                        uploadtofirebase(selectedImage)
                    }






//                    adding multiple image
//                    if (data != null) {
//                        if (data.getClipData() != null) {
//
//                            val count = data.getClipData()!!.getItemCount()
//
//                            Toast.makeText(this,count.toString(),Toast.LENGTH_LONG).show()
//                            for (i in 0 until count) {
//
//                                val imageUri = data.getClipData()!!.getItemAt(i).uri
//                                    if (imageUri != null) {
//                                    uploadtofirebase(imageUri)
//                                }
//                            }
//                        } else if (data.getData() != null) {
//
//                            val imgUri = data.getData()
//                            if (imgUri != null) {
//                                uploadtofirebase(imgUri)
//                            }
//                        }
//                    }





                }
            }
        }
    }

    private fun uploadtofirebase(selectedImage: Uri) {
        val uploadPath = " "
        val firebaseStore = FirebaseStorage.getInstance()
        val ref =
            FirebaseStorage.getInstance().reference.child(uploadPath + System.currentTimeMillis())
        var uploadTask: StorageTask<UploadTask.TaskSnapshot>? = ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                //add item logic

                ref.downloadUrl.addOnCompleteListener() { taskSnapshot ->

                    var url = taskSnapshot.result

                    this.imagePathList.add(url.toString())

                }
            }
    }
//    private fun uploadItem(){
//        createItem(this,item_name_input,uid,imagePathList)
//    }
}