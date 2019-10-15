package com.honegroupp.familyRegister.model


import android.widget.ImageButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.PropertyName
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Item(
    @set:PropertyName("itemName")
    @get:PropertyName("itemName")
    var itemName: String = "",

    @set:PropertyName("itemDescription")
    @get:PropertyName("itemDescription")
    var itemDescription: String = "",

    @set:PropertyName("itemMaterial")
    @get:PropertyName("itemMaterial")
    var itemMaterial: String = "",

    @set:PropertyName("itemOwnerUID")
    @get:PropertyName("itemOwnerUID")
    var itemOwnerUID: String = "",

    @set:PropertyName("itemLocation")
    @get:PropertyName("itemLocation")
    var itemLocation:  String = "",

    @set:PropertyName("imageURLs")
    @get:PropertyName("imageURLs")
    var imageURLs: ArrayList<String> = ArrayList(),

    @set:PropertyName("isPublic")
    @get:PropertyName("isPublic")
    var isPublic: Boolean = false,

    @set:PropertyName("date")
    @get:PropertyName("date")
    var date: String = "",

    @set:PropertyName("showPageUids")
    @get:PropertyName("showPageUids")
    var showPageUids: HashMap<String, String> = HashMap()

) {
    /*This is the primary constructor to create an item instance*/
//    constructor(itemName: String, itemDescription: String, itemOwnerUID: String, imageURLs: ArrayList<String>, itemPrivacy: String) : this() {
//        this.itemName = "itemName"
//        this.itemDescription = "itemDescription"
//        this.itemOwnerUID = itemOwnerUID
//        this.imageURLs = imageURLs
//        this.itemPrivacy = itemPrivacy
//    }

    var key: String? = null

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
        val currFamilyId = FirebaseDatabaseManager.getFamilyIDByUID(uid, dataSnapshot)

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
        val categoryPath =
            FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/" + "categories/" + categoryName + "/"
        FirebaseDatabaseManager.uploadItem(this, path, items, categoryPath)
    }

    /**
     * this function is used to edit item
     * */
    fun edit(uid: String, categoryName: String) {

        val rootPath = "/"
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackEdit(uid, categoryName, d) }
    }

    /**
     * this is the help callback function of edit item, use to get current index of item list and edit item
     * */
    private fun callbackEdit(uid: String, categoryName: String, dataSnapshot: DataSnapshot) {
        // get user's family ID
        val currFamilyId = FirebaseDatabaseManager.getFamilyIDByUID(uid, dataSnapshot)

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
        val categoryPath =
            FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/" + "categories/" + categoryName + "/"
        FirebaseDatabaseManager.UeditItem(this, path, items, categoryPath)
    }

    /**
     * This method is responsible for adding or removing item from the user's show page.
     *
     * */
    fun manageShowItem(uid: String) {
        val rootPath = "/"
        FirebaseDatabaseManager.retrieve(rootPath) { d: DataSnapshot ->
            callbackManageShowItem(uid, d)
        }
    }

    /**
     * This method is the callback for adding or removing item from the user's show page.
     *
     * */
    private fun callbackManageShowItem(
        uid: String,
        dataSnapshot: DataSnapshot
    ) {
        // get user's family ID
        val currFamilyId = FirebaseDatabaseManager.getFamilyIDByUID(uid, dataSnapshot)

        // First check whether this user's uid in the item's showPageUids
        val showPageUidsPath =
            "${FirebaseDatabaseManager.FAMILY_PATH}$currFamilyId/items/$key/showPageUids/"
        val isInItemShowPageUids = dataSnapshot.child(showPageUidsPath).hasChild(uid)

        // if it is not in the showPageUids, then the user now want to click to add it in
        if (!isInItemShowPageUids) {
            // add this user's uid
            FirebaseDatabase.getInstance().getReference(showPageUidsPath).child(uid)
                .setValue(FirebaseDatabaseManager.NOTHING)
        } else {
            // remove user's uid
            FirebaseDatabase.getInstance().getReference(showPageUidsPath).child(uid).removeValue()
        }

    }
}