package com.honegroupp.familyRegister.view.item

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import java.io.File
import java.io.FileOutputStream

class DetailSlide : AppCompatActivity(), DetailSliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadUrl :String = ""

    lateinit var mSlideViewPager : ViewPager

    lateinit var detailUserId: String
    lateinit var detailFamilyId: String

    lateinit var pathItem: String
    lateinit var pathCategory: String
    private lateinit var pathUser: String

    lateinit var databaseReferenceItem: DatabaseReference
    lateinit var databaseReferenceCategory: DatabaseReference
    lateinit var databaseReferenceUser: DatabaseReference
    lateinit var dbListenerItem: ValueEventListener
    lateinit var dbListenerCategory: ValueEventListener
    lateinit var dbListenerUser: ValueEventListener

    var itemUploads: ArrayList<Item> = ArrayList()
    var categoryUploads: ArrayList<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        setContentView(R.layout.slide_background)

        // StrictMode for share
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // adapter of items for ViewPager, set listener in adapter for listening click action
        mSlideViewPager = findViewById(R.id.detail_slideViewPager)
        val sliderAdapter = DetailSliderAdapter(itemUploads,this)
        mSlideViewPager.adapter = sliderAdapter
        sliderAdapter.listener = this@DetailSlide

        // get position of item clicked in item list for setting Current page item
        val positionList = intent.getStringExtra("PositionList").toInt()

        // get userID for setting firbase database reference for items and categories
        detailUserId= intent.getStringExtra("UserID")

        // get position of current category for setting Current page item
        val categoryIndexList= intent.getStringExtra("CategoryNameList").toInt()


        // initialise database References, Item and Categories path cannot be get before the family id is get
        pathUser = "Users"
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference(pathUser)
        databaseReferenceItem = FirebaseDatabase.getInstance().getReference("")
        databaseReferenceCategory = FirebaseDatabase.getInstance().getReference("")

        // whether item position is already set, View Pager pages cannot be set until it is ready
        var alreadySet = false

        // listener for user on firebase, realtime change familyID(detailFamilyId)
        dbListenerUser = databaseReferenceUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {

                // Retrieve each User from database from pathUser
                p0.children.forEach { it ->
                    val currUserUpload = it.getValue(User::class.java) as User
                    
                    // find the user by UserId
                    // , get all detail url into itemUrls
                    if (it.key == detailUserId){
                        // get familyID to produce path of Item & path of Category
                        detailFamilyId = currUserUpload.familyId
                        pathItem = "Family/$detailFamilyId/items"
                        pathCategory = "Family/$detailFamilyId/categories"

                        // database Reference for Item & Category
                        databaseReferenceItem = FirebaseDatabase.getInstance().getReference(pathItem)
                        databaseReferenceCategory = FirebaseDatabase.getInstance().getReference(pathCategory)

                        // listener for category on firebase, realtime change categories(categoryUploads)
                        dbListenerCategory = databaseReferenceCategory.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                toast(p0.message, Toast.LENGTH_SHORT)
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                categoryUploads.clear()

                                // get all categories and put into categories(categoryUploads)
                                p0.children.forEach {
                                    val currCategoryUpload = it.getValue(Category::class.java) as Category
                                    categoryUploads.add(currCategoryUpload)
                                }

                                // Notify ViewPager to update
                                sliderAdapter.notifyDataSetChanged()
                            }
                        })

                        // listener for items on firebase, realtime change items(itemUploads)
                        dbListenerItem = databaseReferenceItem.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                toast(p0.message, Toast.LENGTH_SHORT)
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                itemUploads.clear()

                                // get all items and put into items(itemUploads) if user has access
                                p0.children.forEach {
                                    val currItemUpload = it.getValue(Item::class.java) as Item
                                    currItemUpload.key = it.key

                                    // wait for categories(categoryUploads) is get from database
                                    if (categoryUploads.size != 0){
                                        // check item in current category
                                        if (currItemUpload.key in categoryUploads[categoryIndexList].itemKeys){
                                            // check item is visible, if not check user is owner
                                            if (currItemUpload.isPublic) {
                                                itemUploads.add(currItemUpload)
                                            } else if (currItemUpload.itemOwnerUID == detailUserId){
                                                itemUploads.add(currItemUpload)
                                            }
                                        }
                                    }
                                }

                                // Notify ViewPager to update
                                sliderAdapter.notifyDataSetChanged()

                                // set Item to be seen first in View Page when items(itemUploads) is ready
                                if (itemUploads.size > 0) {
                                    if (!alreadySet){
                                        mSlideViewPager.currentItem = positionList
                                        alreadySet = true
                                    }
                                }
                            }
                        })
                    }
                }

                // It would update recycler after loading image from firebase storage
                sliderAdapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * share use Bitmap from ImageVIew
     * code change from:
     * https://www.youtube.com/watch?v=1tpc3fyEObI&t=2s
     */
    @SuppressLint("SetWorldReadable")
    override fun onShareClick(position: Int, items:ArrayList<Item>, imageView: ImageView) {
        this.downloadUrl = items[position].imageURLs[0]
        val bitmap = getBitmapFromView(imageView)
        try {
            val file = File(this.externalCacheDir,"fml_rgst_share.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            file.setReadable(true, false)
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_TEXT, "name")
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            intent.type = "image/png"
            startActivity(Intent.createChooser(intent, "Share image via"))
        } catch (e: Exception ) {
            e.printStackTrace()
        }
    }

    /**
     * share use Bitmap from ImageVIew
     * code change from:
     * https://stackoverflow.com/questions/14492354/create-bitmap-from-view-makes-view-disappear-how-to-get-view-canvas
     */
    private fun getBitmapFromView(view: View ): Bitmap {
        val returnedBitmap: Bitmap = Bitmap.createBitmap(view.width, view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    // download when click
    override fun onDownloadClick(position: Int, items: ArrayList<Item>) {
        this.downloadUrl = items[position].imageURLs[0]
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){

                //permission denied
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
            }else{
                //permission already granted
                startDownloading()

            }
        }else{
            //system os less than mashmallow
            startDownloading()
        }
    }

    //download the image to local album of the device
    private fun startDownloading() {
        Log.d("SAVEinging","")
        //download request
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
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

    // open Detail Image page when image is clicked
    override fun onItemClick(position: Int, items:ArrayList<Item>) {
        val intent = Intent(this, DImageSlide::class.java)
        intent.putExtra("ItemKey", items[position].key)
        intent.putExtra("FamilyId", detailFamilyId)
        this.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReferenceItem.removeEventListener(dbListenerItem)
        databaseReferenceCategory.removeEventListener(dbListenerCategory)
    }

    fun toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}