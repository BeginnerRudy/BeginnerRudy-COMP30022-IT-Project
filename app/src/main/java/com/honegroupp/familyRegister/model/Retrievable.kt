package com.honegroupp.familyRegister.model

import com.google.firebase.database.DataSnapshot

interface Retrievable {
    /**/
    fun notifyDataRetrieveFnished(dataSnapshot: DataSnapshot)
}