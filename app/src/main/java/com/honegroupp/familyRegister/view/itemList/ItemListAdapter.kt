package com.honegroupp.familyRegister.view.itemList

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.honegroupp.familyRegister.view.home.ContainerAdapter

class ItemListAdapter(items: ArrayList<Item>, val mContext: ItemListActivity)  {

//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)
//
//        val currItem = items[position]
//
//        // Add logic for show page
//        val showButton = holder.showButton
//
//        // if this user liked this item, make the like image on
//        if (currItem.ShowPageUids.containsKey(mContext.uid)) {
//            showButton.setImageResource(android.R.drawable.star_big_on)
//        }
//
//    }
//
//    inner class ImageViewHolder(viewItem: View) :
//        ContainerAdapter.ImageViewHolder(viewItem),
//        View.OnCreateContextMenuListener,
//        MenuItem.OnMenuItemClickListener {
//
//        init {
//            viewItem.setOnClickListener(this)
//            viewItem.setOnCreateContextMenuListener(this)
//        }
//
//
//        override fun onMenuItemClick(p0: MenuItem?): Boolean {
//            if (listener != null) {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    when (p0?.itemId) {
//                        1 -> {
//                            listener!!.onDeleteClick(items[position].key.toString())
//                            return true
//                        }
//                    }
//                }
//            }
//            return false
//        }
//
//        override fun onCreateContextMenu(
//            p0: ContextMenu?,
//            p1: View?,
//            p2: ContextMenu.ContextMenuInfo?
//        ) {
//            p0?.setHeaderTitle("Select Action")
//            val delete = p0?.add(Menu.NONE, 1, 1, R.string.delete_from_this_category)
//
//
//            delete?.setOnMenuItemClickListener(this)
//        }
//    }
}
