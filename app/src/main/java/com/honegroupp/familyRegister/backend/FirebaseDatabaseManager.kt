package com.honegroupp.familyRegister.backend

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.model.Category
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.family.FamilyActivity
import com.honegroupp.familyRegister.view.home.HomeActivity


class FirebaseDatabaseManager() {
    companion object {
        val USER_PATH = "/Users/"
        val FAMILY_PATH = "/Family/"
        val NOTHING = "=_="

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
                    // remove this listener, since we only need to use DataSnapshot once
                    databaseRef.removeEventListener(this)
                    callback(p0)
                }
            })
        }

        /**
         * This method is responsible for retrieving object from the database depends on given path.
         * This would be called whenever the dataSnapshot is changed
         * */
        // TODO should change class Any change to some Class more specific.
        fun retrieveLive(path: String, callback: (DataSnapshot) -> Unit) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)
            // retrieve data


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
        fun uploadUser(mActivity: AppCompatActivity, uid: String, user: User) {
            val databaseRef =
                    FirebaseDatabase.getInstance().getReference(USER_PATH)

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //Don't ignore errors!
                    Log.d("TAG", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {

                    var isExist = false
                    var intent = Intent(mActivity, FamilyActivity::class.java)
                    var username = user.username

                    p0.children.forEach {
                        // Check if the user has already exists
                        if (it.key == uid) {
                            isExist = true
                            val currUser = it.getValue(User::class.java) as User

                            // If the user has no family go to FamilyActivity
                            // otherwise go to HomeActivity
                            if (currUser.hasFamily()) {
                                intent = Intent(
                                    mActivity,
                                    HomeActivity::class.java)
                                username = currUser.username
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
                    intent.putExtra("UserName", user.username)

                    mActivity.startActivity(intent)
                }
            })


        }

        /**
         * This method is responsible for uploading given family to specified path of the database.
         * */
        fun uploadFamily(family: Family, uid: String) {
            val databaseRef =
                    FirebaseDatabase.getInstance().getReference(FAMILY_PATH)
            databaseRef.child(family.familyId).setValue(family)
        }

        /**
         * This method is responsible for uploading given item to specified path of the database.
         * */
        fun uploadItem(
            item: Item,
            path: String,
            items: HashMap<String, Item>,
            categoryPath: String
        ) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)
            val itemKey = databaseRef.push().key.toString()

            // add current item to the items Hashmap
            items[itemKey] = item

            // add current's key to the corresponding category
            val categoryDatabaseReference =
                    FirebaseDatabase.getInstance().getReference(categoryPath)
            categoryDatabaseReference
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        //Don't ignore errors!
                        Log.d("TAG", p0.message)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        // remove listener, since we only want to call this listener once.
                        categoryDatabaseReference.removeEventListener(this)

                        val category =
                                p0.child("").getValue(Category::class.java) as Category
                        // add new item key to category and update counts
                        category.itemKeys.add(itemKey)
                        category.count = category.itemKeys.size

                        // update category to Firebase
                        update(categoryPath, category)

                    }
                })


            // upload items to the Firebase
            databaseRef.child("items").setValue(items)
        }

        /**
         * This method is responsible for edit given item to specified path of the database.
         * */
        fun UeditItem(
            item: Item,
            path: String,
            items: HashMap<String, Item>,
            categoryPath: String
        ) {
            // reference of family
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)
            val itemKey = databaseRef.push().key.toString()

            // add current item to the items Hashmap
            items[itemKey] = item

            // add current's key to the corresponding category
            val categoryDatabaseReference =
                    FirebaseDatabase.getInstance().getReference(categoryPath)
            categoryDatabaseReference
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        //Don't ignore errors!
                        Log.d("TAG", p0.message)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        // remove listener, since we only want to call this listener once.
                        categoryDatabaseReference.removeEventListener(this)

                        val category =
                                p0.child("").getValue(Category::class.java) as Category
                        // add new item key to category and update counts
                        category.itemKeys.add(itemKey)
                        category.count = category.itemKeys.size

                        // update category to Firebase
                        update(categoryPath, category)

                    }
                })


            // upload items to the Firebase
            databaseRef.child("items").setValue(items)
        }

        /**
         * This method is responsible for uploading given object to specified path of the database.
         * */
        fun update(path: String, obj: Any) {
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)

            databaseRef.child("").setValue(obj)
        }

        /**
         * This method is responsible for getting family id by uid
         *
         * */
        fun getFamilyIDByUID(uid: String, dataSnapshot: DataSnapshot): String {
            val currFamilyId =
                    dataSnapshot.child(FirebaseDatabaseManager.USER_PATH).child(
                        uid).child("familyId").getValue(
                        String::class.java
                    ) as String

            return currFamilyId
        }

    }
}