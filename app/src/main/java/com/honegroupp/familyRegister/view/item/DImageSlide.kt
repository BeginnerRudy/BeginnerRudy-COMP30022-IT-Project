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
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import java.io.File
import java.io.FileOutputStream

class DImageSlide() : AppCompatActivity(), DImageSliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl :String = ""

    lateinit var mSlideViewPager : ViewPager
    lateinit var mDotLayout: LinearLayout

    lateinit var mDots: Array<TextView?>
    var numDots: Int = 0

    var uploads: ArrayList<String> = ArrayList()

    lateinit var path_DImage_familyId: String
    lateinit var path_DImage_itemKey: String
    lateinit var path: String
    lateinit var databaseReference: DatabaseReference
    lateinit var dbListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_dimage_background)

        // set database reference for items
        path_DImage_familyId= intent.getStringExtra("FamilyId")
        path_DImage_itemKey= intent.getStringExtra("ItemKey")
        path = "Family" + "/" + path_DImage_familyId + "/" + "items"
        databaseReference = FirebaseDatabase.getInstance().getReference(path)


        mSlideViewPager = findViewById<ViewPager>(R.id.dimage_slideViewPager)
        mDotLayout = findViewById<LinearLayout>(R.id.dimage_dotsLayout)

        var DImageSliderAdapter = DImageSliderAdapter(uploads,this)
        mSlideViewPager.adapter = DImageSliderAdapter
        DImageSliderAdapter.listener = this@DImageSlide


        var alreadySet = false

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
                    // Retrieve data from database, create an Item object and store in the list of one ItemListAdapter
                    val currUpload = it.getValue(Item::class.java) as Item
                    currUpload.key = it.key

                    if (it.key == path_DImage_itemKey){
                        for (currUrl in currUpload.imageURLs){
                            uploads.add(currUrl)
                        }
                    }

                }
                Log.d("upload",uploads.size.toString())

                // It would update recycler after loading image from firebase storage
                DImageSliderAdapter.notifyDataSetChanged()

                // set number of dots
                numDots = uploads.size

                if (uploads.size > 0){
                    if (!alreadySet){
                        Log.d("ddddiimageadddot",uploads.size.toString())
                        addDotsIndicator(0)
                        alreadySet = true
                    }
                }
            }

        })

        var listener = mSlideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                addDotsIndicator(position)
            }

        })
}

    fun addDotsIndicator(position: Int){
        mDots = arrayOfNulls<TextView>(numDots)
        mDotLayout.removeAllViews()
        if (numDots > 0) {
            for (i in 0..numDots-1) {
                mDots[i] = TextView(this)
                mDots[i]?.setText(Html.fromHtml("&#8226;"))
                mDots[i]?.setTextColor(getResources().getColor(R.color.colorTransparentWhite))

                mDotLayout.addView(mDots[i])

            }

            if(mDots.size > 0){
                mDots[position]?.setTextColor(getResources().getColor(R.color.colorWhite))
            }
        }
    }

    override fun onShareClick(position: Int, item:ArrayList<String>, imageView: ImageView) {
        this.downloadurl = item[position]
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

    fun getBitmapFromView(view: View ): Bitmap {
        var returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        var canvas = Canvas(returnedBitmap);
        var bgDrawable = view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    override fun onDownloadClick(position: Int, item: ArrayList<String>) {
        this.downloadurl = item[position]
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