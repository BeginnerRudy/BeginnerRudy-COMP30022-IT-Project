package com.example.FamilyRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_images.*

class ImagesActivity : AppCompatActivity(), ImageAdapter.OnItemClickerListener {
    lateinit var imageAdapter: ImageAdapter

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var dbListener: ValueEventListener
    var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(MainActivity.IMAGE_POLDER_PATH)
    var uploads: ArrayList<Upload> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState!=null){

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        // Setting the recycler view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        // setting one ImageAdapter
        imageAdapter = ImageAdapter(uploads, this@ImagesActivity)
        recycler_view.adapter = imageAdapter
        imageAdapter.listener = this@ImagesActivity


        dbListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
                progress_circular.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                // clear it before filling it
                uploads.clear()

                p0.children.forEach {
                    // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                    val currUpload = it.getValue(Upload::class.java) as Upload
                    currUpload.key = it.key
                    uploads.add(currUpload)
                }

                // It would update recycler after loading image from firebase storage
                imageAdapter.notifyDataSetChanged()
                progress_circular.visibility = View.INVISIBLE
            }

        })
    }

    override fun onItemClick(position: Int) {
        toast("Normal click at position $position", Toast.LENGTH_SHORT)
    }

    override fun onWhatEverClick(position: Int) {
        toast("WhatEver click at position $position", Toast.LENGTH_SHORT)
    }

    override fun onDeleteClick(position: Int) {
        toast("Delete click at position $position", Toast.LENGTH_SHORT)

        val selectedItem = uploads[position]
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

    fun ImagesActivity.toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}
