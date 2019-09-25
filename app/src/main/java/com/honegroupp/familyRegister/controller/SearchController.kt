package com.honegroupp.familyRegister.controller

import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.utility.SearchMethod


/**
 * This class is responsible for controller the event related to search.
 *
 * */
class   SearchController {
    companion object {

        val searchMethod: SearchMethod = SearchMethod()

        //TODO 1 user could create one item each time
        /**
         * This function is respnsible to create initial view for search
         * */
        fun init(
            mActivity: AppCompatActivity,
            listView: ListView,
            uid: String
        ){
            //actual logic function for init function
            searchMethod.init(mActivity,listView, uid)
        }

        /**
         * This methods is responsible for make a search.
         *
         * */
        fun makeSearch(
            mActivity: AppCompatActivity,
            queryText: String,
            uid: String,
            listView: ListView
        ) {
            //actual logic function for doSearch function
            searchMethod.doSearch(mActivity, listView, uid, queryText);

            //Toast.makeText(mActivity, "Search Finished", Toast.LENGTH_SHORT).show()
        }

    }
}