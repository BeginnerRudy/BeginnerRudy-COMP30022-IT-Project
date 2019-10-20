package com.honegroupp.familyRegister


import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.utility.searchUtil.SearchMethod
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit category_item, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SearchTest {

    /**
    * Set up the environment for testing
    */
    @Before // Before testing
    fun setUp() {
    }

    fun generateTestList(): ArrayList<Item>{
        val item1 : Item = Item()
        item1.itemName = "happy"

        val item2 : Item = Item()
        item2.itemName = "apple"

        val item3 : Item = Item()
        item3.itemName = "piano"

        val itemList: ArrayList<Item> = ArrayList()
        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        return itemList
    }

    /**
     * provide a itemList and a query already in itemList, should return a sub-list
     * */
    @Test
    fun searchExistItemByFullName(){
        val query: String = "happy"
        val itemList: ArrayList<Item> = generateTestList()
        val searchMethod: SearchMethod =
                SearchMethod()
        val newList: ArrayList<Item> = searchMethod.search(query, itemList)
        var result: Boolean = false
        for (item in newList){
            if (itemList.contains(item)){
                result = true
                break
            }
        }
        assertEquals(result, true)
    }

    /**
     * provide a itemList and a query not in itemList, should return a empty list
     * */
    @Test
    fun searchNotExistItem(){
        val query = "banana"
        val itemList: ArrayList<Item> = generateTestList()
        val searchMethod: SearchMethod =
                SearchMethod()
        val newList: ArrayList<Item> = searchMethod.search(query, itemList)
        var result: Boolean = false
        for (item in newList){
            if (itemList.contains(item)){
                result = true
                break
            }
        }
        assertEquals(result, false)
    }

    /**
     * provide a itemList and a query already in itemList, should return a sub-list
     * */
    @Test
    fun searchExistItemBySubName(){
        val query: String = "hap"
        val itemList: ArrayList<Item> = generateTestList()
        val searchMethod: SearchMethod =
                SearchMethod()
        val newList: ArrayList<Item> = searchMethod.search(query, itemList)
        var result: Boolean = false
        for (item in newList){
            if (itemList.contains(item)){
                result = true
                break
            }
        }
        assertEquals(result, true)
    }
}