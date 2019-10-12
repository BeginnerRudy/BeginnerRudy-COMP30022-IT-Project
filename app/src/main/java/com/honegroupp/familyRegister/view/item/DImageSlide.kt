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
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slide_dimage_background.*
import java.io.File
import java.io.FileOutputStream

class DImageSlide : AppCompatActivity(), DImageSliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadUrl :String = ""

    private lateinit var mSlideViewPager : ViewPager
    
    private lateinit var mDotLayout: LinearLayout
    private lateinit var mDots: Array<TextView?>
    private var numDots: Int = 0
    
    private lateinit var databaseReferenceItem: DatabaseReference
    private lateinit var dbListenerItem: ValueEventListener

    private var currPosition: Int = 0

    private var itemUrls: ArrayList<String> = ArrayList()

    override fun onResume() {
        super.onResume()
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

        setContentView(R.layout.slide_dimage_background)

        // StrictMode for share
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // get position of item clicked in item detail for setting Current page item
        currPosition = intent.getStringExtra("PositionDetail").toInt()

        // set database reference for itemUrls
        val dImageFamilyId= intent.getStringExtra("FamilyId").toString()
        val dImageItemKey= intent.getStringExtra("ItemKey").toString()

        // path of item
        val pathItem = "Family/$dImageFamilyId/items"
        databaseReferenceItem = FirebaseDatabase.getInstance().getReference(pathItem)

        // adapter of itemUrls for ViewPager, set listener in adapter for listening click action
        mSlideViewPager = findViewById(R.id.dimage_slideViewPager)
        val dImageSliderAdapter = DImageSliderAdapter(itemUrls)
        mSlideViewPager.adapter = dImageSliderAdapter
        dImageSliderAdapter.listener = this@DImageSlide

        // layout for bottom dots
        mDotLayout = findViewById(R.id.dimage_dotsLayout)

        // whether bottom dots are already set, View Pager pages cannot be set until it is ready
        var alreadySet = false

        // listener for items on database, realtime change itemUrls
        dbListenerItem = databaseReferenceItem.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                itemUrls.clear()
                
                // Retrieve each Item from database from pathItem
                p0.children.forEach {
                    val currUpload = it.getValue(Item::class.java) as Item

                    // find the item by Item Key, put all detail url into itemUrls
                    if (it.key == dImageItemKey){
                        for (currUrl in currUpload.imageURLs){
                            itemUrls.add(currUrl)
                        }
                    }
                }

                // Notify ViewPager to update
                dImageSliderAdapter.notifyDataSetChanged()

                // click on share
                dimage_share.setOnClickListener(){
                    onShareClick()
                }

                // click on download
                dimage_download.setOnClickListener(){
                    onDownloadClick()
                }

                // set number of dots to be appeared at bottom
                numDots = itemUrls.size

                // only need to set initial dot indicator one time once the itemUrls(urls) is got from database
                if (itemUrls.size > 0){
                    if (!alreadySet){
                        mSlideViewPager.currentItem = currPosition
                        addDotsIndicator(currPosition)
                        alreadySet = true
                    } else{
                        addDotsIndicator(this@DImageSlide.currPosition)
                    }
                }
            }
        })

        // mSlideViewPager page change listener
        mSlideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                addDotsIndicator(position)
                this@DImageSlide.currPosition = position
            }

        })
}

    /**
     * add dots on page in mDotLayout according to position
     * code change from:
     * https://www.youtube.com/watch?v=byLKoPgB7yA&t=847s
     */
    private fun addDotsIndicator(position: Int){
        mDots = arrayOfNulls(numDots)
        mDotLayout.removeAllViews()
        if (numDots > 0) {
            for (i in 0 until numDots) {
                mDots[i] = TextView(this)
                mDots[i]?.text = Html.fromHtml("&#8226;")
                mDots[i]?.textSize = 20.toFloat()
                mDots[i]?.setTextColor(resources.getColor(R.color.colorTransparentWhite))

                mDotLayout.addView(mDots[i])

            }

            if(position <= mDots.size-1){
                mDots[position]?.setTextColor(resources.getColor(R.color.colorWhite))
            } else {
                mDots[mDots.size-1]?.setTextColor(resources.getColor(R.color.colorWhite))
            }
        }
    }

    /**
     * share use Bitmap from ImageVIew
     * code change from:
     * https://www.youtube.com/watch?v=1tpc3fyEObI&t=2s
     */
    @SuppressLint("SetWorldReadable")
    private fun onShareClick() {
        this.downloadUrl = itemUrls[mSlideViewPager.currentItem]
        lateinit var bitmapShare : Bitmap
        Picasso.get().load(downloadUrl).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmapShare = bitmap as Bitmap
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
        try {
            val file = File(this.externalCacheDir,"fml_rgst_share.png")
            val fOut = FileOutputStream(file)
            bitmapShare.compress(Bitmap.CompressFormat.PNG, 100, fOut)
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
     * code from:
     * https://stackoverflow.com/questions/14492354/create-bitmap-from-view-makes-view-disappear-how-to-get-view-canvas
     */
    // share use Bitmap from ImageVIew
    private fun getBitmapFromView(view: View ): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas)
        }   else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun onDownloadClick() {
        this.downloadUrl = itemUrls[mSlideViewPager.currentItem]
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

    //download the image to local album on the device
    private fun startDownloading() {
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
                    makeText(this,"Permission Denied", LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReferenceItem.removeEventListener(dbListenerItem)
    }

    private fun toast(msg: String, duration: Int) {
        makeText(this, msg, duration).show()
    }
}