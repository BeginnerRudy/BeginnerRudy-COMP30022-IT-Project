package com.honegroupp.familyRegister.controller

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.itemList.ItemListActivity

class ItemListController{
    companion object{
        fun addItem(uid: String, categoryName: String, mActivity: AppCompatActivity){
            Family.addItem(uid, categoryName, mActivity)
        }

        /**
         * This method is responsible for showing all the items in the given category
         * */
        fun showItems(uid: String, categoryName: String, sortOrder: String, mActivity: ItemListActivity){
            Family.showItems(uid, categoryName, sortOrder, mActivity)
        }

        /**
         * This method is responsible for delete the items in the given category
         * */
        fun deleteItems(uid: String, categoryName: String, mActivity: ContainerActivity, itemId: String){
            Family.deleteItem(uid, categoryName, mActivity, itemId)
        }

    }
}