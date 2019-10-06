package com.honegroupp.familyRegister.controller

import androidx.fragment.app.Fragment
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.view.home.HomeActivity

class AllPageController {
    companion object{
        fun showAll(uid: String, mActivity: HomeActivity, currFrag: Fragment){
            Family.showAll(uid, mActivity, currFrag)
        }
    }
}