package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso

class DetailImagesSliderAdapter(val items: ArrayList<String>, val context: Context) : PagerAdapter() {
    var listener: OnItemClickerListener? = null
    lateinit var slideImageView: ImageView

    interface OnItemClickerListener {
        fun onImageClick(position: Int, items: ArrayList<String>)
        fun setMenu(slideImageView: ImageView)
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
        val view: View = layoutInflater.inflate(R.layout.slide_detail_images_layout, container, false)

        slideImageView = view.findViewById<ImageView>(R.id.detail_images)

        val currItemUrls = items[position]

        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currItemUrls)
            .placeholder(R.drawable.loading_jewellery)
            .into(slideImageView)

        listener!!.setMenu(slideImageView)

        view.findViewById<ImageView>(R.id.detail_images).setOnClickListener{
            Log.d("ddddtailclickonviewp",items.toString())
            listener!!.onImageClick(position, items)
        }

//        view.setOnLongClickListener{
//            Log.d("longgggclicklis", "longclick")
//            return@setOnLongClickListener true
//        }

        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

}