package com.example.familyRegister.core

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import com.example.familyRegister.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.view.*

/**
 * This class is just  for fake registration in order to create default categories for new user
 *
 *
 * */

class RegisterFragment : Fragment() {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    companion object {
        val defaultCategories = listOf("Letter", "Instrument", "Furniture", "Photos")
        val fakeInitialValue = "fakeInitialValue"
        lateinit var uid: String
    }

    var database = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Navigate to the category fragment
        view.btn_register.setOnClickListener {
            // uid is the user id entered
            uid = view.edit_text_user_id.text.toString()
            // initialize default categories in user's database
            val path = "$uid/"
            val databaseReference = database.getReference(path)
            defaultCategories.forEach {
                databaseReference.child(it).setValue(fakeInitialValue)
            }

            (activity as NavigationHost).navigateTo(CategoryFragment(), false)
        }
        view.downloadbutton.setOnClickListener{
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(checkSelfPermission(context!!,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_DENIED){
                    //permission denied
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
                }else{
                    //permission already granted
                    startDownloading();

                }
            }else{
                //system os less than mashmallow
                startDownloading();
            }

        }
        return view
    }

    private fun startDownloading() {
        val url = urt.text.toString()
        //download request
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${System.currentTimeMillis()}")
        //get download service and enqueue file
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }


    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray){
        when(requestCode){
            STORAGE_PERMISSION_CODE ->{
                if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from the popup was granted, perform download
                    startDownloading()
                }else{
                    Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}