package com.honegroupp.utility

import android.content.Intent
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.item.DetailSlide
import com.honegroupp.familyRegister.view.utility.ListViewAapter
import com.honegroupp.familyRegister.view.utility.SearchActivity

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

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(mActivity, DetailSlide::class.java)
            intent.putExtra("UserID", uid)
            intent.putExtra("PositionList", listView.adapter.getItemId(position).toString())
            intent.putExtra("CategoryNameList", "1")
            intent.putExtra("FamilyId", currFamilyId)
            mActivity.startActivity(intent)
        }
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

        var itemPositionMap : HashMap<Item, Int> = HashMap()

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

        for (i in 0 until itemList.size){
            itemPositionMap[itemList[i]] = i
        }


        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(mActivity,position.toString(),Toast.LENGTH_SHORT)
            val element = listView.adapter.getItem(listView.adapter.getItemId(position).toInt()) // The item that was clicked
            val intent = Intent(mActivity, DetailSlide::class.java)
            intent.putExtra("UserID", uid)
            intent.putExtra("PositionList", itemPositionMap[element].toString())
            intent.putExtra("CategoryNameList", "1")
            intent.putExtra("FamilyId", currFamilyId)
            mActivity.startActivity(intent)
        }
    }

    //a search function depending on item Name
    fun search(queryText: String, itemList: ArrayList<Item>):ArrayList<Item>{
        val newItemList: ArrayList<Item> = ArrayList()
        for (item in itemList){
            if (item.itemName.contains(queryText)){
                newItemList.add(item);
            }
        }
        return newItemList
    }


}