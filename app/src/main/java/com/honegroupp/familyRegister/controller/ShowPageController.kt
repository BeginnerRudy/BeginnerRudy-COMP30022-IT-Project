package com.honegroupp.familyRegister.controller

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.ContainerActivity
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.home.HomeActivity

class ShowPageController {
    companion object {

        /**
         * This method is responsible for managing whether the item is in show page.
         *
         * */
        fun manageShow(item: Item, uid: String) {
            item.manageShowItem(uid)
        }

        /**
         * This method is responsible for displaying all liked items
         *
         * */
        fun showAllLiked(
            mActivity: HomeActivity,
            items: ArrayList<Item>,
            showTabAdapter: ContainerAdapter,
            uid: String,
            currFrag: Fragment
        ) {
            // set the sorter logic
            sortItem(mActivity, showTabAdapter)

            Family.displayShowPage(mActivity, items, showTabAdapter, uid, currFrag)
        }

        //activity adapter
        fun sortItem(
            mActivity: HomeActivity,
            adapter: ContainerAdapter
        ) {
            val drawerSortLayout = mActivity.findViewById<DrawerLayout>(R.id.drawer_layout)
            val navi_sort_view = mActivity.findViewById<NavigationView>(R.id.navi_home_sort_view)
            navi_sort_view.menu.findItem(R.id.sort_name_asc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortBy { it.itemName }
                //update sort order
                mActivity.sortOrder = ContainerActivity.NAME_ASCENDING
                // update the UI layer
                updateRecyclerView(adapter, drawerSortLayout)
                true
            }
            navi_sort_view.menu.findItem(R.id.sort_name_desc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortByDescending { it.itemName }
                //update sort order
                mActivity.sortOrder = ContainerActivity.NAME_DESCENDING
                // update the UI layer
                updateRecyclerView(adapter, drawerSortLayout)
                true
            }
            navi_sort_view.menu.findItem(R.id.sort_time_asc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortBy { it.date }
                //update sort order
                mActivity.sortOrder = ContainerActivity.TIME_ASCENDING
                // update the UI layer
                updateRecyclerView(adapter, drawerSortLayout)
                true
            }
            navi_sort_view.menu.findItem(R.id.sort_time_desc).setOnMenuItemClickListener {
                //sort logic
                adapter.items.sortByDescending { it.date }
                //update sort order
                mActivity.sortOrder = ContainerActivity.TIME_DESCENDING
                // update the UI layer
                updateRecyclerView(adapter, drawerSortLayout)
                true
            }

        }

        fun updateRecyclerView(adapter: ContainerAdapter, drawer_sort_layout: DrawerLayout) {
            // hide the item
            drawer_sort_layout.closeDrawer(GravityCompat.END)
            // update the recycler view
            adapter.notifyDataSetChanged()
        }
    }
}