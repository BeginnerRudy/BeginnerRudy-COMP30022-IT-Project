package com.honegroupp.familyRegister.view.item

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.item_upload_page.*
import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.honegroupp.familyRegister.controller.ItemController.Companion.createItem
import com.honegroupp.familyRegister.view.itemList.ItemGridAdapter
import kotlin.collections.ArrayList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.honegroupp.familyRegister.backend.FirebaseStorageManager



class ItemUploadActivity : AppCompatActivity(){
    val GALLERY_REQUEST_CODE = 123
    var imagePathList = ArrayList<String>()
    var allImageUri= ArrayList<Uri>()
    lateinit var uid :String

    var itemPrivacyPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.honegroupp.familyRegister.R.layout.item_upload_page)

        uid = intent.getStringExtra("UserID")
        val categoryName = intent.getStringExtra("categoryPath").toString()

        //set up the spinner (select public and privacy)
        val spinner: Spinner = findViewById(com.honegroupp.familyRegister.R.id.privacy_spinner)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            com.honegroupp.familyRegister.R.array.privacy_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //set up the grid view
        // Get an instance of base adapter
        val adapter = ItemGridAdapter(this,allImageUri)
        // Set the grid view adapter
        itemGridView.adapter = adapter






        addItemConfirm.setOnClickListener{
            itemPrivacyPosition = spinner.selectedItemPosition

            progressBarRound.visibility = View.VISIBLE
//            addItemConfirm.visibility = View.INVISIBLE

            //check input
            checkInputAndUpload(categoryName)
        }
    }



   /* use the phone API to get thr image from the album*/
    fun selectImageInAlbum() {

        //reset the image url list
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // ask for multiple images picker
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("ResourceType", "NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {

//                    adding multiple image
                    if (data != null) {


                        var allUris : ArrayList<Uri> = arrayListOf()

                        if (data.getClipData() != null) {

                            //handle multiple images
                            val count = data.getClipData()!!.getItemCount()


                            for (i in 0 until count) {
                                var uri = data.getClipData()!!.getItemAt(i).uri
                                if (uri != null) {


                                    //add into Uri List
                                    allImageUri.add(uri)
                                    allUris.add(uri)
                                }
                            }




                            //selecting single image from album
                        } else if (data.getData() != null) {

                            val uri = data.getData()
                            if (uri != null) {


                                allUris.add(uri)

                                //add into Uri List
                                allImageUri.add(uri)
                            }

                        }



                        // Get an instance of base adapter
                        val adapter = ItemGridAdapter(this,allImageUri)

                        // Set the grid view adapter
                        itemGridView.adapter = adapter


//                        Toast.makeText(this,numberOfImages.toString(),Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }



   /*remove already selected items from the list, update the view
   */
    fun removeItem(position:Int){

       allImageUri.removeAt(position)

       // reset the grid view adapter
       val adapter = ItemGridAdapter(this, allImageUri)
       itemGridView.adapter = adapter

   }

    fun checkInputAndUpload(categoryName:String){

        // need to check item name is not empty
        if(item_name_input.text.toString() == ""){
            Toast.makeText(this,getString(com.honegroupp.familyRegister.R.string.item_name_should_not_leave_blank),Toast.LENGTH_SHORT).show()

        //check at least one photo is added
        }else if(allImageUri.size == 0) {
            Toast.makeText(this,getString(com.honegroupp.familyRegister.R.string.please_select_at_least_one_image), Toast.LENGTH_SHORT).show()

        }else {
            //upload uri to firebase
            FirebaseStorageManager.uploadToFirebase(allImageUri, categoryName,this)
        }

    }


    //Update the progress bar and display the progress message
    fun displayProgress(){
        val percent = imagePathList.size*100/(imagePathList.size + allImageUri.size)
        progressBarText.text = percent.toString() + " %,  " +
                getString(com.honegroupp.familyRegister.R.string.uploading) +
                (imagePathList.size+1).toString()+
                getString(com.honegroupp.familyRegister.R.string.of)+
                (imagePathList.size + allImageUri.size).toString() + " " +
                getString(com.honegroupp.familyRegister.R.string.image)
    }

}