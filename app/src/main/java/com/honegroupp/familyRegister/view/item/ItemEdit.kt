package com.honegroupp.familyRegister.view.item

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.storage.FirebaseStorage

import com.honegroupp.familyRegister.utility.CompressionUtil
import com.honegroupp.familyRegister.utility.EmailPathSwitch
import com.honegroupp.familyRegister.utility.FilePathUtil
import com.honegroupp.familyRegister.utility.ImageRotateUtil
import com.honegroupp.familyRegister.view.item.itemEditDialogs.LocationChangeDialog
import com.honegroupp.familyRegister.view.item.itemEditDialogs.LocationEnterPasswordDialog
import com.honegroupp.familyRegister.view.item.itemEditDialogs.LocationViewDialog
import java.text.ParseException


class ItemEdit : AppCompatActivity(), LocationEnterPasswordDialog.OnViewClickerListener,
    LocationViewDialog.OnChangeClickListener, LocationChangeDialog.OnChangeConfirmClickListener {

    val GALLERY_REQUEST_CODE = 123
    var itemLocation = "Bedside ddtable first drawer"
    var allImageUri = ArrayList<Uri>()
    var detailImageUrls = ArrayList<String>()
    var deleteImageUrls = ArrayList<String>()

    lateinit var databaseRef: DatabaseReference
    lateinit var uid: String
    lateinit var itemKey: String
    lateinit var currFamilyId: String

    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun clickOnChangeLocation(newLocation: String) {
        itemLocation = newLocation
        toast(itemLocation, Toast.LENGTH_SHORT)
        openLocationViewDialog()
    }

    override fun clickOnChangeLocation() {
        openLocationChangeDialog()
    }


    /*
    Compare the password, if the password is correct, show the location view dialog
     */
    override fun applyPasswords(enteredPassword: String, dialog: LocationEnterPasswordDialog) {

        //check emptyness of password
        if(enteredPassword != "") {

            //get user and uid
            val user = FirebaseAuth.getInstance().currentUser

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            val credential: AuthCredential = EmailAuthProvider
                .getCredential(EmailPathSwitch.pathToEmail(uid!!), enteredPassword)


            //use firebase to re authenticate the password
            user?.reauthenticate(credential)
                ?.addOnCompleteListener(OnCompleteListener<Void> { task ->
                    if (task.isSuccessful) {

                        //close the old dialog
                        dialog.dismiss()

                        //open the new dialog
                        openLocationViewDialog()
                    } else {

                        //password is incorrect
                        toast(
                            getString(R.string.edit_location_password_incorrect),
                            Toast.LENGTH_LONG
                        )
                    }
                })
        }else{
            //password is blank
            toast(getString(R.string.password_cannot_be_blank),Toast.LENGTH_SHORT)
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
    fun removeItem(currentIsUrl: Boolean, position:Int){
        if (currentIsUrl){
            deleteImageUrls.add(detailImageUrls[position])
            detailImageUrls.removeAt(position)
        } else {
            allImageUri.removeAt(position)
        }


        // reset the grid view adapter
        val adapter = ItemEditGridAdapter(this, detailImageUrls, allImageUri)
        editImagesGrid.adapter = adapter
        Log.d("ggggginit1", "init")


    }

    fun deleteStorageImages() {
        for (deleteUrl in deleteImageUrls){
            // use url create reference of image to be deleted
            val imageRef = storage.getReferenceFromUrl(deleteUrl)

            // Delete image and its tile from Fitrbase Storage
            imageRef.delete()
                .addOnSuccessListener {
                    toast(getString(R.string.detail_delete_success), Toast.LENGTH_SHORT)
                }
                .addOnFailureListener {
                    toast(getString(R.string.detail_delete_fail), Toast.LENGTH_SHORT)
                }
        }
    }

    fun uploadToFirebase(currItem: Item) {
        val uploadPath = " "
        var numSuccess = 0
        for (uri in allImageUri){

            //get firebase storage reference
            val ref =
                FirebaseStorage.getInstance()
                    .reference.child(uploadPath + System.currentTimeMillis())

            //convert first image in list to bitmap
            val path = FilePathUtil.getFilePathFromContentUri(uri, this)
            val orientation = ImageRotateUtil.getCameraPhotoOrientation(path!!).toFloat()
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

            //decrease the resolution
            val scaledBitmap = CompressionUtil.scaleDown(bitmap, true)

            //correct the orientation of the bitmap
            val orientedScaledBitmap = ImageRotateUtil.rotateBitmap(scaledBitmap,orientation)

            //compress the image
            val data = CompressionUtil.compressImage(orientedScaledBitmap)

            //upload the compressed image
            ref.putBytes(data)
                .addOnSuccessListener {

                    //add item logic
                    ref.downloadUrl.addOnCompleteListener { taskSnapshot ->
                        var url = taskSnapshot.result
                        detailImageUrls.add(url.toString())
                        numSuccess += 1

                        if (numSuccess == allImageUri.size){
                            databaseRef
                                .child(FirebaseDatabaseManager.FAMILY_PATH)
                                .child(currFamilyId)
                                .child("items")
                                .child(itemKey)
                                .child("imageURLs")
                                .setValue(detailImageUrls)
                            toast(getString(R.string.upload_success) + numSuccess.toString() + "/" + allImageUri.size.toString(), Toast.LENGTH_SHORT)
                            Log.d("eeeenimgupload", detailImageUrls.toString())
                        } else {
                            toast(getString(R.string.upload_complete) + " " + numSuccess.toString() + "/" + allImageUri.size.toString(), Toast.LENGTH_SHORT)
                            Log.d("eeeenimg", detailImageUrls.toString())
                        }
                    }
                }
        }
        toast(getString(R.string.uploading) + " " + numSuccess.toString() + "/" + allImageUri.size.toString(), Toast.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // get extra from Item Detail(DetailSlide)
        uid = intent.getStringExtra("uid")
        itemKey = intent.getStringExtra("ItemKey").toString()
        currFamilyId = intent.getStringExtra("FamilyId").toString()

        // retrieve Item
        lateinit var currItem: Item
        val rootPath = "/"
        databaseRef = FirebaseDatabase.getInstance().getReference(rootPath)
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
                for (url in currItem.imageURLs) {
                    detailImageUrls.add(url)
                }
                val adapter = ItemEditGridAdapter(this@ItemEdit, detailImageUrls, allImageUri)
                editImagesGrid.adapter = adapter
                Log.d("ggggginitdatalis", "init")
                findViewById<EditText>(R.id.editName).setText(currItem.itemName)
                findViewById<EditText>(R.id.editDescription).setText(currItem.itemDescription)
                findViewById<EditText>(R.id.editMaterial).setText(currItem.itemMaterial)
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

                    // delete images in storage
                    deleteStorageImages()

                    // upload new images
                    uploadToFirebase(currItem)

                    // need to check item name is not empty
                    if (editName.text.toString() == "") {
                        Toast.makeText(
                            this@ItemEdit,
                            getString(R.string.item_name_should_not_leave_blank),
                            Toast.LENGTH_SHORT
                        ).show()
                        //check at least one photo is added
                    } else if(!legalDate(editItemDate)) {
                        Toast.makeText(
                            this@ItemEdit,
                            getString(R.string.please_pick_date_for_item),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // create item to upload to Firebase
                        val updatedItem = Item(
                            itemName = editName.text.toString(),
                            itemDescription = editDescription.text.toString(),
                            itemMaterial = editMaterial.text.toString(),
                            itemOwnerUID = currItemOwner,
                            imageURLs = detailImageUrls,
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
                        Log.d("ggggginitselectimg", "init")
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