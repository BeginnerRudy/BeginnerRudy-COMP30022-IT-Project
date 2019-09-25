package com.honegroupp.familyRegister.utility

import android.R
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.utility.ListViewAapter

class SearchMethod{

    /**
     * This function used to create initial view for search activity
     * */
    fun init(mActivity: AppCompatActivity, listView: ListView, uid: String){
        //get whole snapshot of database
        val rootPath = "/"

        //get datasnapshot and create view in callback function
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackInit(mActivity, uid, listView, d) }
    }

    /**
     * This callback function is the help function to create search view*/
    private fun callbackInit(mActivity: AppCompatActivity, uid:String, listView: ListView, dataSnapshot: DataSnapshot){

        var itemList: ArrayList<Item> = ArrayList()

        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        //get itemlist datasnapshot
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

        //set adapter to show items
        listView.adapter = ListViewAapter(itemList, mActivity)
    }

    /**
     * This function used to create initial view for search activity
     * */
    fun doSearch(mActivity: AppCompatActivity, listView: ListView, uid: String, queryText: String){
        //get whole snapshot of database
        val rootPath = "/"

        //get datasnapshot and create view in callback function
        FirebaseDatabaseManager.retrieve(
            rootPath
        ) { d: DataSnapshot -> callbackDoSearch(mActivity, uid, queryText, listView, d) }
    }

    /**
     * This function is the help function to show search view with query*/
    private fun callbackDoSearch(mActivity: AppCompatActivity, uid: String, queryText: String, listView: ListView, dataSnapshot: DataSnapshot) {

        var itemList: ArrayList<Item> = ArrayList()
        var newList: ArrayList<Item> = ArrayList()

        // get user's family ID
        val currFamilyId =
            dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(uid).child("familyId").getValue(
                String::class.java
            ) as String

        //get itemlist datasnapshot
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

        //get final list after search
        newList = search(queryText, itemList)

        //set adapter to show items
        listView.adapter = ListViewAapter(newList, mActivity)
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