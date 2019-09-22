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
    var itemDescription: String = "",
    @set:PropertyName("itemDescription")
    @get:PropertyName("itemDescription")
    var itemName: String = "",
    @set:PropertyName("itemOwnerUID")
    @get:PropertyName("itemOwnerUID")
    var itemOwnerUID: String = "",
    @set:PropertyName("imageURLs")
    @get:PropertyName("imageURLs")
    var imageURLs: ArrayList<String> = ArrayList(),
    @set:PropertyName("visibility")
    @get:PropertyName("visibility")
    var visibility: Boolean = false
) {
    /*This is the primary constructor to create an item instance*/
    constructor(itemName: String, itemDescription: String, itemOwnerUID: String, imageURLs: ArrayList<String>) : this() {
        this.itemDescription = itemDescription
        this.itemName = itemName
        this.itemOwnerUID = itemOwnerUID
        this.imageURLs = imageURLs
    }

    /**
     * this function is used to store new item into item list of a family
     * */
    fun store(uid: String) {

        val rootPath = "/"
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackStore(uid, d) }
    }

    /**
     * this is the help callback function of store item, use to get current index of item list and upload new item
     * */
    private fun callbackStore(uid: String, dataSnapshot: DataSnapshot) {
        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        var lastIndex: Int

        //get last index
        val familyItemDataSnapshot =
            dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId).child("items")

        lastIndex = if (familyItemDataSnapshot.hasChildren()) {
            familyItemDataSnapshot.children.last().key.toString().toInt()
        } else {
            -1
        }

        val path = FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/"
        FirebaseDatabaseManager.uploadItem(this, path, lastIndex + 1)
    }
}