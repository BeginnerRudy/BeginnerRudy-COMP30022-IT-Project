package com.honegroupp.familyRegister.backend

import android.util.Log
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.model.User
import com.google.firebase.FirebaseError
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.Toast
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Retrievable
import java.util.*
import kotlin.reflect.KClass
import com.google.firebase.database.DataSnapshot


class FirebaseDatabaseManager() {
    companion object {
        val USER_PATH = "/Users/"
        val FAMILY_PATH = "/Family/"

        /**
         * This method is responsible for retrieving object from the database depends on given path
         *
         * */
        // TODO should change class Any change to some Class more specific.
        fun retrieve(path: String, callback: (DataSnapshot) -> Unit){
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)
            // retrieve data

3
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //Don't ignore errors!
                    Log.d("TAG", p0.message)
                }
                override fun onDataChange(p0: DataSnapshot) {
                    callback(p0)
                }
            })
        }

        /**
         * This method is responsible for uploading the given user to  the database when user login.
         * */
        fun uploadUser(uid: String, user: User) {
            // TODO This logic should not be exposed in controller.
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

        /**
         * This method is responsible for uploading given family to specified path of the database.
         * */
        fun uploadFamily(family: Family) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(FAMILY_PATH)
            val uploadKey = databaseRef.push().key.toString()

            family.familyId = uploadKey

            databaseRef.child(uploadKey).setValue(family)
        }

        /**
         * This method is responsible for uploading given object to specified path of the database.
         * */
        fun update(path:String, obj:Any) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)

            databaseRef.child("").setValue(obj)
        }

    }
}