package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso

class DImageSliderAdapter(val items: ArrayList<String>, val context: Context) : PagerAdapter() {
    var listener: DImageSliderAdapter.OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onDownloadClick(position: Int,item:ArrayList<String>)
        fun onShareClick(position: Int,item:ArrayList<String>, imageView: ImageView)
    }

    override fun getCount(): Int {
        Log.d("dimageitemssize", items.size.toString())
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): View {
        var layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_dimage_layout, container, false)

        var slideImageView = view.findViewById<ImageView>(R.id.dimage_image)

        val currUpload = items[position]

        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currUpload)
            .placeholder(R.mipmap.ic_launcher)
            .into(slideImageView)
        Log.d("url", currUpload)

        view.findViewById<ImageButton>(R.id.dimage_download).setOnClickListener{
            Log.d("dowloding",position.toString())
            listener!!.onDownloadClick(position, items)
        }

        view.findViewById<ImageButton>(R.id.dimage_share).setOnClickListener{
            Log.d("sharing",position.toString())
            listener!!.onShareClick(position, items, slideImageView)
        }

        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}