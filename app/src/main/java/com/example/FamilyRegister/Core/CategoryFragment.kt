package com.example.FamilyRegister.Core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.FamilyRegister.Model.CategoryUpload
import com.example.FamilyRegister.Model.ItemUpload
import com.example.FamilyRegister.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryFragment : Fragment() {
    companion object {
        val PLEASE_USE_DEFAULT_COVER = "Default Cover"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // Snippet from "Navigate to the next Fragment" section goes here.

        val view = inflater.inflate(R.layout.fragment_category, container, false)

        // retrieve all the category data from the database
        val categories = ArrayList<CategoryUpload>()

        val databaseReference = FirebaseDatabase.getInstance().getReference(RegisterFragment.uid + "/")
        val categoryAdapter = CategoryAdapter(categories, context!!)

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                toast(p0.message, Toast.LENGTH_SHORT)
//                progress_circular.visibility = View.INVISIBLE
            }


            override fun onDataChange(p0: DataSnapshot) {
                // clear it before filling it
                categories.clear()

                p0.children.forEach {
                    // Retrieve data from database
                    val mValue = it.value

                    // check the data
                    var currItemUpload: CategoryUpload?
                    if (mValue == RegisterFragment.fakeInitialValue) {
                        currItemUpload = CategoryUpload(
                            it.key.toString(),
                            PLEASE_USE_DEFAULT_COVER,
                            "0"
                        )
                    } else {
                        val downloadURL = (it.children.last().getValue(ItemUpload::class.java) as ItemUpload).url
                        val count = it.childrenCount.toString()
                        currItemUpload =
                            CategoryUpload(it.key.toString(), downloadURL, count)
                    }


                    // add the freshly created object to the categories list
                    currItemUpload.key = it.key
                    categories.add(currItemUpload)
                }

                // It would update recycler after loading image from firebase storage
                categoryAdapter.notifyDataSetChanged()
//                progress_circular.visibility = View.INVISIBLE
            }
        })

        // set it into the adapter
        view.category_recycler_view.layoutManager = GridLayoutManager(activity, 2)
        view.category_recycler_view.adapter = categoryAdapter

        return view
    }

    fun CategoryFragment.toast(msg: String, duration: Int) {
        Toast.makeText(activity, msg, duration).show()
    }
}