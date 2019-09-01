package com.example.familyRegister.core

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat.JPEG
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyRegister.model.ItemUpload
import com.example.familyRegister.R
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_item_list.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.annotation.Target as Target1

class ItemListActivity : AppCompatActivity(), ItemListAdapter.OnItemClickerListener{
    lateinit var itemListAdapter: ItemListAdapter
    lateinit var path: String
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var dbListener: ValueEventListener
    lateinit var databaseReference: DatabaseReference
    var itemUploads: ArrayList<ItemUpload> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        path = RegisterFragment.uid + "/" + intent.getStringExtra("categoryPath") + "/"

        databaseReference = FirebaseDatabase.getInstance().getReference(path)
        // Setting the recycler view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        Log.d("RRR","Running")
        // setting one ItemListAdapter
        itemListAdapter = ItemListAdapter(itemUploads, this@ItemListActivity)
        recycler_view.adapter = itemListAdapter
        itemListAdapter.listener = this@ItemListActivity


        dbListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
                progress_circular.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                // clear it before filling it
                itemUploads.clear()

                p0.children.forEach {
                    // Retrieve data from database, create an ItemUpload object and store in the list of one ItemListAdapter
                    val currUpload = it.getValue(ItemUpload::class.java) as ItemUpload
                    currUpload.key = it.key
                    itemUploads.add(currUpload)
                }

                // It would update recycler after loading image from firebase storage
                itemListAdapter.notifyDataSetChanged()
                progress_circular.visibility = View.INVISIBLE
            }


        })

        // setting for the btn_add
        btn_add.setOnClickListener {
            openDialog()
            toast("Hah", Toast.LENGTH_SHORT)
        }
    }

    private fun openDialog() {
        val addImageDialog = AddImageDialog(path)
        addImageDialog.show(this@ItemListActivity.supportFragmentManager, "add new item image")
    }
    override fun onSaveClick(position: Int) {
        toast("Save click at position $position", Toast.LENGTH_SHORT)
        var imageurl2 = path + "/" + itemUploads[position].key.toString()
        Log.d("URL2",imageurl2.toString())
        var imageurl = "https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/11%2FFurniture%2F1?alt=media&token=4f48b8b8-60b6-4c3a-93bc-47ad6d8eb9a5"
        Picasso.get().load(imageurl).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                //save to album
                try {
                    Log.d("SAVE11111111",11111111111.toString())
                    val root = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/"
                    var myDir = File("$root")
                    Log.d("SAVE22222222",myDir.toString())
                    if (!myDir.exists()) {
                        myDir.mkdirs()
                    }
                    val name = "myimage.jpg"
                    myDir = File(myDir, name)
                    Log.d("SAVE333333333",myDir.toString())
                    val out = FileOutputStream(myDir)
                    Log.d("SAVE3333333555", myDir.toString())
                    Log.d("SAVE3333333555", bitmap.toString())
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    Log.d("SAVE444444444444",myDir.toString())
                    out.flush()
                    Log.d("SAVE555555555",myDir.toString())
                    out.close()
                    baseContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(myDir.absolutePath)))
                    Log.d("SAVE666666666666",myDir.toString())
                    Log.d("SAVE_IMAGE",myDir.absolutePath.toString())
                } catch (e: Exception) {
                    // some action
                }
            }
        })

    }

    override fun onItemClick(position: Int) {
        toast("Normal click at position $position", Toast.LENGTH_SHORT)
        val intent = Intent(this, ItemDetailActivity()::class.java)
        intent.putExtra("int_key", position)
        intent.putExtra("itemPath", path + "/" + itemUploads[position].key.toString())
        startActivity(intent)
    }

    override fun onWhatEverClick(position: Int) {
        toast("WhatEver click at position $position", Toast.LENGTH_SHORT)
    }

    override fun onDeleteClick(position: Int) {
        toast("Delete click at position $position", Toast.LENGTH_SHORT)

        val selectedItem = itemUploads[position]
        val selectedKey = selectedItem.key as String
        val imageRef = storage.getReferenceFromUrl(selectedItem.url)
        // Delete image and its tile from Fitrbase Storage
        imageRef.delete()
            .addOnSuccessListener {
                // Delete image title and its image url from Firebase Real-time Database
                databaseReference.child(selectedKey).removeValue()
                toast("Item deleted", Toast.LENGTH_SHORT)
            }
            .addOnFailureListener {
                toast("Failed for deleting the item", Toast.LENGTH_SHORT)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(dbListener)
    }

    fun ItemListActivity.toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}
