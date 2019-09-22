//package com.honegroupp.familyRegister.model
//
//
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.database.DataSnapshot
//import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
//import com.honegroupp.familyRegister.controller.FamilyController
//import com.honegroupp.familyRegister.model.Hash.Companion.applyHash
//import com.honegroupp.familyRegister.model.Hash.Companion.salt
//import kotlin.random.Random
//
//
//class FamilyIDGenerator{
//
//
//    val MAX_ID = 1000000
//
//    //generate UniqueID for family, without repetition
//    fun generateUniqueID():Int{
//        var id: Int = getRandomNumber()
//        while (checkAvailability(id) == false){
//            id = getRandomNumber()
//        }
//        return id
//    }
//
//    //generate ID randomly in the range
//    private fun getRandomNumber(): Int{
//        return Random.nextInt(0, MAX_ID)
//    }
//
//
//    //check whether the id is used by other family
//    //return true if the id is available (not used)
//    private fun checkAvailability(id: Int): Boolean{
//
//
//        FirebaseDatabaseManager.retrieve(
//            FirebaseDatabaseManager.FAMILY_PATH
//        ) { d: DataSnapshot ->
//            FamilyIDGenerator.callbackJoinFamily(
//                id.toString(),
//                d
//            )
//        }
//
//
//        return true
//    }
//
//
//
//    private fun callbackJoinFamily(
//
//        familyIdInput: String,
//        dataSnapshot: DataSnapshot
//    ) {
//        // Check whether family exist
//        if (!dataSnapshot.hasChild(familyIdInput)) {
//            //return false
//        }else{
//            //return true
//        }
//    }
//
//}