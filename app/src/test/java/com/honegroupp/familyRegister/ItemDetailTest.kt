package com.honegroupp.familyRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.honegroupp.familyRegister.model.User
import com.honegroupp.familyRegister.view.item.DetailSlide
import com.honegroupp.familyRegister.view.itemList.ItemListActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
 * Example local unit category_item, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ItemDetailTest {
    val activity: DetailSlide = DetailSlide()
//    private val mUser = User()

    /**
     * Set up the environment for testing
     */
    @Before // Before testing
    fun setUp() {
        val uid = "`123"
        val position = 0
        val categoryName = 0
        val currFamilyId = "aaa"
        val intent = Intent(activity, DetailSlide::class.java)
        intent.putExtra("UserID", uid)
        intent.putExtra("PositionList", position)
        intent.putExtra("CategoryNameList", categoryName)
        intent.putExtra("FamilyId", currFamilyId)
        intent.putExtra("SortOrder", DetailSlide.SORT_DEFAULT)
        activity.startActivity(intent)
//        activity.startDownloading()
    }


    /**
     * A new User with familyId "", the hasFamily should return false
     * */
    @Test
    fun hasFamilyNewUser() {
        val mUser = User()
        val hasFamilyResult = mUser.hasFamily()
        Assert.assertEquals(hasFamilyResult, false)
    }

    /**
     * A new User with familyId something other than "", the hasFamily should return true
     * */
    @Test
    fun hasFamilyIdIsNotEmpty() {
        val mUser = User(familyId = "a family id")
        val hasFamilyResult = mUser.hasFamily()
        Assert.assertEquals(hasFamilyResult, true)
    }
}

