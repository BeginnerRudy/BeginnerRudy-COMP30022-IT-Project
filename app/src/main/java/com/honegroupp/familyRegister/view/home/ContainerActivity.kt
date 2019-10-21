package com.honegroupp.familyRegister.view.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.controller.ItemListController
import com.honegroupp.familyRegister.view.item.DetailSlide
import com.honegroupp.familyRegister.view.itemList.ItemListActivity

/**
 * This class is responsible for the logic of activities which contains a list of items.
 *
 * */
open class ContainerActivity : AppCompatActivity(), ContainerAdapter.OnItemClickerListener {

    companion object {

        const val SORT_DEFAULT = "default"
        const val NAME_ASCENDING = "name_asc"
        const val NAME_DESCENDING = "name_desc"
        const val TIME_ASCENDING = "time_asc"
        const val TIME_DESCENDING = "time_desc"
    }

    lateinit var uid: String
    lateinit var familyId: String
    lateinit var path: String
    lateinit var categoryPosition: String

    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailSlide::class.java)
        intent.putExtra("UserID", uid)
        intent.putExtra("FamilyId", familyId)
        intent.putExtra("PositionList", position.toString())
        intent.putExtra("CategoryNameList", categoryPosition)
        startActivity(intent)
    }

    override fun onDeleteClick(itemId: String) {
        ItemListController.deleteItems(uid, categoryPosition, itemId, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get User ID
        uid = intent.getStringExtra("UserID")

        if (this is ItemListActivity) {
            categoryPosition = intent.getStringExtra("categoryPath")
        }
    }

}