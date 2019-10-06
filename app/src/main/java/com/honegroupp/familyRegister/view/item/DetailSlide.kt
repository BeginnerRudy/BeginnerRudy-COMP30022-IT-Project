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
import android.opengl.Visibility
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
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.Item
import kotlinx.android.synthetic.main.slide_background.*
import java.io.File
import java.io.FileOutputStream

class DetailSlide : AppCompatActivity(), DetailSliderAdapter.OnItemClickerListener{
    companion object {
        const val ALL_PAGE_SIGNAL = -1
        const val SHOW_PAGE_SIGNAL = -2
        const val CATEGORY_SIGNAL = 0

        private const val STORAGE_PERMISSION_CODE: Int = 1000
    }

    private var downloadUrl :String = ""

    lateinit var mSlideViewPager : ViewPager
    lateinit var sliderAdapter: DetailSliderAdapter

    lateinit var detailUserId: String
    lateinit var detailFamilyId: String

    lateinit var pathItem: String
    lateinit var databaseReferenceItem: DatabaseReference
    lateinit var dbListenerItem: ValueEventListener
    var itemUploads: ArrayList<Item> = ArrayList()

    var isInCategory: Boolean = false
    lateinit var pathCategory: String
    lateinit var databaseReferenceCategory: DatabaseReference
    lateinit var dbListenerCategory: ValueEventListener
    var categoryUploads: ArrayList<Category> = ArrayList()

    var storage: FirebaseStorage = FirebaseStorage.getInstance()



    // open Detail Image page when image is clicked
    override fun onItemClick(position: Int) {
        val intent = Intent(this, DImageSlide::class.java)
        intent.putExtra("PositionDetail", position.toString())
        intent.putExtra("ItemKey", itemUploads[mSlideViewPager.currentItem].key)
        intent.putExtra("FamilyId", detailFamilyId)
        this.startActivity(intent)
    }

    override fun onDeleteClick(position: Int) {
        if (itemUploads[mSlideViewPager.currentItem].imageURLs.size > 1){
            // use url create reference of image to be deleted
            val deleteUrl = itemUploads[mSlideViewPager.currentItem].imageURLs[position]
            val imageRef = storage.getReferenceFromUrl(deleteUrl)

            // Delete image and its tile from Fitrbase Storage
            imageRef.delete()
                .addOnSuccessListener {
                    // Delete image url from Firebase Real-time Database
                    removeItemUrl(position)
                    toast(getString(R.string.detail_delete_success), Toast.LENGTH_SHORT)
                }
                .addOnFailureListener {
                    toast(getString(R.string.detail_delete_fail), Toast.LENGTH_SHORT)
                }
        }
    }

    fun removeItemUrl(position: Int){
        itemUploads[mSlideViewPager.currentItem].imageURLs.removeAt(position)
        databaseReferenceItem
            .child(itemUploads[mSlideViewPager.currentItem].key.toString())
            .child("imageURLs")
            .setValue(itemUploads[mSlideViewPager.currentItem].imageURLs)
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

        // hide status bar
        hideStatusBar()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        // hide status bar
        hideStatusBar()
    }

    private fun hideStatusBar(){
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide status bar
        hideStatusBar()

        setContentView(R.layout.slide_background)

        // StrictMode for share
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // get position of item clicked in item list for setting Current page item
        val positionList = intent.getStringExtra("PositionList").toInt()

        // get userID for setting firbase database reference for items and categories
        detailUserId= intent.getStringExtra("UserID")

        // get position of current category for setting Current page item
        val categoryIndexList= intent.getStringExtra("CategoryNameList").toInt()
        if (categoryIndexList >= CATEGORY_SIGNAL ){
            isInCategory = true
        }

        // get position of current category for setting Current page item
        detailFamilyId= intent.getStringExtra("FamilyId")

        Log.d("dddddddpoli", positionList.toString())
        Log.d("ddddddduid", detailUserId.toString())
        Log.d("dddddddcat", categoryIndexList.toString())
        Log.d("ddddddddetai", detailFamilyId.toString())

        // adapter of items for ViewPager, set listener in adapter for listening click action
        mSlideViewPager = findViewById(R.id.detail_slideViewPager)
        sliderAdapter = DetailSliderAdapter(itemUploads, detailUserId, this)
        mSlideViewPager.adapter = sliderAdapter
        sliderAdapter.listener = this@DetailSlide

        // whether item position is already set, View Pager pages cannot be set until it is ready
        var alreadySet = false

        // database Reference for Item
        pathItem = "Family/$detailFamilyId/items"
        databaseReferenceItem = FirebaseDatabase.getInstance().getReference(pathItem)

        // get category information if this activity is activated in category page
        if (isInCategory) {
            // database Reference for Category
            pathCategory = "Family/$detailFamilyId/categories"
            databaseReferenceCategory = FirebaseDatabase.getInstance().getReference(pathCategory)

            // listener for category on firebase, realtime change categories(categoryUploads)
            dbListenerCategory =
                databaseReferenceCategory.addValueEventListener(object : ValueEventListener {
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
        }

        // listener for items on firebase, realtime change items(itemUploads)
        dbListenerItem = databaseReferenceItem.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                itemUploads.clear()
                sliderAdapter.notifyDataSetChanged()

                // get all items and put into items(itemUploads) if user has access
                p0.children.forEach {
                    val currItemUpload = it.getValue(Item::class.java) as Item
                    currItemUpload.key = it.key

                    // wait for categories(categoryUploads) is get from database
                    if (categoryUploads.size != 0 || !isInCategory){
                        if (isInCategory) {
                            // check item in current category
                            if (currItemUpload.key in categoryUploads[categoryIndexList].itemKeys){
                                // check item is visible, if not check user is owner
                                if (currItemUpload.isPublic) {
                                    itemUploads.add(currItemUpload)
                                } else if (currItemUpload.itemOwnerUID == detailUserId){
                                    itemUploads.add(currItemUpload)
                                }
                            }
                        } else if (categoryIndexList == ALL_PAGE_SIGNAL) {
                            Log.d("ccccindexalll", categoryIndexList.toString())
                            itemUploads.add(currItemUpload)
                        } else if (categoryIndexList == SHOW_PAGE_SIGNAL){
                            Log.d("ccccindexssss", categoryIndexList.toString())

                            if (detailUserId in currItemUpload.showPageUids.keys){
                                itemUploads.add(currItemUpload)
                            }
                        }
                    }
                }

                // Notify ViewPager to update
                if (itemUploads.size == 0) {
                    text_view_empty_detail.visibility = View.VISIBLE
                } else {
                    text_view_empty_detail.visibility = View.INVISIBLE
                }
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
    @SuppressLint("SetWorldReadable")
    override fun onShareClick(imageView: ImageView) {
        val bitmap = getBitmapFromView(imageView)
        try {
            val file = File(this.externalCacheDir,"family_remember.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            file.setReadable(true, false)
            val intent = Intent(android.content.Intent.ACTION_SEND)
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
        if (isInCategory){
            databaseReferenceCategory.removeEventListener(dbListenerCategory)
        }
    }

    fun toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}