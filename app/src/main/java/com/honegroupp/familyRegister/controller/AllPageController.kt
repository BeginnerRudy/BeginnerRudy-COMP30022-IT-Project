package com.honegroupp.familyRegister.controller

import androidx.fragment.app.Fragment
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.view.home.ContainerAdapter
import com.honegroupp.familyRegister.view.home.HomeActivity

class AllPageController {

    companion object {
        fun showAll(
            /**
             * show all items in family the user can see
             * */
            uid: String,
            showTabAdapter: ContainerAdapter,
            mActivity: HomeActivity,
            currFrag: Fragment
        ) {
            Family.showAll(uid, showTabAdapter, mActivity, currFrag)
        }

    }
}