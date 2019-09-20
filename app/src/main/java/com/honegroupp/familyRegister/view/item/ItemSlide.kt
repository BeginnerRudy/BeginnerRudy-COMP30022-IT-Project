package com.honegroupp.familyRegister.view.item

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.ItemU
import kotlinx.android.synthetic.main.item_slide.view.*
import kotlinx.android.synthetic.main.slide_layout.view.*

class ItemSlide() : AppCompatActivity(), SliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl :String = ""

    lateinit var mSlideViewPager : ViewPager
    var uploads: ArrayList<ItemU> = ArrayList()
    val path = "CeShi" + "/" + "Furniture" + "/"
    val databaseReference = FirebaseDatabase.getInstance().getReference(path)
    lateinit var dbListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_slide)

        mSlideViewPager = findViewById<ViewPager>(R.id.slideViewPager)
        var mDotLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        var sliderAdapter = SliderAdapter(uploads,this)
        mSlideViewPager.adapter = sliderAdapter
        sliderAdapter.listener = this@ItemSlide



        mSlideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                invalidateOptionsMenu()
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        dbListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("excution flag", "sadfasfsdgdfhdfhfg")
                // clear it before filling it
                uploads.clear()

                // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                p0.children.forEach {
                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                    val currUpload = it.getValue(ItemU::class.java) as ItemU
                    currUpload.key = it.key
                    uploads.add(currUpload)
                }
                Log.d("upload",uploads.size.toString())

                // It would update recycler after loading image from firebase storage
                sliderAdapter.notifyDataSetChanged()
            }

        })

//        registerForContextMenu(mSlideViewPager.slideViewPager.slide_image)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.item_detail_menu, menu)
        if (mSlideViewPager.getCurrentItem() === 0) {
            // configure
        } else if (mSlideViewPager.getCurrentItem() === 1) {
            // configure
        } else if (mSlideViewPager.getCurrentItem() === 2) {
            // configure
        } else if (mSlideViewPager.getCurrentItem() === 3) {
            // configure
        }
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onCreateContextMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        menu!!.setHeaderTitle("Choose your option");
//        getMenuInflater().inflate(R.menu.item_detail_menu, menu);
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.option_1 -> {
//                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show()
//                return true
//            }
//            R.id.option_2 -> {
//                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
//                return true
//            }
//            else -> return super.onContextItemSelected(item)
//        }
//    }

    override fun onDeleteClick(position: Int) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

    override fun onDownloadClick(position: Int, item: ArrayList<ItemU>) {
        this.downloadurl = item[position].url
        Log.d("dowloding1111","")
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

    //download the image to local album on the device
    private fun startDownloading() {
        Log.d("SAVEinging","")
//        val url = "https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/1%2FFurniture%2F11?alt=media&token=3145f0e7-c552-4ecd-ae0c-a79ce0259c66"
//        val url = urt.text.toString()
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

    override fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(dbListener)
    }

    fun toast(msg: String, duration: Int) {
        android.widget.Toast.makeText(this, msg, duration).show()
    }
}