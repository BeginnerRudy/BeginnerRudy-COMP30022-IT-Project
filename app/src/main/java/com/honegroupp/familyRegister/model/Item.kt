package com.honegroupp.familyRegister.model

import android.content.Intent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.PropertyName
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager

data class Item(
    @set:PropertyName("familyId")
    @get:PropertyName("familyId")
    var familyId: String = "",
    @set:PropertyName("itemId")
    @get:PropertyName("itemId")
    var itemId: String = "",
    @set:PropertyName("itemName")
    @get:PropertyName("itemName")
    var itemName: String = "",
    @set:PropertyName("itemOwnerUID")
    @get:PropertyName("itemOwnerUID")
    var itemOwnerUID: String = "",
    @set:PropertyName("imageURLs")
    @get:PropertyName("imageURLs")
    var imageURLs: ArrayList<String> = ArrayList()
) {
    /*This is the primary constructor to create an item instance*/
    constructor(itemName: String, itemOwnerUID: String, imageURLs: ArrayList<String>) : this() {
        this.itemName = itemName
        this.itemOwnerUID = itemOwnerUID
        this.imageURLs = imageURLs
    }

    /**
     * this function is used to store new item into item list of a family
     * */
    fun store(familyId: String) {

        val itemsPath = FirebaseDatabaseManager.FAMILY_PATH + familyId + "/items"
        FirebaseDatabaseManager.retrieve(
            itemsPath, ::callbackStore
        )
    }

    /**
     * this is the help callback function of store item, use to get current index of item list and upload new item
     * */
    private fun callbackStore(dataSnapshot: DataSnapshot) {
        val lastIndex = dataSnapshot.children.last().toString().toInt()
        val path = FirebaseDatabaseManager.FAMILY_PATH + familyId + "/items"
        FirebaseDatabaseManager.uploadItem(this, path, lastIndex+1)
    }
}