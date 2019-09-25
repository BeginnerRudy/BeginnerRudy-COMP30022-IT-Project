package com.honegroupp.familyRegister.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.authentication.LoginActivity
import com.honegroupp.familyRegister.view.home.HomeActivity

class AuthenticationController {
    companion object {
        fun logout(btn_log_out: Button, mConntext: Context) {
            btn_log_out.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(mConntext)
                    .addOnCompleteListener {
                        Toast.makeText(
                            mConntext,
                            mConntext.getString(R.string.sign_out_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                val intent = Intent(mConntext, LoginActivity::class.java)
                mConntext.startActivity(intent)
            }
        }

        /**
         * This method is responsible for upload user to the database
         * */
        fun storeUser(mActivity: AppCompatActivity, user: User, uid: String) {
            mActivity.setContentView(R.layout.circular_progress_bar)

            user.store(mActivity, uid)
        }
    }
}