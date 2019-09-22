package com.honegroupp.familyRegister

import com.honegroupp.familyRegister.model.User
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit category_item, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UserTest {
//    private val mUser = User()

    /**
     * Set up the environment for testing
     */
    @Before // Before testing
    fun setUp() {
    }

    /**
     * A new User with familyId "", the hasFamily should return false
     * */
    @Test
    fun hasFamilyNewUser() {
        val mUser = User()
        val hasFamilyResult = mUser.hasFamily()
        assertEquals(hasFamilyResult, false)
    }

    /**
     * A new User with familyId something other than "", the hasFamily should return true
     * */
    @Test
    fun hasFamilyIdIsNotEmpty() {
        val mUser = User(familyId = "a family id")
        val hasFamilyResult = mUser.hasFamily()
        assertEquals(hasFamilyResult, true)
    }
}
