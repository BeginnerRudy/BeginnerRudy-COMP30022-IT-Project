package com.honegroupp.familyRegister.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.SearchController
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    lateinit var currUid: String
    var category: Int = -99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //get User ID
        currUid= intent.getStringExtra("UserID")
        // get position of item clicked in item list for setting Current page item
        category = intent.getStringExtra("Category").toInt()
        //get listview
        val listView: ListView = findViewById(R.id.list_view)

        initView(listView, currUid)
        setListener(listView)
    }

    /*
     * this function used to initialize search view
     *
     */
    private fun initView(listView: ListView, uid: String){
        //the style of search view
        view_search.onActionViewExpanded()
        SearchController.init(this, listView, uid, category)
    }

    private fun setListener(listView: ListView){

        //listen to search text
        view_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            //when search clicked
            override fun onQueryTextSubmit(query: String): Boolean {
                SearchController.makeSearch(this@SearchActivity,query,currUid, category, listView)
                return false
            }

            //when search context changed
            override fun onQueryTextChange(newText: String): Boolean {
                SearchController.makeSearch(this@SearchActivity,newText,currUid, category, listView)
                return false
            }

        });
    }
}
