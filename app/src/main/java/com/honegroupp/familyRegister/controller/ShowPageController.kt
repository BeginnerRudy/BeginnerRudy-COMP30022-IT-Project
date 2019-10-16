package com.honegroupp.familyRegister.controller

import androidx.fragment.app.Fragment
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
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
            showTabAdapter: ContainerAdapter,
            uid: String,
            currFrag: Fragment
        ) {
            Family.displayShowPage(mActivity, showTabAdapter, uid, currFrag)
        }
    }
}