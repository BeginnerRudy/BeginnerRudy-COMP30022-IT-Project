package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.AllPageController
import com.honegroupp.familyRegister.controller.ShowPageController
import kotlinx.android.synthetic.main.activity_item_list.view.*


class AllTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all, container, false)

        // Show all item liked
        val homeActivity = activity as HomeActivity
        AllPageController.showAll(homeActivity.uid, homeActivity, this)

        return view
    }

}