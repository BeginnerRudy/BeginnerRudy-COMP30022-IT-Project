package com.honegroupp.familyRegister.controller

import android.content.Context
import android.content.Intent
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
            // TODO Show a progress bar when start in the center
            val circularProgressBar = ProgressBar(mActivity)
            val rlp = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            rlp.addRule(RelativeLayout.CENTER_IN_PARENT)

            mActivity.addContentView(circularProgressBar,rlp)

            user.store(mActivity, uid)
        }
    }
}