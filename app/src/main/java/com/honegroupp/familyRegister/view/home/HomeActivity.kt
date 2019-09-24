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
import com.honegroupp.familyRegister.view.authentication.AccountActivity

import android.widget.TextView

import com.honegroupp.familyRegister.view.utility.SearchActivity


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), IDoubleClickToExit {

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var userID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        //get User ID
        userID = intent.getStringExtra("UserID")

        // Configure the toolbar setting
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.home_page)
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)

        tabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)


        // Searching Feature


        search_icon.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            intent.putExtra("UserID", userID)
            startActivity(intent)
            true
        }


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
            val intent = Intent(this, AccountActivity::class.java)
            intent.putExtra("UserID", userID)
            startActivity(intent)
            true
        }

        nav_view.menu.findItem(R.id.nav_view_family).setOnMenuItemClickListener {
            toast("Clicked view family")
            true
        }


        // Log out
        AuthenticationController.logout(btn_log_out, this)

        // Click any item has family concepts, then navigate to FamilyActivity

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AllTabFragment(), getString(R.string.all))
        adapter.addFragment(CategoriesTabFragment(), getString(R.string.category))
        adapter.addFragment(ShowTabFragment(), getString(R.string.show))
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