package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

class DetailSliderAdapter(val items: ArrayList<Item>, val context: Context) : PagerAdapter(),
    DetailImagesSliderAdapter.OnItemClickerListener {

    lateinit var imagesSlideViewPager : ViewPager
    var listener: OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onShareClick(imageView: ImageView)
        fun onDownloadClick(position: Int)
        fun onDeleteClick(position: Int)
        fun onEditClick(itemKey: String?)
    }

    // on share click in menu
    override fun onShareClick(imageView: ImageView) {
        listener!!.onShareClick(imageView)
    }

    // on download click in menu
    override fun onDownloadClick(position: Int) {
        listener!!.onDownloadClick(position)
    }

    // on delete click in menu
    override fun onDeleteClick(position: Int) {
        listener!!.onDeleteClick(position)
    }

    // on image click
    override fun onImageClick(position: Int) {
        listener!!.onItemClick(position)
    }


    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    // notify data set change
    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_detail_layout, container, false)

        // set slides of images
        imagesSlideViewPager = view.findViewById(R.id.detail_images_slideViewPager)
        var imagesSliderAdapter = DetailImagesSliderAdapter(items[position].imageURLs, context)
        imagesSlideViewPager.adapter = imagesSliderAdapter
        imagesSliderAdapter.listener = this


        // val slideHeading = view.findViewById<TextView>(R.id.detail_heading)
        val slideDescription = view.findViewById<TextView>(R.id.detail_desc)

        val currItemUploads = items[position]

        // slideHeading.setText(currItemUploads.itemName)
        val slideToolbar = view.findViewById<com.google.android.material.appbar.CollapsingToolbarLayout>(R.id.detial_collapsing_toolbar)
        slideToolbar.setTitle(currItemUploads.itemName)
        slideDescription.setText(currItemUploads.itemDescription)

        // click on edit button
        view.findViewById<Button>(R.id.detail_edit).setOnClickListener{
            listener!!.onEditClick(items[position].key)
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CoordinatorLayout)
    }

}