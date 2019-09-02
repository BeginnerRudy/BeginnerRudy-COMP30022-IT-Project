package com.example.familyRegister.core

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyRegister.R
import com.example.familyRegister.model.ItemUpload
import com.squareup.picasso.Picasso

class ItemDetailAdapter(val item: ArrayList<ItemUpload>, val mContext: Context, val item_position: Int) :
    RecyclerView.Adapter<ItemDetailAdapter.ImageViewHolder>() {

    var listener: ItemDetailAdapter.OnItemClickerListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDetailAdapter.ImageViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.image_detail, parent, false)
        return ImageViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currUpload = item[item_position]
        holder.textViewName.text = currUpload.name
        holder.textDescription.text = currUpload.description
        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currUpload.url)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.imageView)
        Log.d("url", currUpload.url)
    }

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onDownloadClick(position: Int,item:ArrayList<ItemUpload>)
        fun onDeleteClick(position: Int)
    }

    inner class ImageViewHolder(val viewItem: View) : RecyclerView.ViewHolder(viewItem), View.OnClickListener,
        View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        val textViewName: TextView = viewItem.findViewById(R.id.txt_name)
        val textDescription: TextView = viewItem.findViewById(R.id.txt_name2)
        val imageView: ImageView = viewItem.findViewById(R.id.img_upload)

        init {
            viewItem.setOnClickListener(this)
            viewItem.setOnCreateContextMenuListener(this)
        }

        override fun onMenuItemClick(p0: MenuItem?): Boolean {
            if (listener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    when (p0?.itemId) {
                        1 -> {
                            listener!!.onDownloadClick(position,item)
                            return true
                        }
                        2 -> {
                            listener!!.onDeleteClick(position)
                            return true
                        }
                    }
                }
            }
            return false
        }

        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            p0?.setHeaderTitle("Select Action")
            val doWatEver = p0?.add(Menu.NONE, 1, 1, "Do Download: ")
            val delete = p0?.add(Menu.NONE, 2, 2, "Do Delete: ")

            doWatEver?.setOnMenuItemClickListener(this)

            delete?.setOnMenuItemClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (listener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(position)
                }
            }
        }
    }
}

