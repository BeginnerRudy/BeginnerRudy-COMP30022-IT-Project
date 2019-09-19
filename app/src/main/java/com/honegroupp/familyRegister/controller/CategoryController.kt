package com.honegroupp.familyRegister.controller

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.home.CategoriesTabFragment
import com.honegroupp.familyRegister.view.home.CategoryAdapter

class CategoryController {
    companion object{
        fun showCategory(uid:String, view: View, mActivity:AppCompatActivity){
            User.showCategories(uid, view, mActivity)
        }
    }
}