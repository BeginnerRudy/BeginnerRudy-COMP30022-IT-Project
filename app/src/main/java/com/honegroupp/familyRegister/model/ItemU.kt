package com.honegroupp.familyRegister.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

class ItemU(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("url")
    @get:PropertyName("url")
    var url: String = "",
    @set:PropertyName("description")
    @get:PropertyName("description")
    var description: String = ""
    ) {
    /*This constructor has no parameter, which is used to create ItemUpload while retrieve data from database*/
    @Exclude
    var key: String? = null
    constructor() : this("","","")

}