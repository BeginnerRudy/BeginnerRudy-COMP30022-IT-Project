package com.honegroupp.familyRegister.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.PropertyName

data class Category(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("itemKeys")
    @get:PropertyName("itemKeys")
    var itemKeys: ArrayList<String> = ArrayList(),
    @set:PropertyName("count")
    @get:PropertyName("count")
    var count: Int = 0
) {
    /*This constructor has no parameter, which is used to create CategoryUpload while retrieve data from database*/
    constructor() : this("", ArrayList<String>(), 0)

    companion object {
        val DEFAULT_COVER = "DEFAULT_COVER"
    }

    fun getCoverURL(): String {
        var url = DEFAULT_COVER
        if (!itemKeys.isEmpty()) {
            url =
                "https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/t%2FFurniture%2Ft?alt=media&token=3d38101c-cd71-4dc2-8889-ca1b96a17dbf"
        }
        return url
    }
}