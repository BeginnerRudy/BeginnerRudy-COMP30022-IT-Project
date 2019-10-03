package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.CategoryController
import com.honegroupp.familyRegister.controller.ShowPageController


class ShowTabFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_item_list, container, false)

        // Show all item liked
        val homeActivity = activity as HomeActivity
        ShowPageController.showAllLiked(homeActivity, homeActivity.userID)

        return view
    }

}