package com.honegroupp.familyRegister.model

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.view.home.HomeActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.item.DetailSlide
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import com.honegroupp.familyRegister.view.itemList.ItemListActivity

/**
 * This class is responsible for storing data and business logic for Family
 *
 * @param familyId is the id of the family, this is generated by FirebaseAuthetication.
 * @param members is an ArrayList of UIDs of the member of the familyK
 *
 * @author Tianyi Mo
 * */

data class Family(
    @set:PropertyName("familyName")
    @get:PropertyName("familyName")
    var familyName: String = "",
    @set:PropertyName("password")
    @get:PropertyName("password")
    var password: String = "",
    @set:PropertyName("familyId")
    @get:PropertyName("familyId")
    var familyId: String = "",
    @set:PropertyName("members")
    @get:PropertyName("members")
    var members: ArrayList<String> = ArrayList(),
    @set:PropertyName("categories")
    @get:PropertyName("categories")
    var categories: ArrayList<Category> = ArrayList(),
    @set:PropertyName("items")
    @get:PropertyName("items")
    var items: HashMap<String, Item> = HashMap()
) {
    /*This constructor has no parameter, which is used to create CategoryUpload while retrieve data from database*/
    constructor() : this("", "", "", ArrayList(), ArrayList(), HashMap<String, Item>())

    /**
     * This method is responsible for storing Family to the database.
     *
     * */
    fun store(mActivity: AppCompatActivity, uid: String, username: String) {
        // upload family first
        FirebaseDatabaseManager.uploadFamily(this, uid)

        // update User
        val userPath = FirebaseDatabaseManager.USER_PATH + uid + "/"
        FirebaseDatabaseManager.retrieve(
            userPath
        ) { d: DataSnapshot ->
            callbackAddFamilyToUser(
                this,
                mActivity,
                uid,
                username,
                userPath,
                d
            )
        }
    }

    /**
     * This method is the callback for add family ID to teh given user and update user on the Database.
     *
     * */
    private fun callbackAddFamilyToUser(
        family: Family,
        mActivity: AppCompatActivity,
        uid: String,
        username: String,
        userPath: String,
        dataSnapshot: DataSnapshot
    ) {
        // get user
        val user = dataSnapshot.child("").getValue(User::class.java) as User

        // set family id
        user.familyId = this.familyId

        // family ID is the owner's id, so if the uid is same as the family id
        // This user is the owner of the family, otherwise not.
        if (family.familyId == uid) {
            user.isFamilyOwner = true
        }

        // update user in the database
        FirebaseDatabaseManager.update(userPath, user)

        // Go to Home page
        val intent = Intent(mActivity, HomeActivity::class.java)
        intent.putExtra("UserID", uid)
        intent.putExtra("UserName", username)
        mActivity.startActivity(intent)
    }

    companion object {
        /**
         * This method is responsible for joining the user to the given family
         * */
        fun joinFamily(
            mActivity: AppCompatActivity,
            familyIdInput: String,
            familyPasswordInput: String,
            uid: String,
            username: String
        ) {
            FirebaseDatabaseManager.retrieve(
                FirebaseDatabaseManager.FAMILY_PATH
            ) { d: DataSnapshot ->
                callbackJoinFamily(
                    mActivity,
                    uid,
                    username,
                    familyIdInput,
                    familyPasswordInput,
                    mActivity,
                    d
                )
            }
        }

        /**
         * This method is responsible for joining the User to the family.
         * */
        private fun callbackJoinFamily(
            mActivity: AppCompatActivity,
            currUid: String,
            username: String,
            familyIdInput: String,
            familyPasswordInput: String,
            currActivity: AppCompatActivity,
            dataSnapshot: DataSnapshot
        ) {

            // Convert familyiD to th format it stored on firebase
            val familyIdInputModified = familyIdInput.replace(".", "=", true)

            // Check whether family exist
            if (!dataSnapshot.hasChild(familyIdInputModified) || familyIdInput.trim() == "") {
                Toast.makeText(
                    currActivity,
                    mActivity.getString(R.string.family_id_not_exist),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Get family
                val family =
                    dataSnapshot.child(familyIdInputModified).getValue(Family::class.java) as Family
                // Check password
                if (family.password != Hash.applyHash(familyPasswordInput)) {
                    Toast.makeText(
                        currActivity,
                        mActivity.getString(R.string.password_is_incorrect),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    // Add user to family and add family to user
                    if (!family.members.contains(currUid)) {
                        family.members.add(currUid)
                        family.store(mActivity, currUid, username)
                    }

                    Toast.makeText(
                        currActivity,
                        mActivity.getString(R.string.join_family_successfully),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        /**
         * This method is responsible for showing the items in the item list
         *
         * */
        fun addItem(uid: String, categoryName: String, mActivity: AppCompatActivity) {
            // go to upload new item
            val addButton = mActivity.findViewById<FloatingActionButton>(R.id.btn_add)
            addButton.setOnClickListener {
                val intent = Intent(mActivity, ItemUploadActivity::class.java)
                intent.putExtra("UserID", uid)
                intent.putExtra("categoryPath", categoryName)
                mActivity.startActivity(intent)
            }
        }


        /**
         * This function is responsible for showing all the items in the category
         *
         * */
        fun showItems(
            uid: String,
            items: ArrayList<Item>,
            itemListAdapter: ContainerAdapter,
            categoryName: String,
            mActivity: ItemListActivity
        ) {
            val rootPath = "/"
            FirebaseDatabaseManager.retrieveLive(rootPath) { d: DataSnapshot ->
                callbackShowItems(
                    uid,
                    items,
                    itemListAdapter,
                    categoryName,
                    mActivity,
                    d
                )
            }
        }

        /**
         * This method is the callback for the show items
         * */
        private fun callbackShowItems(
            uid: String,
            items: ArrayList<Item>,
            itemListAdapter: ContainerAdapter,
            categoryName: String,
            mActivity: ItemListActivity,
            dataSnapshot: DataSnapshot
        ) {


            // get user's family ID
            val currFamilyId =
                dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                    String::class.java
                ) as String

            // set familyID
            mActivity.familyId = currFamilyId

            // get item keys for the given category as an ArrayList
            val itemKeysSnapshot =
                dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId)
                    .child("categories").child(categoryName).child("itemKeys")

            var itemKeys = if (!itemKeysSnapshot.hasChildren()) {
                // the item keys is empty
                ArrayList()
            } else {
                itemKeysSnapshot.value as ArrayList<String>
            }

            // remove null from the itemkeys
            itemKeys = itemKeys.filterNotNull() as ArrayList<String>

            // clear items once retrieve item from the database
            items.clear()
            for (key in itemKeys) {
                // get item by each key
                val currItem =
                    dataSnapshot
                        .child(FirebaseDatabaseManager.FAMILY_PATH)
                        .child(currFamilyId)
                        .child("items")
                        .child(key)
                        .getValue(Item::class.java) as Item

                currItem.key = key

                // add it to the items, check item is visible, if not check user is owner
                if (currItem.isPublic) {
                    items.add(currItem)
                } else if (currItem.itemOwnerUID == mActivity.uid) {
                    items.add(currItem)
                }

                // notify the adapter to update
                itemListAdapter.notifyDataSetChanged()
                // Make the progress bar invisible
                mActivity.findViewById<ProgressBar>(R.id.progress_circular).visibility =
                    View.INVISIBLE
                mActivity.findViewById<TextView>(R.id.text_view_empty_category).visibility =
                    View.INVISIBLE
            }

            if (itemKeys.isEmpty()) {
                // Make the progress bar invisible
                mActivity.findViewById<ProgressBar>(R.id.progress_circular).visibility =
                    View.INVISIBLE

                mActivity.findViewById<TextView>(R.id.text_view_empty_category).visibility =
                    View.VISIBLE
            }
        }

        /**
         * Delete item from family
         * */
        fun deleteItem(
            uid: String,
            categoryName: String,
            mActivity: ContainerActivity,
            itemId: String
        ) {
            val rootPath = "/"
            FirebaseDatabaseManager.retrieve(rootPath) { d: DataSnapshot ->
                callbackDeleteItem(uid, categoryName, mActivity, itemId, d)
            }
        }

        /**
         * This is the callback for deleteItem.
         * This method defines the logic of deleting an item.
         * */
        @SuppressLint("RestrictedApi")
        private fun callbackDeleteItem(
            uid: String,
            categoryName: String,
            mActivity: ContainerActivity,
            itemId: String,
            dataSnapshot: DataSnapshot
        ) {
            // get user's family ID
            val currFamilyId =
                dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                    String::class.java
                ) as String

            // find the category
            val itemKeys = dataSnapshot
                .child(FirebaseDatabaseManager.FAMILY_PATH)
                .child(currFamilyId)
                .child("categories")
                .child(categoryName)
                .child("itemKeys")
                .children


            for (itemKey in itemKeys) {
                // remove item from category only.
                if (itemKey.value == itemId) {
                    itemKey.ref.setValue(null)

                    break
                }
            }
            // update itemkeys index first

            val itemKeysPath =
                "${FirebaseDatabaseManager.FAMILY_PATH}$currFamilyId/categories/$categoryName/itemKeys/"
            var categoryItemKeys = dataSnapshot
                .child(FirebaseDatabaseManager.FAMILY_PATH)
                .child(currFamilyId)
                .child("categories")
                .child(categoryName)
                .child("itemKeys")
                .value as ArrayList<String>

            categoryItemKeys = categoryItemKeys.filterNotNull() as ArrayList<String>
            categoryItemKeys.remove(itemId)

            FirebaseDatabaseManager.update(itemKeysPath, categoryItemKeys)

            // update the item count
            val itemCount = dataSnapshot
                .child(FirebaseDatabaseManager.FAMILY_PATH)
                .child(currFamilyId)
                .child("categories")
                .child(categoryName)
                .child("itemKeys")
                .childrenCount
            val countReference =
                "${FirebaseDatabaseManager.FAMILY_PATH}$currFamilyId/categories/$categoryName/count"
            FirebaseDatabase.getInstance().getReference(countReference)
                .setValue(itemCount - 1)


        }


        /**
         * This method is responsible for showing all items in the show page
         *
         * */
        fun displayShowPage(
            mActivity: HomeActivity,
            items: ArrayList<Item>,
            showTabAdapter: ContainerAdapter,
            uid: String,
            currFrag: Fragment
        ) {
            val rootPath = "/"
            FirebaseDatabaseManager.retrieveLive(rootPath) { d: DataSnapshot ->
                callbackDisplayShowPage(
                    uid,
                    mActivity,
                    currFrag,
                    items,
                    showTabAdapter,
                    d
                )
            }
        }

        /**
         * This method is the callback for showing all items in the show page
         * */
        private fun callbackDisplayShowPage(
            uid: String,
            mActivity: HomeActivity,
            currFrag: Fragment,
            items: ArrayList<Item>,
            allTabAdapter: ContainerAdapter,
            dataSnapshot: DataSnapshot
        ) {

            if (currFrag != null && currFrag.isVisible) {
                // get user's family ID
                val currFamilyId = FirebaseDatabaseManager.getFamilyIDByUID(uid, dataSnapshot)
                mActivity.familyId = currFamilyId

                // get items from the family
                val allItems =
                    dataSnapshot
                        .child(FirebaseDatabaseManager.FAMILY_PATH)
                        .child(currFamilyId)
                        .child("items")
                        .children


                // clear items once retrieve item from the database
                items.clear()

                allItems.forEach {
                    val item = it.getValue(Item::class.java) as Item

                    // add it to the items, check item is visible, if not check user is owner
                    if (item.showPageUids.contains(uid)) {
                        item.key = it.key.toString()
                        items.add(item)
                    }
                }


                // notify the adapter to update
                allTabAdapter.notifyDataSetChanged()

                if (items.isEmpty()) {
                    // Make the progress bar invisible
                    mActivity.findViewById<ProgressBar>(R.id.progress_circular).visibility =
                        View.INVISIBLE

                    val text = mActivity.findViewById<TextView>(R.id.text_view_empty_category)
                    text.text = mActivity.getString(R.string.no_items_for_the_show_page)
                    text.visibility = View.VISIBLE
                } else {
                    // Make the progress bar invisible
                    mActivity.findViewById<ProgressBar>(R.id.progress_circular).visibility =
                        View.INVISIBLE
                    mActivity.findViewById<TextView>(R.id.text_view_empty_category).visibility =
                        View.INVISIBLE
                }
            }
        }


        fun showAll(
            uid: String,
            items: ArrayList<Item>,
            showTabAdapter: ContainerAdapter,
            mActivity: HomeActivity,
            currFrag: Fragment
        ) {
            val rootPath = "/"
            FirebaseDatabaseManager.retrieveLive(rootPath) { d: DataSnapshot ->
                callbackShowAll(uid, items, showTabAdapter, mActivity, currFrag, d)
            }
        }


        private fun callbackShowAll(
            uid: String,
            items: ArrayList<Item>,
            showTabAdapter: ContainerAdapter,
            mActivity: HomeActivity,
            currFrag: Fragment,
            dataSnapshot: DataSnapshot
        ) {
            if (currFrag != null && currFrag.isVisible) {
                // get user's family ID
                val currFamilyId = FirebaseDatabaseManager.getFamilyIDByUID(uid, dataSnapshot)
                mActivity.familyId = currFamilyId

                // get items from the family
                val allItems =
                    dataSnapshot
                        .child(FirebaseDatabaseManager.FAMILY_PATH)
                        .child(currFamilyId)
                        .child("items")
                        .children

                // clear items once retrieve item from the database
                items.clear()

                allItems.forEach {
                    val item = it.getValue(Item::class.java) as Item

                    item.key = it.key.toString()
                    items.add(item)
                }

                // notify the adapter to update
                showTabAdapter.notifyDataSetChanged()

                if (items.isEmpty()) {
                    // Make the progress bar invisible
                    mActivity.findViewById<ProgressBar>(R.id.all_progress_circular).visibility =
                        View.INVISIBLE

                    val text = mActivity.findViewById<TextView>(R.id.text_view_empty_category)
                    text.visibility = View.VISIBLE
                } else {
                    // Make the progress bar invisible
                    mActivity.findViewById<ProgressBar>(R.id.all_progress_circular).visibility =
                        View.INVISIBLE
                    mActivity.findViewById<TextView>(R.id.all_text_view_empty_category).visibility =
                        View.INVISIBLE
                }
            }
        }
    }

}
