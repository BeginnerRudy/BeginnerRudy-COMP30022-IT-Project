package com.honegroupp.familyRegister.view.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item

class ItemSlide : AppCompatActivity() {
    var uploads: ArrayList<Item> = ArrayList()
    val path = "CeShi" + "/" + "Furniture" + "/"
    val databaseReference = FirebaseDatabase.getInstance().getReference(path)
    lateinit var dbListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_slide)

        var mSlideViewPager = findViewById<ViewPager>(R.id.slideViewPager)
        var mDotLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        val slide_images = arrayOf(R.mipmap.code_icon, R.mipmap.eat_icon, R.mipmap.sleep_icon)
        val slide_headings = arrayOf("EAT", "SLEEP", "CODE")
        val slide_descs = arrayOf(
            "Lkcjash, sajhashcj sajdch sakjch, askjch, aschk, ascjhkjhkjhkhcskah csajk cajksh  akjchoeufq feoif asc,c asjhkjhascjkh askjhckash" + "aliqua",
            "Lkcjash, sajhashcj sajdch sakjch, askjch, aschk, ascjhkjhkjhkhcskah csajk cajksh  akjchoeufq feoif asc,c asjhkjhascjkh askjhckash" + "aliqua",
            "Lkcjash, sajhashcj sajdch sakjch, askjch, aschk, ascjhkjhkjhkhcskah csajk cajksh  akjchoeufq feoif asc,c asjhkjhascjkh askjhckash" + "aliqua"
        )

        var sliderAdapter = SliderAdapter(uploads,this, slide_images, slide_headings, slide_descs)
        mSlideViewPager.adapter = sliderAdapter

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
                    uploads.add(currUpload)
                }
                Log.d("upload",uploads.size.toString())

                // It would update recycler after loading image from firebase storage
                sliderAdapter.notifyDataSetChanged()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(dbListener)
    }

    fun toast(msg: String, duration: Int) {
        android.widget.Toast.makeText(this, msg, duration).show()
    }
}