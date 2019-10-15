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
import com.honegroupp.familyRegister.view.itemList.ItemListActivity

class AllPageController {
    companion object {
        fun showAll(
            uid: String,
            showTabAdapter: ContainerAdapter,
            mActivity: HomeActivity,
            currFrag: Fragment
        ) {
            Family.showAll(uid, showTabAdapter, mActivity, currFrag)
        }

    }
}