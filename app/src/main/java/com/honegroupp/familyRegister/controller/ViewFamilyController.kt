package com.honegroupp.familyRegister.controller

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.home.ViewFamilyActivity
import com.honegroupp.familyRegister.view.home.ViewFamilyAdapter

class ViewFamilyController {
    companion object {
        /**
         * This method is responsible for show all family member in the view family page.
         *
         * */
        fun showAllMembersAndInfo(uid: String, mActivity: ViewFamilyActivity) {
            // a list to store user in the current family
            val users = ArrayList<User>()

            // setting the recycler view
            val recyclerView =
                mActivity.findViewById<RecyclerView>(R.id.family_member_recycler_view)
            val adapter = ViewFamilyAdapter(users, mActivity)
            recyclerView.adapter = adapter

            // Setting the recycler view
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(mActivity, 1)

            Family.showAllMembersAndInfo(uid, adapter, mActivity, users)
        }

        /**
         * This method is responsible for change the family name from the view family page
         * */
        fun changeFamilyName(uid: String, newFamilyName: String) {
            Family.changeName(uid, newFamilyName)
        }

        /**
         * This method is responsible for change the family password from the view family page
         * */
        fun changeFamilyPassword(uid: String, newFamilyPassword: String) {
            Family.changePassword(uid, newFamilyPassword)
        }

    }

}