package com.honegroupp.familyRegister.controller

import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.model.Family

class ItemListController{
    companion object{
        fun showItems(uid: String, categoryName: String, mActivity: AppCompatActivity){
            Family.showItems(uid, categoryName, mActivity)
        }
    }
}