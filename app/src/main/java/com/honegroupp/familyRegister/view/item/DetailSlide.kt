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
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.Item
import java.io.File
import java.io.FileOutputStream

class DetailSlide() : AppCompatActivity(), DetailSliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl :String = ""

    lateinit var mSlideViewPager : ViewPager

    val userId = "zengbinz@student=unimelb=edu=au"
    val path = "Family" + "/" + userId + "/" + "items"
    val path_category = "Family" + "/" + userId + "/" + "categories"

    var uploads: ArrayList<Item> = ArrayList()
    var categoryUploads: ArrayList<Category> = ArrayList()

    val databaseReference = FirebaseDatabase.getInstance().getReference(path)
    val databaseReference_category = FirebaseDatabase.getInstance().getReference(path_category)

    lateinit var dbListener: ValueEventListener
    lateinit var dbListener_category: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_background)

        // StrictMode for share
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        mSlideViewPager = findViewById<ViewPager>(R.id.detail_slideViewPager)
        var sliderAdapter = DetailSliderAdapter(uploads,this)
        mSlideViewPager.adapter = sliderAdapter

        // set adapter listener for click action
        sliderAdapter.listener = this@DetailSlide

        // listener for category on firebase, realtime change
        dbListener_category = databaseReference_category.addValueEventListener(object : ValueEventListener {
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
                    val currUpload = it.getValue(Item::class.java) as Item

                    // add to view if user has access
                    if (categoryUploads.size != 0){
                        if (currUpload.itemName in categoryUploads[0].itemKeys){
                            if (currUpload.visibility) {
                                uploads.add(currUpload)
                            } else if (currUpload.itemOwnerUID == userId){
                                uploads.add(currUpload)
                            }
                        }
                        toast(categoryUploads[0].itemKeys.toString(), Toast.LENGTH_SHORT)
                        Log.d("category keys", categoryUploads[0].itemKeys.toString())
                    } else {
                        toast(0.toString(), Toast.LENGTH_SHORT)
                        Log.d("category keys", 55555555.toString())
                    }

                }
                Log.d("upload",uploads.size.toString())

                // It would update recycler after loading image from firebase storage
                sliderAdapter.notifyDataSetChanged()
            }
        })
    }


    // share when click
    override fun onShareClick(position: Int, item:ArrayList<Item>, imageView: ImageView) {
        this.downloadurl = item[position].imageURLs[0]
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
    override fun onDownloadClick(position: Int, item: ArrayList<Item>) {
        this.downloadurl = item[position].imageURLs[0]
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

        //Download request
        val request = DownloadManager.Request(Uri.parse(downloadurl))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        //path of file
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM+"/FamilyRegister","${System.currentTimeMillis()}")

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
        databaseReference_category.removeEventListener(dbListener_category)
    }

    fun toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}