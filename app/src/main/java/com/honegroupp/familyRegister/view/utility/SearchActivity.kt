package com.honegroupp.familyRegister.view.utility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.SearchController
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    lateinit var currUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //get User ID
        currUid= intent.getStringExtra("UserID")
        val listView: ListView = findViewById(R.id.list_view)

        initView(listView, currUid);
        setListener(listView);
    }

    /*
     * this function used to initialize search view
     *
     */
    private fun initView(listView: ListView, uid: String){
        //the style of search view
        view_search.onActionViewExpanded()
        SearchController.init(this, listView, uid)
    }

    private fun setListener(listView: ListView){

        //listen to search text
        view_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            //when search clicked
            override fun onQueryTextSubmit(query: String): Boolean {
                SearchController.makeSearch(this@SearchActivity,query,currUid, listView)
                return false
            }

            //when search context changed
            override fun onQueryTextChange(newText: String): Boolean {
                SearchController.makeSearch(this@SearchActivity,newText,currUid, listView)
                return false
            }

        });
    }
}
