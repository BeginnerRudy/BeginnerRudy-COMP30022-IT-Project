package com.honegroupp.familyRegister

import android.app.Activity
import android.os.Handler
import android.widget.Toast
import kotlin.system.exitProcess

interface IDoubleClickToExit {
    /**
     * Click back button twice to exit app.
     * */
    companion object{
        var doubleBackToExitPressedOnce = false
    }

    fun doubleClickToExit(mActivity: Activity) {
        if (doubleBackToExitPressedOnce) {
            exitApp(mActivity)
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(mActivity, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    fun exitApp(mActivity: Activity){
        mActivity.finish()
        mActivity.moveTaskToBack(true)
        exitProcess(0)
    }
}