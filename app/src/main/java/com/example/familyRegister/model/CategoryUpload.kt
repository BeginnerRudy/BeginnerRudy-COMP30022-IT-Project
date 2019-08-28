package com.example.familyRegister.model

import com.google.firebase.database.PropertyName

/**
 * This class is responsible for storing data for each category in the Category Fragment
 *
 * @param name the name for the category
 * @param url the download url for the cover image of the category
 * @param count the count of number of items in the category
 * @attribute key
 *
 *
 * @author Renjie Meng
 * */

data class CategoryUpload(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("url")
    @get:PropertyName("url")
    var url: String,
    @set:PropertyName("count")
    @get:PropertyName("count")
    var count: String
) {
    /*This constructor has no parameter, which is used to create CategoryUpload while retrieve data from database*/
    constructor() : this("", "", "0")
}