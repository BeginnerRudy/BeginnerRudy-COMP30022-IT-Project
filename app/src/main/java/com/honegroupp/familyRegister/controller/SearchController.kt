package com.honegroupp.familyRegister.controller

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.utility.SearchMethod


/**
 * This class is responsible for controller the event related to search.
 *
 * */
class SearchController {
    companion object {

        val searchMethod: SearchMethod =
                SearchMethod()

        //TODO 1 user could create one item each time
        /**
         * This function is respnsible to create initial view for search
         * */
        fun init(
            mActivity: AppCompatActivity,
            listView: ListView,
            uid: String,
            category: Int
        ) {
            //actual logic function for init function
            searchMethod.init(mActivity, listView, uid, category)
        }

        /**
         * This methods is responsible for make a search.
         *
         * */
        fun makeSearch(
            mActivity: AppCompatActivity,
            queryText: String,
            uid: String,
            category: Int,
            listView: ListView
        ) {
            //actual logic function for doSearch function
            searchMethod
                .doSearch(mActivity, listView, uid, category, queryText);

            //Toast.makeText(mActivity, "Search Finished", Toast.LENGTH_SHORT).show()
        }

    }
}