package com.honegroupp.familyRegister.view.home

import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ShowPageController
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

/**
 * This class is responsible for activities which contains a list of items's adapter logic.
 *
 * */
open class ContainerAdapter(
    val items: ArrayList<Item>,
    open val mContext: ContainerActivity,
    private val situation: String
) :
    RecyclerView.Adapter<ContainerAdapter.ImageViewHolder>() {
    companion object {
        const val CATEGORY = "C"
        const val SHOWPAGE = "S"
        const val ALL = "A"
    }


    var listener: OnItemClickerListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
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
            ShowPageController.manageShow(currItem, mContext.uid)
        }

        // if this user liked this item, make the like image on
        if (showButton.isFavorite){
            if (!currItem.showPageUids.containsKey(mContext.uid)) {
                showButton.isFavorite = false
            }
        }else{
            if (currItem.showPageUids.containsKey(mContext.uid)) {
                showButton.isFavorite = true
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onDeleteClick(itemId: String)
    }

    inner class ImageViewHolder(viewItem: View) :
        RecyclerView.ViewHolder(viewItem),
        View.OnClickListener,
        View.OnCreateContextMenuListener,
        MenuItem.OnMenuItemClickListener {
        val textViewName: TextView = viewItem.findViewById(R.id.txt_name)

        val imageView: ImageView = viewItem.findViewById(R.id.img_upload)

        val showButton: MaterialFavoriteButton =
            viewItem.findViewById(R.id.item_list_favorite_button)

        init {
            viewItem.setOnClickListener(this)

            if (situation == CATEGORY) {
                viewItem.setOnCreateContextMenuListener(this)
            }
        }

        override fun onClick(p0: View?) {
            if (listener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(position)
                }
            } else {
                Toast.makeText(mContext, "listener is null", Toast.LENGTH_LONG).show()
            }
        }

        override fun onMenuItemClick(p0: MenuItem?): Boolean {
            if (listener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    when (p0?.itemId) {
                        1 -> {
                            listener!!.onDeleteClick(items[position].key.toString())
                            return true
                        }
                    }
                }
            }
            return false
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            p0?.setHeaderTitle("Select Action")
            val delete = p0?.add(Menu.NONE, 1, 1, R.string.delete_from_this_category)


            delete?.setOnMenuItemClickListener(this)
        }
    }

}