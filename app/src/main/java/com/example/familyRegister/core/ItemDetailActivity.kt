package com.example.familyRegister.core

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_list.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.familyRegister.R
import com.example.familyRegister.model.ItemUpload


class ItemDetailActivity() : AppCompatActivity(), ItemDetailAdapter.OnItemClickerListener {
    // add
    lateinit var path: String


    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    lateinit var itemDetailAdapter: ItemDetailAdapter

    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    lateinit var dbListener: ValueEventListener
    lateinit var databaseReference: DatabaseReference
    var uploads: ArrayList<ItemUpload> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        // add
        path = intent.getStringExtra("itemPath")
        Log.d("excution flag", "$path")
        databaseReference = FirebaseDatabase.getInstance().getReference(path)

        var item_position = 0
        Log.i(TAG, "position: $item_position")
        if (savedInstanceState!=null){

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        // Setting the recycler view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        // setting one ImageAdapter
        itemDetailAdapter = ItemDetailAdapter(uploads, this@ItemDetailActivity, item_position)
        recycler_view.adapter = itemDetailAdapter
        itemDetailAdapter.listener = this@ItemDetailActivity


        dbListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
                progress_circular.visibility = View.INVISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("excution flag", "sadfasfsdgdfhdfhfg")
                // clear it before filling it
                uploads.clear()

                    // Retrieve data from database, create an Upload object and store in the list of one ImageAdapter
                    val currUpload = p0.child("").getValue(ItemUpload::class.java) as ItemUpload
                    currUpload!!.key = p0.child("").key
                    uploads.add(currUpload)
                Log.d("upload",uploads.size.toString())


                // It would update recycler after loading image from firebase storage
                itemDetailAdapter.notifyDataSetChanged()
//                Log.d("upload",uploads.size.toString())
                progress_circular.visibility = View.INVISIBLE
            }

        })
    }

    override fun onItemClick(position: Int) {
        toast("Normal click at position $position", Toast.LENGTH_SHORT)
    }

    override fun onWhatEverClick(position: Int) {

    }

    override fun onDeleteClick(position: Int) {

    }

    fun ItemDetailActivity.toast(msg: String, duration: Int) {
        Toast.makeText(this, msg, duration).show()
    }
}