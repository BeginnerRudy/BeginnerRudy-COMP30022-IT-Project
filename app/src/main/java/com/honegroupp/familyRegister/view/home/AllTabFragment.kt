package com.honegroupp.familyRegister.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.AllPageController
import com.honegroupp.familyRegister.controller.ShowPageController
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.item.DetailSlide
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

        // set list for liked items
        val items = ArrayList<Item>()

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.all_item_list_recycler_view)

        // Setting the recycler view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(homeActivity, 2)


        // setting one ItemListAdapter
        val showTabAdapter = ContainerAdapter(items, homeActivity, ContainerAdapter.SHOWPAGE)
        recyclerView.adapter = showTabAdapter

        // set listener
        showTabAdapter.listener = homeActivity


        AllPageController.showAll(homeActivity.uid, items, showTabAdapter, homeActivity, this)
        return view
    }

}