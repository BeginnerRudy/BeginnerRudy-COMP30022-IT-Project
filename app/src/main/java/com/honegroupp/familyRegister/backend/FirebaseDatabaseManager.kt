package com.honegroupp.familyRegister.backend

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.model.Family
import com.google.firebase.database.DataSnapshot
import com.honegroupp.familyRegister.view.home.HomeActivity
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.family.FamilyActivity


class FirebaseDatabaseManager() {
    companion object {
        val USER_PATH = "/Users/"
        val FAMILY_PATH = "/Family/"

        /**
         * This method is responsible for retrieving object from the database depends on given path
         *
         * */
        // TODO should change class Any change to some Class more specific.
        fun retrieve(path: String, callback: (DataSnapshot) -> Unit) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)
            // retrieve data


            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //Don't ignore errors!
                    Log.d("TAG", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    callback(p0)
                    databaseRef.removeEventListener(this)
                }
            })
        }

        /**
         * This method is responsible for uploading the given user to  the database when user login.
         * */
        fun uploadUser(mActivity: AppCompatActivity, uid: String, user: User) {
            // TODO This logic should not be exposed in controller.
            val databaseRef = FirebaseDatabase.getInstance().getReference(USER_PATH)

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //Don't ignore errors!
                    Log.d("TAG", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var isExist = false
                    var intent = Intent(mActivity, FamilyActivity::class.java)

                    p0.children.forEach {
                        // Check if the user has already exists
                        if (it.key == uid) {
                            isExist = true
                            val currUser = it.getValue(User::class.java) as User

                            // If the user has no family go to FamilyActivity
                            // otherwise go to HomeActivity
                            if (currUser.hasFamily()) {
                                intent = Intent(mActivity, HomeActivity::class.java)
                            }
                        }
                    }

                    // remove listener, since we only want to call this listener once.
                    databaseRef.removeEventListener(this)

                    // if the user hasn't been recorded ,
                    // Use the uid to construct the user's uiq path on database
                    if (!isExist) {
                        databaseRef.child(uid).setValue(user)
                    }

                    //  pass user id to next activity
                    intent.putExtra("UserID", uid)


                    mActivity.startActivity(intent)
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
         * This method is responsible for uploading given item to specified path of the database.
         * */
        fun uploadItem(item: Item, path: String, lastIndex: Int) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)

//            databaseRef.child("item").setValue("")
            databaseRef.child("items").child(lastIndex.toString()).setValue(item)
        }

        /**
         * This method is responsible for uploading given object to specified path of the database.
         * */
        fun update(path: String, obj: Any) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)

            databaseRef.child("").setValue(obj)
        }

    }
}