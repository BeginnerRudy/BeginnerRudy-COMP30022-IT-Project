package com.honegroupp.familyRegister.view.family

import android.content.Intent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.R
import org.junit.*
import org.junit.runner.RunWith


/**
 * This class is responsible for unit test the FamilyActivity class
 *
 *
 * @author Renjie Meng
 * */
@RunWith(AndroidJUnit4::class)
class FamilyActivityTest {
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

    // The start test activity of this test class.
    @get:Rule
    val familyActivityRule = ActivityTestRule(FamilyActivity::class.java, false, false)

    /**
     * Test if the FamilyActivity launches successfully.
     * */
    @Test
    fun familyActivityLaunchesSuccessfully() {
        familyActivityRule.launchActivity(intent)
    }

    /**
     * Test if the button with if btn_create_family is displayed
     * */
    @Test
    fun onLaunchCheckButtonCreateFamilyIsDisplayed() {
        // 1 launch the FamilyActivity
        familyActivityRule.launchActivity(intent)

        // 2 get the view
        onView(withId(R.id.btn_create_family))
            // 3 check if it is displayed
            .check(matches(isDisplayed()))
    }

    /**
     * Test whether navigating to FamilyCreateActivity after btn_create_family is clicked
     * */
    @Test
    fun whenCreateFamilyButtonClicked() {
        // 1 launch the FamilyActivity
        familyActivityRule.launchActivity(intent)

        // 2 click the btn_create_family
        onView(withId(R.id.btn_create_family))
            .perform(click())

        // 3 Check if intent with FamilyCreateActivity it's been launched
        intended(hasComponent(FamilyCreateActivity::class.java!!.name))
    }

    /**
     * Test if the button with if btn_create_family is displayed
     * */
    @Test
    fun onLaunchCheckButtonJoinFamilyIsDisplayed() {
        // 1 launch the FamilyActivity
        familyActivityRule.launchActivity(intent)

        // 2 get the view
        onView(withId(R.id.btn_join_family))
            // 3 check if it is displayed
            .check(matches(isDisplayed()))
    }

    /**
     * Test whether navigating to FamilyCreateActivity after btn_create_family is clicked
     * */
    @Test
    fun whenJoinFamilyButtonClicked() {
        // 1 launch the FamilyActivity
        familyActivityRule.launchActivity(intent)

        // 2 click the btn_create_family
        onView(withId(R.id.btn_join_family))
            .perform(click())

        // Check if intent with FamilyJoinActivity it's been launched
        intended(hasComponent(FamilyJoinActivity::class.java!!.name))
    }
}