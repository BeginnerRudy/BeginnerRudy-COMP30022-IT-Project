package com.honegroupp.familyRegister.controller

import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.model.Family

class ItemListController{
    companion object{
        fun addItem(uid: String, categoryName: String, mActivity: AppCompatActivity){
            Family.addItem(uid, categoryName, mActivity)
        }

        /**
         * This method is responsible for showing all the items in the given category
         * */
        fun showItems(uid: String, categoryName: String, mActivity: AppCompatActivity){
            Family.showItems(uid, categoryName, mActivity)
        }
    }
}