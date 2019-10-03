package com.honegroupp.familyRegister.view.home

import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ShowPageController
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

class ShowTabAdapter(val items: ArrayList<Item>, val mContext: HomeActivity) :
    RecyclerView.Adapter<ShowTabAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(v)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currItem = items[position]

        holder.textViewName.text = currItem.itemName
        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currItem.imageURLs[0])
            .placeholder(R.mipmap.loading_jewellery)
            .fit()
            .centerCrop()
            .into(holder.imageView)


        // Add logic for show page
        val showButton = holder.showButton

        showButton.setOnClickListener {
            ShowPageController.manageShow(showButton, currItem, mContext.userID)
        }
        showButton.setImageResource(android.R.drawable.star_big_on)

    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ImageViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val textViewName: TextView = viewItem.findViewById(R.id.txt_name)

        val imageView: ImageView = viewItem.findViewById(R.id.img_upload)

        val showButton: ImageButton = viewItem.findViewById(R.id.button_show)

    }
}
