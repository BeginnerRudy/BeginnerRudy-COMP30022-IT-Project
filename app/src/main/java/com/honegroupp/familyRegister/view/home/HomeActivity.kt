package com.honegroupp.familyRegister.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.honegroupp.familyRegister.R
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout
import com.honegroupp.familyRegister.IDoubleClickToExit
import com.honegroupp.familyRegister.controller.AuthenticationController

import android.widget.TextView
import com.honegroupp.familyRegister.view.family.FamilyCreateActivity
import com.honegroupp.familyRegister.view.family.FamilyJoinActivity
import android.view.Gravity

import androidx.appcompat.widget.SearchView
import android.graphics.PorterDuff
import android.R.id
import android.graphics.Color
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.ImageView


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), IDoubleClickToExit {

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //get User ID
        var userID: String = intent.getStringExtra("UserID")

        // Configure the toolbar setting
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "HOME"
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)

        tabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        // Searching Feature
        val search = findViewById<SearchView>(R.id.searchView)
        //set search icon to right
        search.layoutParams = Toolbar.LayoutParams(Gravity.RIGHT)
        //set search close color to white
        val searchClose = search.findViewById<ImageView>(R.id.search_close_btn)
        searchClose.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)


        // Press Hamburger key to navigate to navigation drawer
        toolbar.setNavigationOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        //display User Name
        val headerView = nav_view.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.nav_userName) as TextView
        navUsername.text = userID


        // Interaction with menuitems contained in the navigation drawer
        nav_view.menu.findItem(R.id.nav_account).setOnMenuItemClickListener {
            toast("Clicked1")
            true
        }
        nav_view.menu.findItem(R.id.nav_create_family).setOnMenuItemClickListener {

            // Navigation to CreateFamily Activity
            val intent = Intent(this, FamilyCreateActivity::class.java)
            intent.putExtra("UserID", userID)
            startActivity(intent)
            true
        }

        nav_view.menu.findItem(R.id.nav_join_family).setOnMenuItemClickListener {

            // Navigation to JoinFamily Activity
            val intent = Intent(this, FamilyJoinActivity::class.java)
            intent.putExtra("UserID", userID)
            startActivity(intent)

            true
        }

        nav_view.menu.findItem(R.id.nav_view_family).setOnMenuItemClickListener {
            toast("Clicked4")
            true
        }


        // Log out
        AuthenticationController.logout(btn_log_out, this)

        // Click any item has family concepts, then navigate to FamilyActivity

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FirstTabFragment(), "ONE")
        adapter.addFragment(SecondTabFragment(), "TWO")
        adapter.addFragment(ThirdTabFragment(), "THREE")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    /**
     * Click back button twice to exit app.
     * */
    override fun onBackPressed() {
        doubleClickToExit(this)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}