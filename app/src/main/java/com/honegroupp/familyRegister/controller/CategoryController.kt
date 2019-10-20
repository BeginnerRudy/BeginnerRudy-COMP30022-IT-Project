package com.honegroupp.familyRegister.controller

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.model.User

class CategoryController {
    companion object {

        /**
         * This method is responsible for show all categories
         * */
        fun showCategory(
            uid: String,
            view: View,
            mActivity: AppCompatActivity
        ) {
            User.showCategories(uid, view, mActivity)
        }
    }
}