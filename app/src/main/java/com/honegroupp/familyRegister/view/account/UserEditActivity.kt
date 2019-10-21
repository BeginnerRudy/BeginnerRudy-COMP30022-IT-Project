package com.honegroupp.familyRegister.view.account

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.DatabaseManager.FirebaseDatabaseManager
import com.honegroupp.familyRegister.backend.StorageManager.FirebaseStorageManager
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.utility.FilePathUtil
import com.honegroupp.familyRegister.utility.ImageRotateUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_edit.*

/**
 * This class is for show User Edit page, user can change name and their
 * image on their page
 * */
class UserEditActivity : AppCompatActivity() {
    val GALLERY_REQUEST_CODE = 123
    private val READ_PERMISSION_CODE: Int = 1000

    lateinit var isFamilyOwner: String
    lateinit var familyId: String
    lateinit var uid: String
    var uri: Uri? = null

    /**
     * create the activity
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        //get the uid from intent
        uid = intent.getStringExtra("UserID")
        val usernameString = intent.getStringExtra("UserName")
        val imageUrl = intent.getStringExtra("ImageUrl")
        familyId = intent.getStringExtra("familyId")
        isFamilyOwner = intent.getStringExtra("IsFamilyOwner")

        //set initial value
        findViewById<EditText>(R.id.username_edit).setText(usernameString)
        val imageView = findViewById<ImageView>(R.id.user_image_edit)

        // Load image to ImageView via its URL from Firebase Storage
        if (imageUrl != "") {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.loading_jewellery)
                .fit()
                .centerCrop()
                .into(imageView)
        }


        //get image from the api
        choose_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1000)
                } else {
                    //permission already granted
                    selectSingleImageInAlbum()
                }
            } else {
                //system os less than mashmallow
                selectSingleImageInAlbum()
            }
        }

        //update the
        user_update.setOnClickListener {
            //some image selected
            if (uri != null) {
                FirebaseStorageManager
                    .uploadUserImageToFirebase(uri!!, this)
            } else {
                uploadUser(imageUrl)
            }
        }
    }

    /**
     * Use the phone API to get single images from the album
     * */
    private fun selectSingleImageInAlbum() {

        //reset the image url list
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // ask for single images picker
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    /**
     * process when receive the result of image selection
     * */
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

                        //get the uri
                        val uri = data.data


                        //Find the image
                        val imageView =
                                findViewById<ImageView>(R.id.user_image_edit)

                        //load the image the the view, get the orientation
                        val path = FilePathUtil
                            .getFilePathFromContentUri(uri!!, this)
                        val orientation = ImageRotateUtil
                            .getCameraPhotoOrientation(path!!).toFloat()

                        //load the image
                        Picasso.get().load(uri).fit().centerCrop()
                            .rotate(orientation).into(imageView)

                        this.uri = uri

                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    /**
     * Over Android M version, need to request EXTERNAL STORAGE permission
     * in order to save image
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
                    selectSingleImageInAlbum()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    /**
     * Upload the user to the firebase
     * */
    fun uploadUser(uri: String) {
        val user = User(
            username = username_edit.text.toString(),
            familyId = familyId,
            imageUrl = uri,
            isFamilyOwner = isFamilyOwner.toBoolean())

        val databaseRef = FirebaseDatabase.getInstance()
            .getReference(FirebaseDatabaseManager.USER_PATH)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Don't ignore errors!
                Log.d("TAG", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                // remove listener, since we only want to call this listener once.
                databaseRef.removeEventListener(this)

                //update the user info
                // Use the uid to construct the user's uiq path on database
                databaseRef.child(uid).setValue(user)
                this@UserEditActivity.finish()
            }
        })

    }

}
