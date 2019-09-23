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

        //TODO 1 user could create one item each time

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

            val searchMethod: SearchMethod = SearchMethod()
            searchMethod.doSearch(mActivity, uid, queryText, listView);

            Toast.makeText(mActivity, "Family Created Successfully", Toast.LENGTH_SHORT).show()
            // Go back to the previous activity
            mActivity.finish()
        }

    }
}