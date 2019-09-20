package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.ItemU
import com.squareup.picasso.Picasso

class SliderAdapter(val items: ArrayList<ItemU>, val context: Context) : PagerAdapter() {

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

        view.findViewById<TextView>(R.id.slide_desc).setOnClickListener{
            val intent = Intent(context, ItemEdit::class.java)
            context.startActivity(intent)
        }

        container.addView(view)
        return view
    }

    inner class ImageViewHolder(val viewItem: View) : View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}