package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ShowPageController
import kotlinx.android.synthetic.main.activity_item_list.view.*
import android.widget.RelativeLayout




class ShowTabFragment(private val showTabAdapter: ContainerAdapter) :
    Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
                inflater.inflate(R.layout.activity_item_list, container, false)

        // Align the recycler view to the top of the parent
        val params = view.item_list_recycler_view.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        view.item_list_recycler_view.layoutParams = params

        // Show all item liked
        val homeActivity = activity as HomeActivity
        val recyclerView =
                view.findViewById<RecyclerView>(R.id.item_list_recycler_view)

        // Setting the recycler view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(homeActivity, 2)


        // setting one ItemListAdapter
        recyclerView.adapter = showTabAdapter

        // set listener
        showTabAdapter.listener = homeActivity


        ShowPageController.showAllLiked(
            homeActivity,
            showTabAdapter,
            homeActivity.userID,
            this
        )

        // User could not add item from the show page
        view.btn_add.visibility = View.INVISIBLE
        view.btn_sort.visibility = View.INVISIBLE

        view.btn_search.visibility = View.INVISIBLE

        return view
    }

}