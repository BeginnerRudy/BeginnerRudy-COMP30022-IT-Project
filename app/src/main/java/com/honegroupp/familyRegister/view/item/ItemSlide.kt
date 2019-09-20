package com.honegroupp.familyRegister.view.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.ItemU

class ItemSlide : AppCompatActivity() {

    var uploads: ArrayList<ItemU> = ArrayList()
    val path = "CeShi" + "/" + "Furniture" + "/"
    val databaseReference = FirebaseDatabase.getInstance().getReference(path)
    lateinit var dbListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_slide)

        var mSlideViewPager = findViewById<ViewPager>(R.id.slideViewPager)
        var mDotLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        var sliderAdapter = SliderAdapter(uploads,this)
        mSlideViewPager.adapter = sliderAdapter

        mSlideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                invalidateOptionsMenu()
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.item_detail_menu, menu)
//        if (mSlideViewPager.getCurrentItem() === 0) {
//            menu.findItem(R.id.action_search).isVisible = true
//        } else if (mSlideViewPager.getCurrentItem() === 1) {
//            menu.findItem(R.id.action_search).isVisible = false
//        } else if (mSlideViewPager.getCurrentItem() === 2) {
//            // configure
//        } else if (mSlideViewPager.getCurrentItem() === 3) {
//            // configure
//        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu!!.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.item_detail_menu, menu);
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_1 -> {
                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.option_2 -> {
                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(dbListener)
    }

    fun toast(msg: String, duration: Int) {
        android.widget.Toast.makeText(this, msg, duration).show()
    }
}