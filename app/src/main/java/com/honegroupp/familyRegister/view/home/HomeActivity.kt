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
import com.honegroupp.familyRegister.view.item.ItemDetail


class HomeActivity : AppCompatActivity(), IDoubleClickToExit {

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        search.setOnClickListener {
            //            (activity as NavigationHost).navigateTo(RegisterActivity(), false)
            Toast.makeText(this, "/aaa", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ItemDetail::class.java)
            startActivity(intent)
        }

        // Press Hamburger key to navigate to navigation drawer
        toolbar.setNavigationOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        // Interaction with menuitems contained in the navigation drawer
        nav_view.bringToFront()

        // Log out
        AuthenticationController.logout(btn_log_out, this)

        //


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OneFragment(), "ONE")
        adapter.addFragment(OneFragment(), "TWO")
        adapter.addFragment(OneFragment(), "THREE")
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

}