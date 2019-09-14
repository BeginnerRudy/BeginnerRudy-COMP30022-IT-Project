package com.honegroupp.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.honegroupp.familyRegister.R
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.FragmentPagerAdapter
import com.honegroupp.familyRegister.view.home.OneFragment
import androidx.viewpager.widget.ViewPager
import com.firebase.ui.auth.AuthUI

import com.google.android.material.tabs.TabLayout
import com.honegroupp.familyRegister.IDoubleClickToExit
import com.honegroupp.familyRegister.view.authentication.LoginActivity


class HomeActivity : AppCompatActivity(), IDoubleClickToExit {

    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        toolbar?.setTitle("HOME");
        toolbar?.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager!!)

        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)

        search.setOnClickListener {
//            (activity as NavigationHost).navigateTo(RegisterActivity(), false)
            Toast.makeText(this, "/aaa",Toast.LENGTH_LONG).show()
        }

        toolbar?.setNavigationOnClickListener{
            Toast.makeText(this, "Hamburger",Toast.LENGTH_LONG).show()
        }


        nav_view.bringToFront()
        val btn_log_out = nav_view.menu.findItem(R.id.btn_log_out)
        btn_log_out.setOnMenuItemClickListener {
            Toast.makeText(this,"CLICK!!!!!!!!!!!", Toast.LENGTH_SHORT).show()
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            true
        }
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
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList.get(position)
        }
    }

    /**
     * Click back button twice to exit app.
     * */
    override fun onBackPressed() {
        doubleClickToExit(this)
    }

}