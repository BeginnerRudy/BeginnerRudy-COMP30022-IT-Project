package com.honegroupp.familyRegister.view.item

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import kotlinx.android.synthetic.main.item_upload_page.*
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.honegroupp.familyRegister.controller.ItemController.Companion.createItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ItemUploadActivity : AppCompatActivity() {
    val GALLERY_REQUEST_CODE = 123
    var imagePathList = ArrayList<String>()
    var numberOfImages = 0
    lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_upload_page)
        uid = intent.getStringExtra("UserID")
        val categoryName = intent.getStringExtra("categoryPath").toString()

        //set up the spinner (select public and privacy)
        val spinner: Spinner = findViewById(R.id.privacy_spinner)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.privacy_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        itemChooseImage.setOnClickListener {
            selectImageInAlbum()
        }

        addItemConfirm.setOnClickListener {

            //            need to CHECK empty logic
            if (item_name_input.text.toString() == "") {
                Toast.makeText(this, "Item name should not leave blank", Toast.LENGTH_SHORT).show()
            } else if (numberOfImages == 0) {
                Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show()
            } else if (!legalDate(text_date)) {
                Toast.makeText(this, "Please pick a date for item", Toast.LENGTH_SHORT).show()
            } else if (numberOfImages != imagePathList.size) {
//                Toast.makeText(this, numberOfImages.toString() +" " + imagePathList.size.toString(),Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Please wait for uploading image", Toast.LENGTH_SHORT).show()
            } else {
                createItem(
                    this,
                    item_name_input,
                    item_description_input,
                    uid,
                    categoryName,
                    imagePathList,
                    spinner.selectedItemPosition == 0,
                    text_date.text.toString()
                )
            }
        }

        // set date picker
        setDatePicker(text_date)
    }

    /**
     * This method validate whether the text of a given textView is a valid date or not.
     * */
    private fun legalDate(textView: TextView): Boolean {
        val dobString = textView.text.toString()
        val df = SimpleDateFormat("dd/M/yyyy")
        df.isLenient = false
        return try {
            val date: Date = df.parse(dobString)
            true
        } catch (e: ParseException) {
            false
        }
    }

    /**
     * This method is responsible for setting date picker.
     * */
    private fun setDatePicker(textView: TextView) {
        var cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("dd/M/yyyy")
                textView.text = sdf.format(cal.time)

            }

        textView.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    //use the phone API to get thr image from the album
    private fun selectImageInAlbum() {

        //reset the image url list
        imagePathList.clear()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        // ask for multiple images picker
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {

//                    adding multiple image
                    if (data != null) {
                        if (data.getClipData() != null) {

                            //handle multiple images
                            val count = data.getClipData()!!.getItemCount()
                            numberOfImages = count


                            for (i in 0 until count) {
                                val uri = data.getClipData()!!.getItemAt(i).uri
                                if (uri != null) {
                                    uploadtofirebase(uri)
                                }
                            }
                        } else if (data.getData() != null) {
                            //handle single image
                            numberOfImages = 1

                            val uri = data.getData()
                            if (uri != null) {
                                uploadtofirebase(uri)
                            }
                        }
                        Toast.makeText(this, numberOfImages.toString(), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
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
}