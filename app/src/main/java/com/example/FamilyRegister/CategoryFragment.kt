package com.example.FamilyRegister

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.fragment_register.*

class CategoryFragment() : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // Snippet from "Navigate to the next Fragment" section goes here.

        val view = inflater.inflate(R.layout.fragment_category, container, false)

        // retrieve all the category data from the database
        val categories = ArrayList<Upload>()
        // put them into category objects
        RegisterFragment.defaultCategories.forEach{
            val category = Upload(it, R.drawable.furniture_default.toString())
            categories.add(category)
        }
        // set it into the adapter
        val categoryAdapter = CategoryAdapter(categories, context!!)
        view.category_recycler_view.layoutManager = GridLayoutManager(activity, 2)
        view.category_recycler_view.adapter = categoryAdapter

        return view
    }
}