package com.example.FamilyRegister.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class ItemUpload(
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("url")
    @get:PropertyName("url")
    var url: String
) {
    @Exclude var key: String? = null
    constructor() : this("", "")
}