package com.honegroupp.familyRegister.view.itemList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ItemListController
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.search.SearchActivity
import kotlinx.android.synthetic.main.activity_item_list.*

class ItemListActivity : ContainerActivity() {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var sortOrder: String = SORT_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        // make the toolbar visible
        toolbar_item_list.visibility = View.VISIBLE

        // set the title name for the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_item_list)
        toolbar.title = intent.getStringExtra("categoryName")

        // set the color of the title of the toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.colorWhite))

        // set the navigation back button for the tool bar
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // add item logic
        ItemListController.addItem(uid, categoryPosition, this)

        // show items in the category logic
        ItemListController.showItems(uid, categoryPosition, this)

        //jump to search activity
        btn_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("UserID", uid)
            intent.putExtra("Category", categoryPosition)
            this.startActivity(intent)
        }

        // Press  key to navigate to navigation drawer
        btn_sort.setOnClickListener {
            drawer_sort_layout.openDrawer(GravityCompat.END)
        }


    }

}