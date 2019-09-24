package com.honegroupp.familyRegister.view.item

import android.Manifest
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
    lateinit var pathUser: String

    lateinit var databaseReferenceItem: DatabaseReference
    lateinit var databaseReferenceCategory: DatabaseReference
    lateinit var databaseReferenceUser: DatabaseReference
    lateinit var dbListenerItem: ValueEventListener
    lateinit var dbListenerCategory: ValueEventListener
    lateinit var dbListenerUser: ValueEventListener

    var itemUploads: ArrayList<Item> = ArrayList()
    var categoryUploads: ArrayList<Category> = ArrayList()
    var userUploads: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_background)

        // StrictMode for share
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // adapter of items for ViewPager, set listener in adapter for listening click action
        mSlideViewPager = findViewById<ViewPager>(R.id.detail_slideViewPager)
        var sliderAdapter = DetailSliderAdapter(itemUploads,this)
        mSlideViewPager.adapter = sliderAdapter
        sliderAdapter.listener = this@DetailSlide

        // get position of item clicked in item list for setting Current page item
        val positionList = intent.getStringExtra("PositionList").toInt()

        // get userID for setting firbase database reference for items and categories
        detailUserId= intent.getStringExtra("UserID")


        val categoryIndexList= intent.getStringExtra("CategoryNameList").toInt()
        Log.d("deeetailpathlist", categoryIndexList.toString())


        // initialise database References, Item and Categories path cannot be get before the family id is get
        pathUser = "Users"
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference(pathUser)
        databaseReferenceItem = FirebaseDatabase.getInstance().getReference("")
        databaseReferenceCategory = FirebaseDatabase.getInstance().getReference("")

        // whether item position is already set, View Pager pages cannot be set until it is ready
        var alreadySet = false

        // listener for user on firebase, realtime change
        dbListenerUser = databaseReferenceUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                // clear it before filling it
                userUploads.clear()

                // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                p0.children.forEach {
                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                    val currUpload = it.getValue(User::class.java) as User
                    Log.d("uploaduuuserkey", it.key)
                    Log.d("uploaduuuseruserid", detailUserId)
                    if (it.key == detailUserId){
                        Log.d("uploaduuuserOK", it.key)
                        detailFamilyId = currUpload.familyId
                        pathItem = "Family" + "/" + detailFamilyId + "/" + "items"
                        pathCategory = "Family" + "/" + detailFamilyId + "/" + "categories"
                        databaseReferenceItem = FirebaseDatabase.getInstance().getReference(pathItem)
                        databaseReferenceCategory = FirebaseDatabase.getInstance().getReference(pathCategory)
                        // listener for category on firebase, realtime change
                        dbListenerCategory = databaseReferenceCategory.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                toast(p0.message, Toast.LENGTH_SHORT)
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                Log.d("excution flag", "sadfasfsdgdfhdfhfg")
                                // clear it before filling it
                                categoryUploads.clear()

                                // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                                p0.children.forEach {
                                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                                    val currUpload = it.getValue(Category::class.java) as Category

                                    categoryUploads.add(currUpload)
                                    Log.d("category keys", currUpload.itemKeys.toString())
                                }
                                Log.d("uploadcategory",categoryUploads.size.toString())

                                // It would update recycler after loading image from firebase storage
                                sliderAdapter.notifyDataSetChanged()
                            }
                        })

                        // listener for items on firebase, realtime change
                        dbListenerItem = databaseReferenceItem.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                toast(p0.message, Toast.LENGTH_SHORT)
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                Log.d("excution flag", "sadfasfsdgdfhdfhfg")
                                // clear it before filling it
                                itemUploads.clear()

                                // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                                p0.children.forEach {
                                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                                    val currUpload = it.getValue(Item::class.java) as Item
                                    currUpload.key = it.key

                                    // add to view if user has access
                                    Log.d("categgggcategoryUple", categoryUploads.size.toString())
                                    Log.d("categgggcurkey", currUpload.key)
                                    Log.d("categgggcurrUploaitme", currUpload.itemName.toString())
                                    Log.d("categgggcategoryUeys", categoryUploads[categoryIndexList].itemKeys.toString())
                                    Log.d("categgggcurrUplblic", currUpload.isPublic.toString())
                                    if (categoryUploads.size != 0){
                                        Log.d("categggg sizeOK", categoryUploads.size.toString())
                                        if (currUpload.key in categoryUploads[categoryIndexList].itemKeys){
                                            Log.d("categggg curkeyOK", currUpload.key)
                                            if (currUpload.isPublic) {
                                                Log.d("categggg isPublic", currUpload.isPublic.toString())
                                                itemUploads.add(currUpload)
                                            } else if (currUpload.itemOwnerUID == detailUserId){
                                                Log.d("categgggnotPublicisOwn", currUpload.itemOwnerUID)
                                                itemUploads.add(currUpload)
                                            } else {
                                                Log.d("categggg notPublicOwner", currUpload.itemOwnerUID)
                                            }
                                        }
                                        Log.d("category keys", categoryUploads[categoryIndexList].itemKeys.toString())
                                    } else {
                                        toast(0.toString(), Toast.LENGTH_SHORT)
                                        Log.d("category keys", 55555555.toString())
                                    }
//                    itemUploads.add(currUpload)
                                }
                                Log.d("upload",itemUploads.size.toString())

                                // It would update recycler after loading image from firebase storage
                                sliderAdapter.notifyDataSetChanged()

                                // set Current Item Position in View Page
                                if (itemUploads.size > 0) {
                                    if (!alreadySet){
                                        mSlideViewPager.setCurrentItem(positionList)
                                        alreadySet = true
                                    }
                                }
                            }
                        })
                    }
                    userUploads.add(currUpload)
                }
                Log.d("uploaduuuser",userUploads.size.toString())

                // It would update recycler after loading image from firebase storage
                sliderAdapter.notifyDataSetChanged()
            }
        })
    }

//    fun setCurrentItemPosition(position: Int, alreadySet: Boolean) : Boolean{
//        if (!alreadySet){
//            mSlideViewPager.setCurrentItem(position)
//            return true
//        }
//        return true
//    }


    // share when click
    override fun onShareClick(position: Int, items:ArrayList<Item>, imageView: ImageView) {
        this.downloadUrl = items[position].imageURLs[0]
        var bitmap = getBitmapFromView(imageView);
        try {
            var file = File(this.getExternalCacheDir(),"fml_rgst_share.png");
            var fOut = FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            var intent = Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, "name");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
            Log.d("sharingactivity",position.toString())
        } catch (e: Exception ) {
            e.printStackTrace();
        }
    }

    // share use Bitmap from ImageVIew
    fun getBitmapFromView(view: View ): Bitmap {
        var returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        var canvas = Canvas(returnedBitmap);
        var bgDrawable = view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    // download when click
    override fun onDownloadClick(position: Int, items: ArrayList<Item>) {
        this.downloadUrl = items[position].imageURLs[0]
        Log.d("dowloding1111","")
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
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

    //download the image to local album of the device
    private fun startDownloading() {
        Log.d("SAVEinging","")
//        val url = "https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/1%2FFurniture%2F11?alt=media&token=3145f0e7-c552-4ecd-ae0c-a79ce0259c66"
//        val url = urt.text.toString()
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