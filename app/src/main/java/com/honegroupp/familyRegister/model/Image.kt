//package com.honegroupp.familyRegister.model
//
//
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.PropertyName
//import com.honegroupp.familyRegister.R
//import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
//import com.honegroupp.familyRegister.view.home.CategoriesTabFragment
//import com.honegroupp.familyRegister.view.home.CategoryAdapter
//import com.honegroupp.familyRegister.view.home.HomeActivity
//import kotlinx.android.synthetic.main.fragment_categories.view.*
//
///**
// * This class is responsible for storing data and business logic for User
// * *
// *
// * */
//
//data class Image(
//    @set:PropertyName("uri")
//    @get:PropertyName("uri")
//    var uri: String,
//    @set:PropertyName("address")
//    @get:PropertyName("address")
//    var address: String,
//    @set:PropertyName("uploaded")
//    @get:PropertyName("uploaded")
//    var uploaded: Boolean = false
//) {
//
//    /*This constructor*/
//    constructor() : this("")
//
//    fun store(mActivity: AppCompatActivity, uid: String) {
//        FirebaseDatabaseManager.uploadUser(mActivity, uid, this)
//
//    }
//
//    fun showUserInfor() {
//
//    }
//
//
//
//}
