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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.ItemU
import java.io.File
import java.io.FileOutputStream

class DetailSlide() : AppCompatActivity(), DetailSliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl :String = ""

    lateinit var mSlideViewPager : ViewPager
    var uploads: ArrayList<ItemU> = ArrayList()
    val path = "CeShi" + "/" + "Furniture" + "/"
    val databaseReference = FirebaseDatabase.getInstance().getReference(path)
    lateinit var dbListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_slide)

        mSlideViewPager = findViewById<ViewPager>(R.id.slideViewPager)
        var mDotLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        var sliderAdapter = DetailSliderAdapter(uploads,this)
        mSlideViewPager.adapter = sliderAdapter
        sliderAdapter.listener = this@DetailSlide

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

    }


    override fun onShareClick(position: Int,item:ArrayList<ItemU>, imageView: ImageView) {
        this.downloadurl = item[position].url
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