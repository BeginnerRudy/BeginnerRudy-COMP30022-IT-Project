package com.honegroupp.familyRegister.view.family

import android.content.Intent
import android.util.Log
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.honegroupp.familyRegister.R
import org.junit.*
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.Hash
import com.honegroupp.utility.ThreadController
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals


/**
 * This class is responsible for unit test the FamilyJoinActivity class
 *
 *
 * @author Renjie Meng
 * */
@RunWith(AndroidJUnit4::class)
class FamilyJoinActivityTest {
    companion object {
        // variables you initialize for the class later in the @BeforeClass method:
        lateinit var intent: Intent
        lateinit var uid: String

        @BeforeClass
        @JvmStatic
        fun setup() {
            // things to execute once and keep around for the class
            intent = Intent()
            uid = "qqq@qq=com"
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
    val familyJoinActivityRule = ActivityTestRule(FamilyJoinActivity::class.java, false, false)

    /**
     * Test if the FamilyCreateActivity launches successfully.
     * */
    @Test
    fun familyActivityLaunchesSuccessfully() {
        familyJoinActivityRule.launchActivity(intent)
    }

    /**
     * Test when the user enter an non-existing family id
     * */
    @Test
    fun enterNonExistingFamilyId() {
        // launch FamilyJoinActivity
        familyJoinActivityRule.launchActivity(intent)

        // Enter a non-existing family id
        onView(withId(R.id.edit_text_family_id_input)).perform(replaceText("non-existing family id"))

        // Click the Confirm Join Family Button
        onView(withId(R.id.familyJoinConfirm))
            .perform(click())

        // Check the shown toast
        onView(withText(R.string.family_id_not_exist)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(
                    familyJoinActivityRule.activity.window.decorView
                )
            )
        ).check(ViewAssertions.matches(isDisplayed()))

        // wait until the toast message disappear
        ThreadController.stopForNMilliseconds(ThreadController.SHORT_TOAST_WAITING)
    }

    /**
     * Test when the user enter an existing family id
     * */
    @Test
    fun enterExistingFamilyId() {
        val familyId = "qqqqqqqq@qq=com"
        // launch FamilyJoinActivity
        familyJoinActivityRule.launchActivity(intent)

        // Enter an existing family id
        onView(withId(R.id.edit_text_family_id_input)).perform(replaceText(familyId))

        // Click the Confirm Join Family Button
        onView(withId(R.id.familyJoinConfirm))
            .perform(click())

        // Check the shown toast
        onView(withText(R.string.password_is_incorrect)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(
                    familyJoinActivityRule.activity.window.decorView
                )
            )
        ).check(ViewAssertions.matches(isDisplayed()))

        // wait until the toast message disappear
        ThreadController.stopForNMilliseconds(ThreadController.SHORT_TOAST_WAITING)
    }

    /**
     * Test when the user enter an existing family id and an incorrect password
     * */
    @Test
    fun enterExistingFamilyIdWithIncorrectPassword() {
        val familyId = "qqqqqqqq@qq=com"
        val password = "incorrect password"
        // launch FamilyJoinActivity
        familyJoinActivityRule.launchActivity(intent)

        // Enter an existing family id
        onView(withId(R.id.edit_text_family_id_input)).perform(replaceText(familyId))

        // Enter an incorrect password
        onView(withId(R.id.edit_text_family_password_input)).perform(replaceText(password))

        // Click the Confirm Join Family Button
        onView(withId(R.id.familyJoinConfirm))
            .perform(click())

        // Check the shown toast
        onView(withText(R.string.password_is_incorrect)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(
                    familyJoinActivityRule.activity.window.decorView
                )
            )
        ).check(ViewAssertions.matches(isDisplayed()))

        // wait until the toast message disappear
        ThreadController.stopForNMilliseconds(ThreadController.SHORT_TOAST_WAITING)
    }

    /**
     * Test when the user enter an existing family id and a correct password
     * */
    @Test
    fun enterExistingFamilyIdWithCorrectPassword() {
        val familyId = "qqqqqqqq@qq=com"
        val password = "qqqqqqqq"
        // launch FamilyJoinActivity
        familyJoinActivityRule.launchActivity(intent)

        // Enter an existing family id
        onView(withId(R.id.edit_text_family_id_input)).perform(replaceText(familyId))

        // Enter an incorrect password
        onView(withId(R.id.edit_text_family_password_input)).perform(replaceText(password))

        // Click the Confirm Join Family Button
        onView(withId(R.id.familyJoinConfirm))
            .perform(click())

        // Check the shown toast
        onView(withText(R.string.join_family_successfully)).inRoot(
            RootMatchers.withDecorView(
                CoreMatchers.not(
                    familyJoinActivityRule.activity.window.decorView
                )
            )
        ).check(ViewAssertions.matches(isDisplayed()))

        // 5 check the family on the database has this user as its member
        val path = FirebaseDatabaseManager.FAMILY_PATH + familyId + "/"
        val databaseRef = FirebaseDatabase.getInstance().getReference(path)

        // retrieve data
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Don't ignore errors!
                Log.d("TAG", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                // remove this listener, since we only need to use DataSnapshot once
                databaseRef.removeEventListener(this)

                // this user is in the family
                assert(p0.child("members").hasChild(uid))
            }
        })

        val path2 = FirebaseDatabaseManager.USER_PATH + uid + "/"
        val databaseRef2 = FirebaseDatabase.getInstance().getReference(path2)

        // retrieve data
        databaseRef2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Don't ignore errors!
                Log.d("TAG", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                // remove this listener, since we only need to use DataSnapshot once
                databaseRef.removeEventListener(this)

                // the user has this family id in its family id entry
                assert(p0.child("familyId").hasChild(familyId))

                // the user is not the family owner
                assertEquals(false, p0.child("isFamilyOwner").value as Boolean)
            }
        })

        // wait until the toast message disappear
        ThreadController.stopForNMilliseconds(ThreadController.SHORT_TOAST_WAITING)
    }
}