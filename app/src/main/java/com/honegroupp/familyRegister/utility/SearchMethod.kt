package com.honegroupp.familyRegister.utility

import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Item

class SearchMethod{

    object SearchMehtod;

    fun doSearch(mActivity: AppCompatActivity, uid: String, queryText: String, listView: ListView){

        FirebaseDatabaseManager.retrieve(
            uid
        ) { d: DataSnapshot -> callbackSearch(mActivity, uid, queryText, listView, d) }
    }

    private fun callbackSearch(mActivity: AppCompatActivity, uid: String, queryText: String, listView: ListView, dataSnapshot: DataSnapshot) {

        var itemList: ArrayList<Item> = ArrayList()
        var newList: ArrayList<Item> = ArrayList()

        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        val itemsDataSnapshot =
            dataSnapshot.child(FirebaseDatabaseManager.FAMILY_PATH).child(currFamilyId).child("items")

        //transfer datasnapshot to arraylist
        for (itemsDataSnapshot in itemsDataSnapshot.children){
            itemList.add(
                itemsDataSnapshot.getValue(
                    Item::class.java
                )!!
            )
        }
        newList = search(queryText, itemList);
        listView.adapter = ArrayAdapter<Item>(mActivity, android.R.layout.simple_list_item_1, newList);

    }

    //a search function depending on item Name
    private fun search(queryText: String, itemList: ArrayList<Item>):ArrayList<Item>{
        val newItemList: ArrayList<Item> = ArrayList()
        for (item in itemList){
            if (item.itemName.contains(queryText)){
                newItemList.add(item);
            }
        }
        return newItemList
    }
}