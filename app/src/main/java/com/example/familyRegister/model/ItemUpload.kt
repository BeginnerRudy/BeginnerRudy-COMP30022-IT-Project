package com.example.familyRegister.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

/**
 * This class is responsible for storing data for each item in the itemList Activity
 *
 * @param name the name for the item
 * @param url the download url for the cover image of the item
 * @param description the description of the items
 *
 *
 * @author Renjie Meng
 * */

data class ItemUpload(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("url")
    @get:PropertyName("url")
    var url: String,
    @set:PropertyName("description")
    @get:PropertyName("description")
    var description: String
) {
    /*key is the identity key in the database for this object, mainly used for item deletion*/
    @Exclude var key: String? = null
    constructor() : this("", "", "None")
}