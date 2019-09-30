package com.honegroupp.familyRegister.view.itemList

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.view.home.HomeActivity
import com.honegroupp.utility.ThreadController
import org.junit.*
import org.junit.runner.RunWith

/**
 * This class is responsible for testing ItemListActivity
 *
 * */
class ItemListActivityTest{
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
     * Verify that when the category is empty, the item list shows nothing but only a text view
     * */
    @Test
    fun emptyCategory(){
        // click the empty category
        onView(withText("Letter"))
            .perform(click())
        // verify if the text view is displayed.
        onView(withId(R.id.text_view_empty_category))
            .check(matches(isDisplayed()))
    }

    /**
     * Verify every item shows in the item list are all clickable.
     * */
    @Test
    fun allItemClickable(){
        // click the empty category
        onView(withText("Photo"))
            .perform(click())

        val count = 1
        for (i in 0..count){
            // click each item in the category
            onView(withId(R.id.item_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))
            pressBack()
        }
    }
}