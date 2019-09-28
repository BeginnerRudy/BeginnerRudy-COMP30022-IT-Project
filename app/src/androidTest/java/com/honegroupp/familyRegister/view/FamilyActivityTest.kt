package com.honegroupp.familyRegister.view

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.view.family.FamilyActivity
import org.junit.Assert.*
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FamilyActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(FamilyActivity::class.java, false, false)

    @Test
    fun appLaunchesSuccessfully() {
//        ActivityScenario.launch(FamilyActivity::class.java)
        val intent = Intent()
        val uid = "qqqqqqqq@qq=com"
        val username = "qqqqqqqq"
        intent.putExtra("UserID", uid)
        intent.putExtra("UserName", username)
        activityRule.launchActivity(intent)
    }

}