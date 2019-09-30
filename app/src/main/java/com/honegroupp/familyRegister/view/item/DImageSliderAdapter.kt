package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso

class DImageSliderAdapter(val items: ArrayList<String>, val context: Context) : PagerAdapter() {
    var listener: OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onDownloadClick(position: Int,item:ArrayList<String>)
        fun onShareClick(position: Int,item:ArrayList<String>, imageView: ImageView)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        Log.d("dimgggggItemPosi",items.toString())
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        Log.d("ooonSlider",items[position])
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_dimage_layout, container, false)

        val slideImageView = view.findViewById<ImageView>(R.id.dimage_image)

        val currItemUrls = items[position]

        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currItemUrls)
            .placeholder(R.drawable.loading_jewellery)
            .into(slideImageView)

        // set on click listeners
        view.findViewById<ImageButton>(R.id.dimage_download).setOnClickListener{
            listener!!.onDownloadClick(position, items)
        }

        view.findViewById<ImageButton>(R.id.dimage_share).setOnClickListener{
            listener!!.onShareClick(position, items, slideImageView)
        }

        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}