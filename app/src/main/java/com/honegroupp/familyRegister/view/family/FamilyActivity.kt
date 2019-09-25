package com.honegroupp.familyRegister.view.family

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.IDoubleClickToExit
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.FamilyController
import kotlinx.android.synthetic.main.activity_create_join_family.*

/**
 * This class is responsible for asking user choose to join or create a family for the first time
 * the user logs in.
 *
 * User click back button twice would exits the app when he/she is in this activity.
 * @author Renjie Meng
 * */
class FamilyActivity: AppCompatActivity(), IDoubleClickToExit{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_family)

        //get User ID
        val currUid= intent.getStringExtra("UserID")
        val username = intent.getStringExtra("UserName")

        // navigate to FamilyCreateActivity if click brn_create
        FamilyController.buttonClick(this, btn_create_family, FamilyCreateActivity::class.java, currUid, username)
        // navigate to FamilyJoinActivity if click brn_join
        FamilyController.buttonClick(this, btn_join_family, FamilyJoinActivity::class.java, currUid, username)
    }

    /**
     * Click back button twice to exit app.
     * */
    override fun onBackPressed() {
        doubleClickToExit(this)
    }
}