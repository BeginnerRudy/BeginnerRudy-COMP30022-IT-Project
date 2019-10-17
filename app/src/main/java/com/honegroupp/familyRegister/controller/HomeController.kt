package com.honegroupp.familyRegister.controller

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.view.home.AllTabFragment
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.home.HomeActivity

class HomeController {
    companion object {
        //activity adapter
        fun sortItem(
            allTabFragment: AllTabFragment,
            allTabAdapter: ContainerAdapter,
            showTabAdapter: ContainerAdapter,
            mActivity: HomeActivity
        ) {
            //            var adapter: ContainerAdapter? = null


            val drawerSortLayout =
                    mActivity.findViewById<DrawerLayout>(R.id.drawer_layout)
            val navi_sort_view = mActivity
                .findViewById<NavigationView>(R.id.navi_home_sort_view)


            navi_sort_view.menu.findItem(R.id.sort_name_asc)
                .setOnMenuItemClickListener {
                    val adapter = if (allTabFragment.isVisible) {
                        allTabAdapter
                    } else {
                        showTabAdapter
                    }

                    //sort logic
                    adapter.items.sortBy { it.itemName }
                    //update sort order
                    if (allTabFragment.isVisible) {
                        mActivity.sortOrderAll =
                                ContainerActivity.NAME_ASCENDING
                    } else {
                        mActivity.sortOrderShow =
                                ContainerActivity.NAME_ASCENDING
                    }
                    // update the UI layer
                    updateRecyclerView(adapter, drawerSortLayout)
                    true
                }


            navi_sort_view.menu.findItem(R.id.sort_name_desc)
                .setOnMenuItemClickListener {
                    val adapter = if (allTabFragment.isVisible) {
                        allTabAdapter
                    } else {
                        showTabAdapter
                    }
                    //sort logic
                    adapter.items.sortByDescending { it.itemName }
                    //update sort order
                    if (allTabFragment.isVisible) {
                        mActivity.sortOrderAll =
                                ContainerActivity.NAME_DESCENDING
                    } else {
                        mActivity.sortOrderShow =
                                ContainerActivity.NAME_DESCENDING
                    }
                    // update the UI layer
                    updateRecyclerView(adapter, drawerSortLayout)
                    true
                }

            navi_sort_view.menu.findItem(R.id.sort_time_asc)
                .setOnMenuItemClickListener {
                    val adapter = if (allTabFragment.isVisible) {
                        allTabAdapter
                    } else {
                        showTabAdapter
                    }
                    //sort logic
                    adapter.items.sortBy { it.date }

                    //update sort order
                    if (allTabFragment.isVisible) {
                        mActivity.sortOrderAll =
                                ContainerActivity.TIME_ASCENDING
                    } else {
                        mActivity.sortOrderShow =
                                ContainerActivity.TIME_ASCENDING
                    }

                    // update the UI layer
                    updateRecyclerView(adapter, drawerSortLayout)
                    true
                }
            navi_sort_view.menu.findItem(R.id.sort_time_desc)
                .setOnMenuItemClickListener {
                    val adapter = if (allTabFragment.isVisible) {
                        allTabAdapter
                    } else {
                        showTabAdapter
                    }
                    //sort logic
                    adapter.items.sortByDescending { it.date }

                    //update sort order
                    if (allTabFragment.isVisible) {
                        mActivity.sortOrderAll =
                                ContainerActivity.TIME_DESCENDING
                    } else {
                        mActivity.sortOrderShow =
                                ContainerActivity.TIME_DESCENDING
                    }

                    // update the UI layer
                    updateRecyclerView(adapter, drawerSortLayout)
                    true
                }

        }

        /**
         * This method is responsible for updating the corresponding recycler view
         *
         * */
        private fun updateRecyclerView(
            adapter: ContainerAdapter,
            drawer_sort_layout: DrawerLayout
        ) {
            // hide the item
            drawer_sort_layout.closeDrawer(GravityCompat.END)
            // update the recycler view
            adapter.notifyDataSetChanged()
        }

    }
}