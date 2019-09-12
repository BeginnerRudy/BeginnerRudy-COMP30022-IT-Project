package com.honegroupp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.honegroupp.familyRegister.R
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.FragmentPagerAdapter
import com.honegroupp.familyRegister.OneFragment
import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout



class HomeActivity : AppCompatActivity() {

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
//            (activity as NavigationHost).navigateTo(RegisterFragment(), false)
            Toast.makeText(this, "/aaa",Toast.LENGTH_LONG).show()
        }

        toolbar?.setNavigationOnClickListener{
            Toast.makeText(this, "Hamburger",Toast.LENGTH_LONG).show()
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
}