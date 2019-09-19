package com.honegroupp.familyRegister.view.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.honegroupp.familyRegister.R

class ItemSlide : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_slide)

        var mSlideViewPager = findViewById<ViewPager>(R.id.slideViewPager)
        var mDotLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        var sliderAdapter = SliderAdapter(this)
        mSlideViewPager.adapter = sliderAdapter
    }
}