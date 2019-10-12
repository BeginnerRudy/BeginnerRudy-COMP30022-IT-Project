package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import com.github.chrisbanes.photoview.PhotoView
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso

class DImageSliderAdapter(
    private val items: ArrayList<String>
) : PagerAdapter() {
    var listener: OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val currItemUrls = items[position]

        val photoView = PhotoView(container.context)

        // Load image to PhotoView via its URL from Firebase Storage
        Picasso.get()
            .load(currItemUrls)
            .placeholder(R.mipmap.loading_jewellery)
            .into(photoView)

        container.addView(
            photoView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return photoView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}