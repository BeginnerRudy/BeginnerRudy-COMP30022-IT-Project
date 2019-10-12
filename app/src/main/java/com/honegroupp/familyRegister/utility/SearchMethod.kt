package com.honegroupp.familyRegister.utility

import android.content.Intent
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.item.DetailSlide

class SearchMethod{

    /**
     * This function used to create initial view for search activity
     * */
    fun init(mActivity: AppCompatActivity, listView: ListView, uid: String, category: Int){
        //get whole snapshot of database
        val rootPath = "/"

        //get datasnapshot and create view in callback function
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackInit(mActivity, uid, category, listView, d) }
    }

    /**
     * This callback function is the help function to create search view*/
    private fun callbackInit(mActivity: AppCompatActivity, uid:String, category: Int, listView: ListView, dataSnapshot: DataSnapshot){

        var itemList: ArrayList<Item> = ArrayList()

        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        //get itemlist datasnapshot
        val itemsDataSnapshot =
            dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId).child("items")

        //when click on category page
        if (category >= DetailSlide.CATEGORY_SIGNAL){
            val itemsKey = dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH)
                .child(currFamilyId)
                .child("categories")
                .child("$category/")
                .child("itemKeys")

            for (keyDataSnapshot in itemsKey.children) {
                val key = keyDataSnapshot.getValue(
                    String::class.java
                )!!

                val newItem = itemsDataSnapshot.child(key).getValue(
                    Item::class.java
                )!!

                itemList.add(newItem)
            }

        }
        //when click on all page
        else if (category == DetailSlide.ALL_PAGE_SIGNAL){
            //transfer datasnapshot to arraylist
            for (itemsDataSnapshot in itemsDataSnapshot.children){
                itemList.add(
                    itemsDataSnapshot.getValue(
                        Item::class.java
                    )!!
                )
            }
        }

        //set adapter to show items
        listView.adapter = ListViewAapter(itemList, mActivity)

        //jump to item detail page with 4 necessary arguments
        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(mActivity, DetailSlide::class.java)
            intent.putExtra("UserID", uid)
            intent.putExtra("PositionList", listView.adapter.getItemId(position).toString())
            intent.putExtra("CategoryNameList", category.toString())
            intent.putExtra("FamilyId", currFamilyId)
            mActivity.startActivity(intent)
        }
    }

    /**
     * This function used to create initial view for search activity
     * */
    fun doSearch(mActivity: AppCompatActivity, listView: ListView, uid: String, category: Int, queryText: String){
        //get whole snapshot of database
        val rootPath = "/"

        //get datasnapshot and create view in callback function
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackDoSearch(mActivity, uid, category,queryText, listView, d) }
    }

    /**
     * This function is the help function to show search view with query*/
    private fun callbackDoSearch(mActivity: AppCompatActivity, uid: String, category: Int, queryText: String, listView: ListView, dataSnapshot: DataSnapshot) {

        var itemList: ArrayList<Item> = ArrayList()
        var newList: ArrayList<Item>
        //a hashmap to get correct position of item in itemlist
        var itemPositionMap : HashMap<Item, Int> = HashMap()

        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        //get itemlist datasnapshot
        val itemsDataSnapshot =
            dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId).child("items")

        //when click on category page
        if (category >= DetailSlide.CATEGORY_SIGNAL){
            val itemsKey = dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH)
                .child(currFamilyId)
                .child("categories")
                .child("$category/")
                .child("itemKeys")

            for (keyDataSnapshot in itemsKey.children) {
                val key = keyDataSnapshot.getValue(
                    String::class.java
                )!!

                val newItem = itemsDataSnapshot.child(key).getValue(
                    Item::class.java
                )!!

                itemList.add(newItem)
            }

        }
        //when click on all page
        else if (category == DetailSlide.ALL_PAGE_SIGNAL){
            //transfer datasnapshot to arraylist
            for (itemsDataSnapshot in itemsDataSnapshot.children){
                itemList.add(
                    itemsDataSnapshot.getValue(
                        Item::class.java
                    )!!
                )
            }
        }

        //get final list after search
        newList = search(queryText, itemList)

        //set adapter to show items
        listView.adapter = ListViewAapter(newList, mActivity)

        for (i in 0 until itemList.size){
            itemPositionMap[itemList[i]] = i
        }


        listView.setOnItemClickListener { parent, view, position, id ->
            val element = listView.adapter.getItem(listView.adapter.getItemId(position).toInt()) // The item that was clicked
            val intent = Intent(mActivity, DetailSlide::class.java)
            intent.putExtra("UserID", uid)
            intent.putExtra("PositionList", itemPositionMap[element].toString())
            intent.putExtra("CategoryNameList", category.toString())
            intent.putExtra("FamilyId", currFamilyId)
            mActivity.startActivity(intent)
        }
    }

    //a search function depending on item Name
    fun search(queryText: String, itemList: ArrayList<Item>):ArrayList<Item>{
        val newItemList: ArrayList<Item> = ArrayList()
        for (item in itemList){
            if (item.itemName.contains(queryText, ignoreCase = true) || item.date.contains(queryText)){
                newItemList.add(item);
            }
        }
        return newItemList
    }


}