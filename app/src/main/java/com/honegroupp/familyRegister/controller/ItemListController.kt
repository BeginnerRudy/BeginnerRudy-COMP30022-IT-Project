package com.honegroupp.familyRegister.controller

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.itemList.ItemListActivity

class ItemListController{
    companion object{
        fun addItem(uid: String, categoryName: String, mActivity: AppCompatActivity){
            Family.addItem(uid, categoryName, mActivity)
        }

        /**
         * This method is responsible for showing all the items in the given category
         * */
        fun showItems(uid: String, categoryName: String, mActivity: ItemListActivity){

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

            // show the items
            Family.showItems(uid, items, itemListAdapter, categoryName, mActivity)
        }

        /**
         * This method is responsible for delete the items in the given category
         * */
        fun deleteItems(uid: String, categoryName: String, mActivity: ContainerActivity, itemId: String){
            Family.deleteItem(uid, categoryName, mActivity, itemId)
        }

    }
}