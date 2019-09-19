package com.honegroupp.familyRegister.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.PropertyName

data class Category(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("itemKeys")
    @get:PropertyName("itemKeys")
    var itemKeys: ArrayList<String> = ArrayList()
) {
    /*This constructor has no parameter, which is used to create CategoryUpload while retrieve data from database*/
    constructor() : this("",  ArrayList<String>())

}