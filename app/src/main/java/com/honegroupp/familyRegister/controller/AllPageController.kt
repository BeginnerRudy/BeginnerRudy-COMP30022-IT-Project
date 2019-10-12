package com.honegroupp.familyRegister.controller

import androidx.fragment.app.Fragment
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.home.HomeActivity

class AllPageController {
    companion object {
        fun showAll(
            uid: String,
            items: ArrayList<Item>,
            showTabAdapter: ContainerAdapter,
            mActivity: HomeActivity,
            currFrag: Fragment
        ) {
            Family.showAll(uid, items, showTabAdapter, mActivity, currFrag)
        }
    }
}