package com.honegroupp.familyRegister.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

class ItemDImage(@set:PropertyName("url")
    @get:PropertyName("url")
    var url: String = ""
) {
    /*This constructor has no parameter, which is used to create ItemUpload while retrieve data from database*/
    @Exclude
    var key: String? = null
    constructor() : this("")

}