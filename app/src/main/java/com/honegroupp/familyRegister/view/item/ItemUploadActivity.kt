package com.honegroupp.familyRegister.view.item

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ImageController
import com.honegroupp.familyRegister.controller.ItemController
import com.honegroupp.familyRegister.view.itemList.ItemGridAdapter
import kotlinx.android.synthetic.main.item_upload_page.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

/**
 * This class for upload item, process input and upload to firebase
 * */
class ItemUploadActivity : AppCompatActivity() {
    private val GALLERY_REQUEST_CODE = 123
    var imagePathList = ArrayList<String>()
    private var allImageUri = ArrayList<Uri>()
    private lateinit var uid: String
    lateinit var categoryName: String

    private var itemPrivacyPosition: Int = 0
    private val READ_PERMISSION_CODE: Int = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_upload_page)

        uid = intent.getStringExtra("UserID").toString()
        categoryName = intent.getStringExtra("categoryPath").toString()
        val sortOrder = intent.getStringExtra("SortOrder")

        //set up the spinner (select public and privacy)
        val spinner: Spinner = this.findViewById(R.id.privacy_spinner)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.privacy_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //set up the grid view
        // Get an instance of base adapter
        val adapter = ItemGridAdapter(this, allImageUri)

        // Set the grid view adapter
        itemGridView.adapter = adapter
        //set height of gridview
        setGridViewHeight(itemGridView)

        addItemConfirm.setOnClickListener {
            //            it.isEnabled = false
            itemPrivacyPosition = spinner.selectedItemPosition

            //            progressBarRound.visibility = View.VISIBLE
            //check input
            checkInputAndUpload(categoryName)
        }

        // set date picker
        setDatePicker(text_date)
    }

    /**
     * Over Android M version, need to request EXTERNAL STORAGE permission in
     * order to save image
     * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(
                        this,
                        getString(R.string.get_read_permission),
                        Toast.LENGTH_SHORT).show()
                    selectImageInAlbum()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    /**
     * create the item and upload
     * */
    fun uploadItem() {

        ItemController.createItem(
            this,
            this.item_name_input,
            this.item_description_input,
            this.item_material_input,
            this.item_location_input,
            this.uid,
            categoryName,
            this.imagePathList,
            this.itemPrivacyPosition == 0,
            this.text_date.text.toString()
        )
    }

    /**
     *use the phone API to get images from the album
     */
    fun selectImageInAlbum() {

        //reset the image url list
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // ask for multiple images picker
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    /**
     *process when receive the result of image selection
     */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {

                    //                    adding multiple image
                    if (data != null) {


                        val allUris: ArrayList<Uri> = arrayListOf()

                        if (data.clipData != null) {

                            //handle multiple images
                            val count = data.clipData!!.getItemCount()

                            for (i in 0 until count) {
                                var uri = data.clipData!!.getItemAt(i).uri
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
                        val adapter = ItemGridAdapter(this, allImageUri)

                        // Set the grid view adapter
                        itemGridView.adapter = adapter

                        //update gridview height
                        setGridViewHeight(itemGridView)


                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     *remove already selected items from the list, update the view
     * */
    fun removeItem(position: Int) {

        allImageUri.removeAt(position)

        // reset the grid view adapter
        val adapter = ItemGridAdapter(this, allImageUri)
        itemGridView.adapter = adapter

    }

    /**
     * This method check the input is valid and upload to firebase if it is valid
     * */
    private fun checkInputAndUpload(categoryName: String) {

        // need to check item name is not empty
        if (item_name_input.text.toString() == "") {
            Toast.makeText(
                this,
                getString(R.string.item_name_should_not_leave_blank),
                Toast.LENGTH_SHORT).show()

            //check at least one photo is added
        } else if (allImageUri.size == 0) {
            Toast.makeText(
                this,
                getString(R.string.please_select_at_least_one_image),
                Toast.LENGTH_SHORT).show()

        } else if (!legalDate(text_date)) {
            Toast.makeText(
                this,
                getString(R.string.please_pick_date_for_item),
                Toast.LENGTH_SHORT).show()

        } else {
            addItemConfirm.isEnabled = false

            //upload uri to firebase
            ImageController.uploadImages(allImageUri, this)
            this.finish()
        }

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

        //set cursor invisible
        textView.isCursorVisible = false

        //disable keyboard because select date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.showSoftInputOnFocus = false
        } else {
            textView.setTextIsSelectable(true)
        }

        val cal = Calendar.getInstance()
        val dateSetListener =
                DatePickerDialog
                    .OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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

    fun toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }

    /*
     *get the screen pixel size and calculated the image size
     * */
    private fun getScreenWidth(): Int {
        val display = this.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    /**
     * change the height of gridview according to the number of images
     * */
    private fun setGridViewHeight(gridView: GridView) {
        val columnsNumber = (ceil(gridView.adapter.count * 1.0 / 3)).toInt()
        val cloumnHeight = ceil(getScreenWidth() * 1.0 / 3).toInt()
        gridView.layoutParams.height = columnsNumber * cloumnHeight

    }

}