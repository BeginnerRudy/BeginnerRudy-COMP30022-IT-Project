package com.honegroupp.familyRegister.controller

import com.honegroupp.familyRegister.model.Family
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.HomeActivity

class ShowPageController {
    companion object{

        /**
         * This method is responsible for managing whether the item is in show page.
         *
         * */
        fun manageShow(item:Item, uid:String){
            item.manageShowItem(uid)
        }

        /**
         * This method is responsible for displaying all liked items
         *
         * */
        fun showAllLiked(mActivity: HomeActivity, uid: String){
            Family.displayShowPage(mActivity, uid)
        }
    }
}