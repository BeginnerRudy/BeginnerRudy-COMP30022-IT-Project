package com.example.FamilyRegister

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

/**
 * @author: Renjie Meng
 * This class is the Adapter for the recycler view with id -> category_recycler_view in the activity_category
 * */

class CategoryAdapter(val items: ArrayList<CategoryUpload>, val mContext: Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var listener: OnItemClickerListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currUpload = items[position]
        holder.textViewName.text = currUpload.name
        holder.textViewCount.text = currUpload.count
        // Load image to ImageView via its URL from Firebase Storage
        if (currUpload.url.equals(CategoryFragment.PLEASE_USE_DEFAULT_COVER)){
            holder.imageView.setImageResource(R.drawable.default_cover)
        }else {
            Picasso.get()
                .load(currUpload.url)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView)
        }

        holder.imageView.setOnClickListener {

            val goToItemListActivity= Intent(mContext, ItemListActivity::class.java)
            // pass category path to goToItemListActivity
            goToItemListActivity.putExtra("categoryPath", holder.textViewName.text.toString())
            mContext.startActivity(goToItemListActivity)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onWhatEverClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    inner class CategoryViewHolder(val viewItem: View) : RecyclerView.ViewHolder(viewItem), View.OnClickListener,
        View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        val textViewName: TextView = viewItem.findViewById(R.id.txt_category_name)
        val textViewCount: TextView = viewItem.findViewById(R.id.txt_category_count)
        val imageView: ImageView = viewItem.findViewById(R.id.img_category_cover)

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
                            listener!!.onWhatEverClick(position)
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
            val doWatEver = p0?.add(Menu.NONE, 1, 1, "Do Whatever: ")
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