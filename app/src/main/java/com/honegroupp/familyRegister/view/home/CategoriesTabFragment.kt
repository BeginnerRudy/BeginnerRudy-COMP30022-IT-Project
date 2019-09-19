package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.CategoryController


class CategoriesTabFragment : Fragment() {
    companion object {
        /*This constant is used as a flag, to show there is no cover image for the category*/
        /*And, it told the adapter to use the default cover for that categoty*/
        const val PLEASE_USE_DEFAULT_COVER = "Default Cover"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)


        // Show all categories
        CategoryController.showCategory((activity as HomeActivity).userID, view, (activity as AppCompatActivity))

        return view
    }

}