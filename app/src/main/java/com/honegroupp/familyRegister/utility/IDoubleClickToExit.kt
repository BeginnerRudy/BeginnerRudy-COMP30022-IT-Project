package com.honegroupp.familyRegister.utility

import android.app.Activity
import android.os.Handler
import android.widget.Toast
import kotlin.system.exitProcess

/**
 * This interface is responsible for providing a default method of double click back to exit.
 * */

interface IDoubleClickToExit {

    companion object {
        var doubleBackToExitPressedOnce = false
    }

    /**
     * Click back button twice to exit app.
     * */
    fun doubleClickToExit(mActivity: Activity) {
        if (doubleBackToExitPressedOnce) {
            exitApp(mActivity)
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(mActivity, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    /**
     * To exit app.
     * */
    fun exitApp(mActivity: Activity) {
        mActivity.finish()
        mActivity.moveTaskToBack(true)
        exitProcess(0)
    }
}