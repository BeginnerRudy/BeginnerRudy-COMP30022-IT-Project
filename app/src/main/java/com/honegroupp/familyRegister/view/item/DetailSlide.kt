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
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

class DetailSlide : AppCompatActivity(), DetailSliderAdapter.OnItemClickerListener{

    lateinit var shareImgView: ImageView
    private var downloadUrl :String = ""

    private val STORAGE_PERMISSION_CODE: Int = 1000

    lateinit var mSlideViewPager : ViewPager
    lateinit var sliderAdapter: DetailSliderAdapter

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

    // open Detail Image page when image is clicked
    override fun onItemClick(position: Int) {
        val intent = Intent(this, DImageSlide::class.java)
        intent.putExtra("ItemKey", itemUploads[mSlideViewPager.currentItem].key)
        intent.putExtra("FamilyId", detailFamilyId)
        this.startActivity(intent)
    }

    // start editing
    override fun onEditClick(itemKey: String?) {
        val intent = Intent(this, ItemEdit::class.java)
        intent.putExtra("ItemKey", itemKey)
        intent.putExtra("FamilyId", detailFamilyId)

        this.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
    }

    fun

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
        sliderAdapter = DetailSliderAdapter(itemUploads,this)
        mSlideViewPager.adapter = sliderAdapter
        sliderAdapter.listener = this@DetailSlide

        // get position of item clicked in item list for setting Current page item
        val positionList = intent.getStringExtra("PositionList").toInt()

        // get userID for setting firbase database reference for items and categories
        detailUserId= intent.getStringExtra("UserID")

        // get position of current category for setting Current page item
        val categoryIndexList= intent.getStringExtra("CategoryNameList").toInt()

        // get position of current category for setting Current page item
        detailFamilyId= intent.getStringExtra("FamilyId")


        // initialise database References, Item and Categories path cannot be get before the family id is get
        pathUser = "Users"
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference(pathUser)
        databaseReferenceItem = FirebaseDatabase.getInstance().getReference("")
        databaseReferenceCategory = FirebaseDatabase.getInstance().getReference("")

        // whether item position is already set, View Pager pages cannot be set until it is ready
        var alreadySet = false

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
                sliderAdapter.notifyDataSetChanged()
            }
        })

        // listener for items on firebase, realtime change items(itemUploads)
        dbListenerItem = databaseReferenceItem.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("ooonDataChange","cccccgne")
                itemUploads.clear()
                sliderAdapter.notifyDataSetChanged()

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
                                Log.d("ooonDataChangeItem",currItemUpload.itemName)
                                itemUploads.add(currItemUpload)
                            } else if (currItemUpload.itemOwnerUID == detailUserId){
                                Log.d("ooonDataChangeItem",currItemUpload.itemName)
                                itemUploads.add(currItemUpload)
                            }
                        }
                    }
                }

                // Notify ViewPager to update
                Log.d("ooonDataChangeNotifyyy","NOty")
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



    /**
     * share use Bitmap from ImageVIew
     * code change from:
     * https://www.youtube.com/watch?v=1tpc3fyEObI&t=2s
     */
    override fun onShareClick(imageView: ImageView) {
        var bitmap = getBitmapFromView(imageView);
        try {
            var file = File(this.getExternalCacheDir(),"logicchip.png");
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
        } catch (e: Exception ) {
            e.printStackTrace();
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

    // download when click in menu
    override fun onDownloadClick(position: Int) {
        downloadUrl = itemUploads[mSlideViewPager.currentItem].imageURLs[position]
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



    override fun onDestroy() {
        super.onDestroy()
        databaseReferenceItem.removeEventListener(dbListenerItem)
        databaseReferenceCategory.removeEventListener(dbListenerCategory)
    }

    fun toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}