package com.honegroupp.familyRegister.view.itemList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ItemListController
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import com.honegroupp.familyRegister.view.utility.SearchActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_item_list.*

class ItemListActivity : ContainerActivity() {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        // add item logic
        ItemListController.addItem(uid, categoryName, this)

        // show items in the category logic
        ItemListController.showItems(uid, categoryName, sortOrder, this)

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

        //sort by item name ascending
        navi_sort_view.menu.findItem(R.id.sort_name_asc).setOnMenuItemClickListener{
            //jump logic
            val intent = Intent(this, ItemListActivity::class.java)

            intent.putExtra("UserID", uid)
            intent.putExtra("categoryPath", categoryName)
            intent.putExtra("sortOrder", "name_asc")
            startActivity(intent)
            true
        }

        //sort by item name descending
        navi_sort_view.menu.findItem(R.id.sort_name_desc).setOnMenuItemClickListener{
            val intent = Intent(this, ItemListActivity::class.java)

            intent.putExtra("UserID", uid)
            intent.putExtra("categoryPath", categoryName)
            intent.putExtra("sortOrder", "name_desc")
            startActivity(intent)
            true
        }

        //sort by time ascending
        navi_sort_view.menu.findItem(R.id.sort_time_asc).setOnMenuItemClickListener{
            val intent = Intent(this, ItemListActivity::class.java)

            intent.putExtra("UserID", uid)
            intent.putExtra("categoryPath", categoryName)
            intent.putExtra("sortOrder", "time_asc")
            startActivity(intent)
            true
        }

        //sort by time descending
        navi_sort_view.menu.findItem(R.id.sort_time_desc).setOnMenuItemClickListener{
            val intent = Intent(this, ItemListActivity::class.java)

            intent.putExtra("UserID", uid)
            intent.putExtra("categoryPath", categoryName)
            intent.putExtra("sortOrder", "time_desc")
            startActivity(intent)
            true
        }


    }
}