package com.honegroupp.familyRegister.model

import com.google.firebase.database.PropertyName

/**
 * This class is responsible for storing data and business logic for User
 *
 * @param name the name for the category
 * @param url the download url for the cover image of the category
 * @param count the count of number of items in the category
 * @attribute key
 *
 *
 * @author Renjie Meng
 * */

data class User(
    @set:PropertyName("account")
    @get:PropertyName("account")
    var account: Account
) {
    /*This constructor has no parameter, which is used to create CategoryUpload while retrieve data from database*/
    constructor() : this(Account())

    var uid: String = ""

}