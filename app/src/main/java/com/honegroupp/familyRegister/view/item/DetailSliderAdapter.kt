package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.viewpager.widget.PagerAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

class DetailSliderAdapter(val items: ArrayList<Item>, val context: Context) : PagerAdapter() {
    var listener: DetailSliderAdapter.OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int, items:ArrayList<Item>)
        fun onDownloadClick(position: Int, items:ArrayList<Item>)
        fun onShareClick(position: Int, items:ArrayList<Item>, imageView: ImageView)
    }

    override fun getCount(): Int {
        Log.d("detailitemssize", items.size.toString())
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): View {
        var layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_detail_layout, container, false)

        var slideImageView = view.findViewById<ImageView>(R.id.detail_image)
        var slideHeaing = view.findViewById<TextView>(R.id.detail_heading)
        var slideDescription = view.findViewById<TextView>(R.id.detail_desc)

        val currUpload = items[position]


        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currUpload.imageURLs[0])
            .placeholder(R.mipmap.ic_launcher)
            .into(slideImageView)
        Log.d("url", currUpload.imageURLs[0])
        slideHeaing.setText(currUpload.itemName)
        slideDescription.setText(currUpload.itemDescription)

        view.findViewById<Button>(R.id.detail_edit).setOnClickListener{
            val intent = Intent(context, ItemEdit::class.java)
            context.startActivity(intent)
        }

        view.findViewById<ImageView>(R.id.detail_image).setOnClickListener{
            Log.d("detailonitemclick",position.toString())
            listener!!.onItemClick(position, items)
        }

        view.findViewById<Button>(R.id.detail_download).setOnClickListener{
            Log.d("detaildowloding",position.toString())
            listener!!.onDownloadClick(position, items)
        }

        view.findViewById<Button>(R.id.detail_share).setOnClickListener{
            Log.d("detailsharing",position.toString())
            Picasso.get()
                .load(currUpload.imageURLs[0])
                .placeholder(R.mipmap.ic_launcher)
                .into(slideImageView)
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