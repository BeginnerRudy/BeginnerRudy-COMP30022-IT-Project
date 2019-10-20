package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.AllPageController

/**
 * This class is responsible for All_page tab in the home page
 *
 * */
class AllTabFragment(private val showTabAdapter: ContainerAdapter) :
    Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all, container, false)

        // Show all item liked
        val homeActivity = activity as HomeActivity


        val recyclerView =
                view
                    .findViewById<RecyclerView>(R.id.all_item_list_recycler_view)

        // Setting the recycler view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(homeActivity, 2)

        // setting one ItemListAdapter
        recyclerView.adapter = showTabAdapter

        // set listener
        showTabAdapter.listener = homeActivity


        AllPageController
            .showAll(homeActivity.uid, showTabAdapter, homeActivity, this)
        return view
    }

}