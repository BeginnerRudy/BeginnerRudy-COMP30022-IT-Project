package com.honegroupp.familyRegister.view.itemList

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import android.view.Menu
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ItemListController
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.search.SearchActivity
import kotlinx.android.synthetic.main.activity_item_list.*

class ItemListActivity : ContainerActivity() {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        // add item logic
        ItemListController.addItem(uid, categoryName, this)

        // show items in the category logic
        ItemListController.showItems(uid, categoryName, navi_sort_view, sortOrder, this)

        //jump to search activity
        btn_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("UserID", uid)
            intent.putExtra("Category", categoryName)
            this.startActivity(intent)
        }

        // Press  key to navigate to navigation drawer
        btn_sort.setOnClickListener {
            drawer_sort_layout.openDrawer(GravityCompat.END)
        }


    }

}