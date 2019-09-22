package com.honegroupp.familyRegister.model


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
    fun store(uid: String, categoryName: String) {

        val rootPath = "/"
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackStore(uid, categoryName, d) }
    }

    /**
     * this is the help callback function of store item, use to get current index of item list and upload new item
     * */
    private fun callbackStore(uid: String, categoryName: String, dataSnapshot: DataSnapshot) {
        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        //get last index
        val familyItemsDataSnapshot =
            dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId)
                .child("items")

        // if there has item in the family


        var items: HashMap<String, Item>

        items = if (familyItemsDataSnapshot.hasChildren()) {
            familyItemsDataSnapshot.value as HashMap<String, Item>
        } else {
            HashMap()
        }

        val path = FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/"
        val categoryPath = FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/" + "categories/" + categoryName + "/"
        FirebaseDatabaseManager.uploadItem(this, path, items, categoryPath)
    }
}