package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.ItemU
import com.squareup.picasso.Picasso

class SliderAdapter(val items: ArrayList<ItemU>, val context: Context) : PagerAdapter() {
    var listener: SliderAdapter.OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onDownloadClick(position: Int,item:ArrayList<ItemU>)
        fun onShareClick(position: Int,item:ArrayList<ItemU>)
    }

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

        view.findViewById<ImageView>(R.id.slide_image).setOnClickListener{
            Log.d("dowloding",position.toString())
            listener!!.onDownloadClick(position, items)
        }

        view.findViewById<TextView>(R.id.slide_heading).setOnClickListener{
            Log.d("sharing",position.toString())
            listener!!.onShareClick(position, items, slideImageView)
        }
        container.addView(view)
        return view
    }

    inner class ImageViewHolder(val viewItem: View) : View.OnClickListener,
        View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        override fun onMenuItemClick(p0: MenuItem?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onClick(p0: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}