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

class DetailSlide() : AppCompatActivity(), DetailSliderAdapter.OnItemClickerListener {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var downloadurl :String = ""

    lateinit var mSlideViewPager : ViewPager

    var uploads: ArrayList<Item> = ArrayList()
    var categoryUploads: ArrayList<Category> = ArrayList()
    var userUploads: ArrayList<User> = ArrayList()

    lateinit var Detail_userId: String
    lateinit var Detail_familyId: String
    lateinit var path: String
    lateinit var path_category: String
    lateinit var path_user: String
    lateinit var databaseReference: DatabaseReference
    lateinit var databaseReference_category: DatabaseReference
    lateinit var databaseReference_user: DatabaseReference

    lateinit var dbListener: ValueEventListener
    lateinit var dbListener_category: ValueEventListener
    lateinit var dbListener_user: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_background)

        // StrictMode for share
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        mSlideViewPager = findViewById<ViewPager>(R.id.detail_slideViewPager)
        var sliderAdapter = DetailSliderAdapter(uploads,this)
        mSlideViewPager.adapter = sliderAdapter

        //set Current page
        val position_list = intent.getStringExtra("PositionList").toInt()
        mSlideViewPager.setCurrentItem(position_list)

        // set adapter listener for click action
        sliderAdapter.listener = this@DetailSlide

        // set database reference for items and categories
        Detail_userId= intent.getStringExtra("UserID")

        Log.d("gootUserId", Detail_userId)

        path_user = "Users"
        databaseReference_user = FirebaseDatabase.getInstance().getReference(path_user)

        databaseReference = FirebaseDatabase.getInstance().getReference("")
        databaseReference_category = FirebaseDatabase.getInstance().getReference("")

        // set current Item position
        var alreadySet = false

        // listener for user on firebase, realtime change
        dbListener_user = databaseReference_user.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("excution flag", "sadfasfsdgdfhdfhfg")
                // clear it before filling it
                userUploads.clear()

                // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                p0.children.forEach {
                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                    val currUpload = it.getValue(User::class.java) as User
                    Log.d("uploaduuuserkey", it.key)
                    Log.d("uploaduuuseruserid", Detail_userId)
                    if (it.key == Detail_userId){
                        Log.d("uploaduuuserOK", it.key)
                        Detail_familyId = currUpload.familyId
                        path = "Family" + "/" + Detail_familyId + "/" + "items"
                        path_category = "Family" + "/" + Detail_familyId + "/" + "categories"
                        databaseReference = FirebaseDatabase.getInstance().getReference(path)
                        databaseReference_category = FirebaseDatabase.getInstance().getReference(path_category)
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
                                    currUpload.key = it.key

                                    // add to view if user has access
                                    Log.d("categgggcategoryUple", categoryUploads.size.toString())
                                    Log.d("categgggcurkey", currUpload.key)
                                    Log.d("categgggcurrUploaitme", currUpload.itemName.toString())
                                    Log.d("categgggcategoryUeys", categoryUploads[0].itemKeys.toString())
                                    Log.d("categgggcurrUplblic", currUpload.isPublic.toString())
                                    if (categoryUploads.size != 0){
                                        Log.d("categggg sizeOK", categoryUploads.size.toString())
                                        if (currUpload.key in categoryUploads[0].itemKeys){
                                            Log.d("categggg curkeyOK", currUpload.key)
                                            if (currUpload.isPublic) {
                                                Log.d("categggg isPublic", currUpload.isPublic.toString())
                                                uploads.add(currUpload)
                                            } else if (currUpload.itemOwnerUID == Detail_userId){
                                                Log.d("categgggnotPublicisOwn", currUpload.itemOwnerUID)
                                                uploads.add(currUpload)
                                            } else {
                                                Log.d("categggg notPublicOwner", currUpload.itemOwnerUID)
                                            }
                                        }
                                        Log.d("category keys", categoryUploads[0].itemKeys.toString())
                                    } else {
                                        toast(0.toString(), Toast.LENGTH_SHORT)
                                        Log.d("category keys", 55555555.toString())
                                    }
//                    uploads.add(currUpload)
                                }
                                Log.d("upload",uploads.size.toString())

                                // It would update recycler after loading image from firebase storage
                                sliderAdapter.notifyDataSetChanged()

                                // set Current Item Position in View Page
                                if (uploads.size > 0) {
                                    if (!alreadySet){
                                        mSlideViewPager.setCurrentItem(position_list)
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
        this.downloadurl = items[position].imageURLs[0]
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
        this.downloadurl = items[position].imageURLs[0]
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

    override fun onItemClick(position: Int, items:ArrayList<Item>) {
        val intent = Intent(this, DImageSlide::class.java)
        intent.putExtra("ItemKey", items[position].key)
        intent.putExtra("FamilyId", Detail_familyId)
        this.startActivity(intent)
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