package com.honegroupp.familyRegister.controller

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.itemList.ItemListActivity
import kotlinx.android.synthetic.main.activity_item_list.*

class ItemListController {
    companion object {
        fun addItem(uid: String, categoryName: String, mActivity: AppCompatActivity) {
            Family.addItem(uid, categoryName, mActivity)
        }

        /**
         * This method is responsible for showing all the items in the given category
         * */
        fun showItems(
            uid: String,
            categoryName: String,
            navi_sort_view: NavigationView,
            mActivity: ItemListActivity
        ) {

            // get items of that category
            val items = ArrayList<Item>()

            val recyclerView = mActivity.findViewById<RecyclerView>(R.id.item_list_recycler_view)

            // Setting the recycler view
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(mActivity, 2)

            // setting one ItemListAdapter
            val itemListAdapter = ContainerAdapter(items, mActivity, ContainerAdapter.CATEGORY)
            recyclerView.adapter = itemListAdapter
            itemListAdapter.listener = mActivity

            // set the sorter logic
            sortItem(mActivity, itemListAdapter, navi_sort_view)

            // show the items
            Family.showItems(uid, items, itemListAdapter, categoryName, mActivity)
        }

        /**
         * This method is responsible for delete the items in the given category
         * */
        fun deleteItems(uid: String, categoryName: String, itemId: String) {
            Family.deleteItem(uid, categoryName, itemId)
        }

        //activity adapter
        fun sortItem(
            mActivity: ItemListActivity,
            adapter: ContainerAdapter,
            navi_sort_view: NavigationView
        ) {
            val drawer_sort_layout = mActivity.findViewById<DrawerLayout>(R.id.drawer_sort_layout)
            navi_sort_view.menu.findItem(R.id.sort_name_asc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortBy { it.itemName }
                // update the UI layer
                updateRecyclerView(adapter, drawer_sort_layout)
                true
            }
            navi_sort_view.menu.findItem(R.id.sort_name_desc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortByDescending { it.itemName }
                // update the UI layer
                updateRecyclerView(adapter, drawer_sort_layout)
                true
            }
            navi_sort_view.menu.findItem(R.id.sort_time_asc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortBy { it.date }
                // update the UI layer
                updateRecyclerView(adapter, drawer_sort_layout)
                true
            }
            navi_sort_view.menu.findItem(R.id.sort_time_desc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortByDescending { it.date }
                // update the UI layer
                updateRecyclerView(adapter, drawer_sort_layout)
                true
            }

        }

        fun updateRecyclerView(adapter: ContainerAdapter, drawer_sort_layout:DrawerLayout){
            // hide the item
            drawer_sort_layout.closeDrawer(GravityCompat.END)
            // update the recycler view
            adapter.notifyDataSetChanged()
        }
    }
}