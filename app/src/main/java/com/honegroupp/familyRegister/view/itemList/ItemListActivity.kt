package com.honegroupp.familyRegister.view.itemList

import android.os.Bundle
import com.google.firebase.storage.FirebaseStorage
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ItemListController
import com.honegroupp.familyRegister.view.home.ContainerActivity

class ItemListActivity : ContainerActivity() {
    var storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        // add item logic
        ItemListController.addItem(uid, categoryName, this)

        // show items in the category logic
        ItemListController.showItems(uid, categoryName, this)

    }
}