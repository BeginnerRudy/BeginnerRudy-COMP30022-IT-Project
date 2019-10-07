package com.honegroupp.familyRegister.view.item

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.GenericTypeIndicator
import com.honegroupp.familyRegister.view.item.itemEditDialogs.LocationChangeDialog
import com.honegroupp.familyRegister.view.item.itemEditDialogs.LocationEnterPasswordDialog
import com.honegroupp.familyRegister.view.item.itemEditDialogs.LocationViewDialog


class ItemEdit : AppCompatActivity(), LocationEnterPasswordDialog.OnViewClickerListener,
    LocationViewDialog.OnChangeClickListener, LocationChangeDialog.OnChangeConfirmClickListener {

    val GALLERY_REQUEST_CODE = 123
    val passwordLocation = "1"
    var enteredPassword = ""
    var itemLocation = "Bedside ddtable first drawer"
    var allImageUri = ArrayList<Uri>()
    var detailImageUrls = ArrayList<String>()

    override fun clickOnChangeLocation(newLocation: String) {
        itemLocation = newLocation
        toast(itemLocation, Toast.LENGTH_SHORT)
        openLocationViewDialog()
    }

    override fun clickOnChangeLocation() {
        openLocationChangeDialog()
    }

    override fun applyPasswords(password: String) {
        enteredPassword = password
        if (passwordLocation == enteredPassword) {
            openLocationViewDialog()
        } else {
            toast(getString(R.string.edit_location_password_incorrect), Toast.LENGTH_LONG)
        }
    }

    /*
   use the phone API to get images from the album
   */
    fun selectImageInAlbum() {

        //reset the image url list
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // ask for multiple images picker
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    /*
   remove already selected items from the list, update the view
   */
    fun removeItem(position:Int){

        allImageUri.removeAt(position)

        // reset the grid view adapter
        val adapter = ItemEditGridAdapter(this, detailImageUrls, allImageUri)
        editImagesGrid.adapter = adapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // get extra from Item Detail(DetailSlide)
        val itemKey = intent.getStringExtra("ItemKey").toString()
        val currFamilyId = intent.getStringExtra("FamilyId").toString()

        // retrieve Item
        lateinit var currItem: Item
        val rootPath = "/"
        val databaseRef = FirebaseDatabase.getInstance().getReference(rootPath)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Don't ignore errors!
                Log.d("TAG", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val t = object : GenericTypeIndicator<ArrayList<String>>() {}

                // get current Item from data snap shot
                currItem =
                    p0
                        .child(FirebaseDatabaseManager.FAMILY_PATH)
                        .child(currFamilyId)
                        .child("items")
                        .child(itemKey)
                        .getValue(Item::class.java) as Item

                // get current family members from data snap shot
                var currFamilyMembers =
                    p0
                        .child(FirebaseDatabaseManager.FAMILY_PATH)
                        .child(currFamilyId)
                        .child("members")
                        .getValue(t) as ArrayList<String>

                // get users username in family, prepare for pass down
                var userNames: Array<String> = emptyArray()
                val usersHashMap:HashMap<String, String> = HashMap()
                p0.child("Users").children.forEach {
                    val currUserUploads = it.getValue(User::class.java) as User

                    if (it.key in currFamilyMembers && it.key != currItem.itemOwnerUID){
                        usersHashMap[currUserUploads.username] = it.key.toString()
                        userNames = userNames.plus(currUserUploads.username)
                    }
                }

                // set current item to view
//                Picasso.get()
//                    .load(currItem.imageURLs[0])
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(editItemImage)
                detailImageUrls = currItem.imageURLs
                val adapter = ItemEditGridAdapter(this@ItemEdit, detailImageUrls, allImageUri)
                editImagesGrid.adapter = adapter
                findViewById<EditText>(R.id.editName).setText(currItem.itemName)
                findViewById<EditText>(R.id.editDescription).setText(currItem.itemDescription)
                findViewById<TextView>(R.id.editItemDate).setText(currItem.date)

                // set position click
                edit_location_layout.setOnClickListener { openLocationEnterPasswordDialog() }

                // set current item Owner
                var currItemOwner = currItem.itemOwnerUID

                // set passDown dialog
                editPassDownBtn.setOnClickListener(){
                    val mBuilder = AlertDialog.Builder(this@ItemEdit)
                    mBuilder.setTitle(R.string.edit_pass_down_text).setItems(userNames, DialogInterface.OnClickListener { dialog, which ->
                        currItemOwner = usersHashMap[userNames[which]].toString()
                        findViewById<TextView>(R.id.edit_passdown_to).text = userNames[which]
                        // The 'which' argument contains the index position
                        // of the selected item
                    })

                    // Set the neutral/cancel button click listener
                    mBuilder.setNeutralButton(R.string.edit_cancel) { dialog, which ->
                        // Do something when click the neutral button
                        currItemOwner = currItem.itemOwnerUID
                        findViewById<TextView>(R.id.edit_passdown_to).setText(R.string.edit_pass_down_to)
                        dialog.cancel()
                    }

                    val mDialog = mBuilder.create()
                    mDialog.window?.setBackgroundDrawableResource(R.color.fui_bgAnonymous)
                    mDialog.show()
                }

                // set Date picker
                setDatePicker(editItemDate)

                // set up the spinner (select public and privacy)
                val spinner: Spinner = findViewById(R.id.edit_privacy_spinner)

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter.createFromResource(
                    this@ItemEdit,
                    R.array.privacy_options,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    spinner.adapter = adapter
                }

                // set on click listener
                editConfirm.setOnClickListener {
                    if (editName.text.toString() == "") {
                        Toast.makeText(
                            this@ItemEdit,
                            "Item name should not leave blank",
                            Toast.LENGTH_SHORT
                        ).show()
//                    }else if(numberOfImages == 0) {
//                        Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show()
//                    }else if(numberOfImages != imagePathList.size){
//                        Toast.makeText(this, "Please wait for uploading image", Toast.LENGTH_SHORT).show()
                    } else {

                        // create item to upload to Firebase
                        val updatedItem = Item(
                            itemName = editName.text.toString(),
                            itemDescription = editDescription.text.toString(),
                            itemOwnerUID = currItemOwner,
                            imageURLs = currItem.imageURLs,
                            isPublic = spinner.selectedItemPosition == 0,
                            date = editItemDate.text.toString(),
                            showPageUids = currItem.showPageUids
                        )

                        // upload to Firebase
                        val itemPath =
                            FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/" + "items/" + itemKey
                        val databaseRef = FirebaseDatabase.getInstance().getReference(itemPath)

                        databaseRef.child("").setValue(updatedItem)

                        // Go back to the previous activity
                        this@ItemEdit.finish()
                    }
                }
            }
        })
    }

    /*
    process when receive the result of image selection
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {

                    // adding multiple image
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
                        val adapter = ItemEditGridAdapter(this, detailImageUrls, allImageUri)

                        // Set the grid view adapter
                        editImagesGrid.adapter = adapter
                    }else{
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun openLocationEnterPasswordDialog() {
        val locationEnterPasswordDialog = LocationEnterPasswordDialog()
        locationEnterPasswordDialog.show(supportFragmentManager, "Location Enter Password Dialog")
    }

    private fun openLocationViewDialog() {
        val locationViewDialog = LocationViewDialog(itemLocation)
        locationViewDialog.show(supportFragmentManager, "Location View Dialog")
    }

    private fun openLocationChangeDialog() {
        val locationChangeDialog = LocationChangeDialog(itemLocation)
        locationChangeDialog.show(supportFragmentManager, "Location Change Dialog")
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
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
}