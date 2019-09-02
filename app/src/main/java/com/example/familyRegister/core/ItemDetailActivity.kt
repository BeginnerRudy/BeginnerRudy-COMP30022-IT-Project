package com.example.familyRegister.core

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_list.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import com.example.familyRegister.R
import com.example.familyRegister.model.ItemUpload
import kotlinx.android.synthetic.main.image_detail.*


class ItemDetailActivity() : AppCompatActivity(), ItemDetailAdapter.OnItemClickerListener {
    // add
    lateinit var path: String
    private val STORAGE_PERMISSION_CODE: Int = 1000


    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    lateinit var itemDetailAdapter: ItemDetailAdapter

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var dbListener: ValueEventListener
    lateinit var databaseReference: DatabaseReference
    var uploads: ArrayList<ItemUpload> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        // add
        path = intent.getStringExtra("itemPath")
        Log.d("excution flag", "$path")
        databaseReference = FirebaseDatabase.getInstance().getReference(path)

        var item_position = 0
        Log.i(TAG, "position: $item_position")
        if (savedInstanceState!=null){

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        // Setting the recycler view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        // setting one ImageAdapter
        itemDetailAdapter = ItemDetailAdapter(uploads, this@ItemDetailActivity, item_position)
        recycler_view.adapter = itemDetailAdapter
        itemDetailAdapter.listener = this@ItemDetailActivity


        dbListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
                progress_circular.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("excution flag", "sadfasfsdgdfhdfhfg")
                // clear it before filling it
                uploads.clear()

                    // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                    val currUpload = p0.child("").getValue(ItemUpload::class.java) as ItemUpload
                    currUpload!!.key = p0.child("").key
                    uploads.add(currUpload)
                Log.d("upload",uploads.size.toString())


                // It would update recycler after loading image from firebase storage
                itemDetailAdapter.notifyDataSetChanged()
//                Log.d("upload",uploads.size.toString())
                progress_circular.visibility = View.INVISIBLE
            }

        })

//        downloadbutton.setOnClickListener{
//            Log.d("SAVE333333333","")
//            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                    PackageManager.PERMISSION_DENIED){
//                    //permission denied
//                    Log.d("SAVEAAAAAAA","")
//                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
//                }else{
//                    //permission already granted
//                    Log.d("SAVEBBBBBB","")
//                    startDownloading();
//
//                }
//            }else{
//                //system os less than mashmallow
//                Log.d("SAVECCCCCC","")
//                startDownloading();
//            }
//
//        }
    }


    //download the image to local album on the device
    private fun startDownloading() {
        Log.d("SAVEinging","")
        val url = "https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/1%2FFurniture%2F11?alt=media&token=3145f0e7-c552-4ecd-ae0c-a79ce0259c66"
//        val url = urt.text.toString()
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

    override fun onItemClick(position: Int) {
        toast("Normal click at position $position", Toast.LENGTH_SHORT)
    }

    override fun onDownloadClick(position: Int){
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

    override fun onDeleteClick(position: Int) {

    }



    fun ItemDetailActivity.toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}