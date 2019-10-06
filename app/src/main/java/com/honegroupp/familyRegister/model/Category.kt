package com.honegroupp.familyRegister.model

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.PropertyName
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.view.home.CategoryAdapter
import com.honegroupp.familyRegister.view.itemList.ItemListActivity
import com.squareup.picasso.Picasso

data class Category(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("itemKeys")
    @get:PropertyName("itemKeys")
    var itemKeys: ArrayList<String> = ArrayList(),
    @set:PropertyName("count")
    @get:PropertyName("count")
    var count: Int = 0
) {
    /*This constructor has no parameter, which is used to create CategoryUpload while retrieve data from database*/
    constructor() : this("", ArrayList<String>(), 0)

    companion object {
        val DEFAULT_COVER = "DEFAULT_COVER"
    }

    /**
     * This method is responsible for get cover Url for the category.
     *
     * */
    fun setCoverURL(
        holder: CategoryAdapter.CategoryViewHolder,
        mActivity: AppCompatActivity,
        position: Int, uid: String
    ) {
//        var url = DEFAULT_COVER
        if (itemKeys.isNotEmpty()) {
            val lastItemKey = itemKeys.last()
            val rootPath = "/"
            FirebaseDatabaseManager.retrieve(rootPath) { d: DataSnapshot ->
                callbackGetCoverURL(
                    holder,
                    mActivity,
                    position,
                    uid,
                    lastItemKey,
                    d
                )
            }
        }else{
// TODO too much duplication
            holder.imageView.setImageResource(R.drawable.fui_ic_googleg_color_24dp)
            holder.imageView.setOnClickListener {
                // Snippet from navigate to the ItemListActivity along with the category path
                val goToItemListActivity = Intent(mActivity, ItemListActivity::class.java)

                //  pass user id to next activity
                goToItemListActivity.putExtra("UserID", uid)
                // pass category path to goToItemListActivity
                goToItemListActivity.putExtra("categoryPath", position.toString())
                mActivity.startActivity(goToItemListActivity)
            }
        }
    }

    /**
     * This is the call back for getting the image URl for the last item in the category.
     *
     * */
    private fun callbackGetCoverURL(
        holder: CategoryAdapter.CategoryViewHolder,
        mActivity: AppCompatActivity,
        position: Int,
        uid: String,
        itemKeys: String,
        dataSnapshot: DataSnapshot
    ) {
        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        // get item's last url
        val url =
            dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId)
                .child("items")
                .child(itemKeys)
                .child("imageURLs")
                .children
                .last()
                .getValue(String::class.java) as String

            Picasso.get()
                .load(url)
                .placeholder(R.mipmap.loading_jewellery)
                .fit()
                .centerCrop()
                .into(holder.imageView)

        holder.imageView.setOnClickListener {
            // Snippet from navigate to the ItemListActivity along with the category path
            val goToItemListActivity = Intent(mActivity, ItemListActivity::class.java)

            //  pass user id to next activity
            goToItemListActivity.putExtra("UserID", uid)
            // pass category path to goToItemListActivity
            goToItemListActivity.putExtra("categoryPath", position.toString())
            mActivity.startActivity(goToItemListActivity)
        }
    }
}