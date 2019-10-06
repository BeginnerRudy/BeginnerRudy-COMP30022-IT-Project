package com.honegroupp.familyRegister

import com.honegroupp.familyRegister.model.User
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 *
 */
class FamilyActivityTest {
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
        assertEquals(false, hasFamilyResult)
    }
}
