package com.honegroupp.familyRegister.model

import com.google.firebase.database.PropertyName

/**
 * This class is responsible for storing data and business logic for User
 *
 * @param account the account of the user
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