package com.honegroupp.familyRegister.view

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.view.family.FamilyActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FamilyActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(FamilyActivity::class.java, false, false)

}