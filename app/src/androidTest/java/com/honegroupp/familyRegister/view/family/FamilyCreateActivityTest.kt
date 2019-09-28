package com.honegroupp.familyRegister.view.family

import android.content.Intent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.R
import com.honegroupp.utility.ToastMatcher
import kotlinx.android.synthetic.main.create_family_main.*
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText


/**
 * This class is responsible for unit test the FamilyActivity class
 *
 *
 * @author Renjie Meng
 * */
@RunWith(AndroidJUnit4::class)
class FamilyCreateActivityTest {
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
    val familyCreateActivityRule = ActivityTestRule(FamilyCreateActivity::class.java, false, false)

    /**
     * Test if the FamilyCreateActivity launches successfully.
     * */
    @Test
    fun familyActivityLaunchesSuccessfully() {
        familyCreateActivityRule.launchActivity(intent)
    }

    /**
     * Test when the user enter empty family input, whether the app would show a toast to remind
     * user for entering family name
     * */
    @Test
    fun whenEnterEmptyUserName_1() {
        // 1 launch FamilyCreateActivity
        familyCreateActivityRule.launchActivity(intent)

        // 2 enter empty family name and nothing else

        // 3 click button
        onView(withId(R.id.familyCreateConfirm))
            .perform(click())

        // 4 check whether show a toast message with string R.string.type_family_name
        onView(withText(R.string.type_family_name)).inRoot(
            withDecorView(
                not(
                    familyCreateActivityRule.activity.window.decorView
                )
            )
        ).check(matches(isDisplayed()))
    }

    /**
     * Test when the user enter empty family input, whether the app would show a toast to remind
     * user for entering family name
     * */
    @Test
    fun whenEnterEmptyUserName_2() {
        // 1 launch FamilyCreateActivity
        familyCreateActivityRule.launchActivity(intent)

        // 2 enter family name with a lot of blank space nothing else
        onView(withId(R.id.edit_text_family_name)).perform(replaceText("        "))

        // 3 click button
        onView(withId(R.id.familyCreateConfirm))
            .perform(click())

        // 4 check whether show a toast message with string R.string.type_family_name
        onView(withText(R.string.type_family_name)).inRoot(
            withDecorView(
                not(
                    familyCreateActivityRule.activity.window.decorView
                )
            )
        ).check(matches(isDisplayed()))
    }
}