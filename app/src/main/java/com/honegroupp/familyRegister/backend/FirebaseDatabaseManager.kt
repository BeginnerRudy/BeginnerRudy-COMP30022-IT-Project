package com.honegroupp.familyRegister.backend

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.model.User

class FirebaseDatabaseManager() {
    companion object {
        val USER_PATH = "/Users/"


        /**
         * This method is responsible for uploading given object to specified path of the database.
         * */
        fun uploadUser(uid: String, user: User) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(USER_PATH)

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //Don't ignore errors!
                    Log.d("TAG", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var isExist = false

                    p0.children.forEach {
                        // Check if the user has already exists
                        if (it.key == uid) {
                            isExist = true
                        }
                    }

                    // if the user hasn't been recorded ,
                    // Use the uid to construct the user's uiq path on database
                    if (!isExist){
                        databaseRef.child(uid).setValue(user)
                    }

                }


            })
        }
    }
}