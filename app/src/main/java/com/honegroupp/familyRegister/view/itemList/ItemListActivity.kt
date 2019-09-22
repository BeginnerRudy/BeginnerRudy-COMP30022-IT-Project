package com.honegroupp.familyRegister.view.itemList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*

import com.honegroupp.familyRegister.R
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.controller.ItemListController
import com.honegroupp.familyRegister.model.Item
import kotlin.collections.ArrayList

class ItemListActivity : AppCompatActivity() {
    lateinit var itemListAdapter: ItemListAdapter
    lateinit var uid: String
    lateinit var path: String
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var dbListener: ValueEventListener
    lateinit var databaseReference: DatabaseReference
    var itemUploads: ArrayList<Item> = ArrayList()

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        //get User ID
        uid = intent.getStringExtra("UserID")
        val categoryName = intent.getStringExtra("categoryPath")

        // get category by its name
        ItemListController.addItem(uid, categoryName, this)

    }


//    override fun onItemClick(position: Int) {
//        toast("Normal click at position $position", Toast.LENGTH_SHORT)
//        val intent = Intent(this, ItemDetailActivity()::class.java)
//        intent.putExtra("int_key", position)
//        intent.putExtra("itemPath", path + "/" + itemUploads[position].key.toString())
//        startActivity(intent)
//    }
//
//    override fun onWhatEverClick(position: Int) {
//        toast("WhatEver click at position $position", Toast.LENGTH_SHORT)
//    }
//
//    override fun onDeleteClick(position: Int) {
//        toast("Delete click at position $position", Toast.LENGTH_SHORT)
//
//        val selectedItem = itemUploads[position]
//        val selectedKey = selectedItem.key as String
//        val imageRef = storage.getReferenceFromUrl(selectedItem.url)
//        // Delete image and its tile from Fitrbase Storage
//        imageRef.delete()
//            .addOnSuccessListener {
//                // Delete image title and its image url from Firebase Real-time Database
//                databaseReference.child(selectedKey).removeValue()
//                toast("Item deleted", Toast.LENGTH_SHORT)
//            }
//            .addOnFailureListener {
//                toast("Failed for deleting the item", Toast.LENGTH_SHORT)
//            }
//    }
//    override fun onDownloadClick(position: Int,item:ArrayList<Item>){
//        this.downloadurl = item[position].url
//        Log.d("SAVE333333333","")
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_DENIED){
//                //permission denied
//                Log.d("SAVEAAAAAAA","")
//                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
//            }else{
//                //permission already granted
//                Log.d("SAVEBBBBBB","")
//                startDownloading();
//
//            }
//        }else{
//            //system os less than mashmallow
//            Log.d("SAVECCCCCC","")
//            startDownloading();
//        }
//
//    }

    override fun onDestroy() {
        super.onDestroy()
//        databaseReference.removeEventListener(dbListener)
    }

    fun ItemListActivity.toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }

    //download the image to local album on the device
//    private fun startDownloading() {
//        Log.d("SAVEinging","")
//
//        //download request
//        val request = DownloadManager.Request(Uri.parse(downloadurl))
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
//        request.setTitle("Download")
//        request.setDescription("The file is downloading...")
//        request.allowScanningByMediaScanner()
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${System.currentTimeMillis()}")
//        //get download service and enqueue file
//        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        manager.enqueue(request)
//    }

    //Over Android M version, need to request EXTERNAL STORAGE permission in order to save image
//    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray){
//        when(requestCode){
//            STORAGE_PERMISSION_CODE ->{
//                if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    //permission from the popup was granted, perform download
////                    startDownloading()
//                }else{
//                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }


}