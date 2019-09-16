package com.honegroupp.familyRegister.controller

import android.content.Context
import android.widget.Button
import com.honegroupp.familyRegister.model.User

class AuthenticationController{
    companion object{
        fun logout(btn_log_out: Button, mConntext:Context){
            btn_log_out.setOnClickListener {

            }
        }

        /**
         * This method is responsible for upload user to the database
         * */
        fun storeUser(user: User, uid:String){
            user.store(uid)
        }
    }
}