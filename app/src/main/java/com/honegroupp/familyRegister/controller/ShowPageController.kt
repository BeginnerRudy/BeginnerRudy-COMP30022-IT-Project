package com.honegroupp.familyRegister.controller

import android.widget.ImageButton
import com.honegroupp.familyRegister.model.Item

class ShowPageController {
    companion object{

        /**
         * This method is responsible for managing whether the item is in show page.
         *
         * */
        fun manageShow(showButton: ImageButton, item:Item, uid:String){
            item.manageShowItem(showButton, uid)
        }
    }
}