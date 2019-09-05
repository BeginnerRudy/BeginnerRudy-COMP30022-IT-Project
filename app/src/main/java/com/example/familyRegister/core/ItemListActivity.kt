package com.example.familyRegister.core

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat.JPEG
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyRegister.model.ItemUpload
import com.example.familyRegister.R
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_item_list.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.annotation.Target as Target1

class ItemListActivity : AppCompatActivity(), ItemListAdapter.OnItemClickerListener{
    lateinit var itemListAdapter: ItemListAdapter
    lateinit var path: String
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var dbListener: ValueEventListener
    lateinit var databaseReference: DatabaseReference
    var itemUploads: ArrayList<ItemUpload> = ArrayList()

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        path = LoginFragment.uid + "/" + intent.getStringExtra("categoryPath") + "/"

        databaseReference = FirebaseDatabase.getInstance().getReference(path)
        // Setting the recycler view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        Log.d("RRR","Running")
        // setting one ItemListAdapter
        itemListAdapter = ItemListAdapter(itemUploads, this@ItemListActivity)
        recycler_view.adapter = itemListAdapter
        itemListAdapter.listener = this@ItemListActivity


        dbListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
                progress_circular.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                // clear it before filling it
                itemUploads.clear()

                p0.children.forEach {
                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                    val currUpload = it.getValue(ItemUpload::class.java) as ItemUpload
                    currUpload.key = it.key
                    itemUploads.add(currUpload)
                }

                // It would update recycler after loading image from firebase storage
                itemListAdapter.notifyDataSetChanged()
                progress_circular.visibility = View.INVISIBLE
            }


        })

        // setting for the btn_add
        btn_add.setOnClickListener {
            openDialog()
            toast("Hah", Toast.LENGTH_SHORT)
        }
    }

    private fun openDialog() {
        val addImageDialog = AddImageDialog(path)
        addImageDialog.show(this@ItemListActivity.supportFragmentManager, "add new item image")
    }


    override fun onItemClick(position: Int) {
        toast("Normal click at position $position", Toast.LENGTH_SHORT)
        val intent = Intent(this, ItemDetailActivity()::class.java)
        intent.putExtra("int_key", position)
        intent.putExtra("itemPath", path + "/" + itemUploads[position].key.toString())
        startActivity(intent)
    }

    override fun onWhatEverClick(position: Int) {
        toast("WhatEver click at position $position", Toast.LENGTH_SHORT)
    }

    override fun onDeleteClick(position: Int) {
        toast("Delete click at position $position", Toast.LENGTH_SHORT)

        val selectedItem = itemUploads[position]
        val selectedKey = selectedItem.key as String
        val imageRef = storage.getReferenceFromUrl(selectedItem.url)
        // Delete image and its tile from Fitrbase Storage
        imageRef.delete()
            .addOnSuccessListener {
                // Delete image title and its image url from Firebase Real-time Database
                databaseReference.child(selectedKey).removeValue()
                toast("Item deleted", Toast.LENGTH_SHORT)
            }
            .addOnFailureListener {
                toast("Failed for deleting the item", Toast.LENGTH_SHORT)
            }
    }
    override fun onDownloadClick(position: Int,item:ArrayList<ItemUpload>){
        this.downloadurl = item[position].url
        Log.d("SAVE333333333","")
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                Log.d("SAVEAAAAAAA","")
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
            }else{
                //permission already granted
                Log.d("SAVEBBBBBB","")
                startDownloading();

            }
        }else{
            //system os less than mashmallow
            Log.d("SAVECCCCCC","")
            startDownloading();
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(dbListener)
    }

    fun ItemListActivity.toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }

    //download the image to local album on the device
    private fun startDownloading() {
        Log.d("SAVEinging","")

        //download request
        val request = DownloadManager.Request(Uri.parse(downloadurl))
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

    //Over Android M version, need to request EXTERNAL STORAGE permission in order to save image
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray){
        when(requestCode){
            STORAGE_PERMISSION_CODE ->{
                if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from the popup was granted, perform download
                    startDownloading()
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}
