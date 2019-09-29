package com.honegroupp.familyRegister.view.home

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.R
import com.honegroupp.utility.ThreadController
import org.junit.*
import org.junit.runner.RunWith

class CategoriesFragmentTabTest {
    companion object {
        // variables you initialize for the class later in the @BeforeClass method:
        lateinit var intent: Intent

        @BeforeClass
        @JvmStatic
        fun setup() {
            // things to execute once and keep around for the class
            intent = Intent()
            val uid = "qqqqqqqq@qq=com"
            val username = "qqqqqqqq"
            intent.putExtra("UserID", uid)
            intent.putExtra("UserName", username)

            Intents.init()
        }


        @AfterClass
        @JvmStatic
        fun teardown() {
            // clean up after this class, leave nothing dirty behind
        }
    }

    @get:Rule
    val homeActivityRule = ActivityTestRule(HomeActivity::class.java, false, false)
    val categoryNames = arrayListOf<String>("Letter", "Instrument", "Photo", "Others")

    @Before
    fun prepareTest() {
        homeActivityRule.launchActivity(intent)

        onView(withText(R.string.category))
            // 3 check if it is displayed
            .perform(click())


        // wait for the categories are loaded
        ThreadController.stopForNMilliseconds(3000)
    }

    /**
     * Verify that all 4 categories display correctly.
     * */
    @Test
    fun allCategoriesDisplayed() {
        for (category in categoryNames){
            onView(withText(category))
                // check if it is displayed
                .check(matches(isDisplayed()))
        }
    }

    /**
     * Verify that all 4 categories are clickable.
     * */
    @Test
    fun allCategoriesClickable() {
        for (category in categoryNames){
            onView(withText(category))
                .perform(click())
            pressBack()
        }
    }


}