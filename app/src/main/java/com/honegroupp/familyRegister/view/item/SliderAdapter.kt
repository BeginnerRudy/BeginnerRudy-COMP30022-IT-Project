package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

public class SliderAdapter(val items: ArrayList<Item>, val context: Context, val slide_images: Array<Int>, val slide_headings: Array<String>, val slide_descs: Array<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        var layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_layout, container, false)

        var slideImageView = view.findViewById<ImageView>(R.id.slide_image)
        var slideHeaing = view.findViewById<TextView>(R.id.slide_heading)
        var slideDescription = view.findViewById<TextView>(R.id.slide_desc)

        val currUpload = items[position]

        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currUpload.url)
            .placeholder(R.mipmap.ic_launcher)
            .into(slideImageView)
        Log.d("url", currUpload.url)
        slideHeaing.setText(currUpload.name)
        slideDescription.setText(currUpload.description)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}