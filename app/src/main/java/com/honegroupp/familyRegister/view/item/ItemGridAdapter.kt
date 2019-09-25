package com.honegroupp.familyRegister.view.itemList

import android.content.Context
import android.view.*
import android.widget.*
import com.honegroupp.familyRegister.R
import android.view.LayoutInflater
import android.net.Uri
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import com.squareup.picasso.Picasso


class ItemGridAdapter:BaseAdapter{
    private var context : Context? = null
    private var allUris: ArrayList<Uri>? = null


    constructor( context: ItemUploadActivity, allUris: ArrayList<Uri>){
        this.context = context
        this.allUris = allUris
    }

    override fun getView(position:Int, convertView: View?, parent: ViewGroup?):View{
        // Inflate the custom view
        val inflater = parent?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.upload_image,null)
//        view.setBackgroundResource(allElementDetails[position])
//        Picasso.get().load(allElementDetails?.get(position)).into(convertView.)

        val imageView = view.findViewById<ImageView>(R.id.upload_image)

        Picasso.get().load(allUris?.get(position)).into(imageView)


        // Set a click listener for card view
//        card.setOnClickListener{
//            // Show selected color in a toast message
//            Toast.makeText(parent.context,
//                "Clicked : ${list[position].first}",Toast.LENGTH_SHORT).show()
//
//            // Get the activity reference from parent
//            val activity  = parent.context as Activity
//
//            // Get the activity root view
//            val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
//                .getChildAt(0)
//
//            // Change the root layout background color
//            viewGroup.setBackgroundColor(list[position].second)
//        }

        // Finally, return the view
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        if (allUris == null){
            return 0
        }
        return allUris!!.size
    }


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false)
//        return ImageViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val currItem = items[position]
//
//        holder.textViewName.text = currItem.itemName
//        // Load image to ImageView via its URL from Firebase Storage
//        Picasso.get()
//            .load(currItem.imageURLs[0])
//            .placeholder(R.mipmap.ic_launcher)
//            .fit()
//            .centerCrop()
//            .into(holder.imageView)
//        Log.d("url000", currItem.imageURLs[0])
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//
//    interface OnItemClickerListener {
//        fun onItemClick(position: Int)
////        fun onWhatEverClick(position: Int)
////        fun onDeleteClick(position: Int)
////        fun onDownloadClick(position: Int,item:ArrayList<ItemUpload>)
//    }
//
//    inner class ImageViewHolder(val viewItem: View) : RecyclerView.ViewHolder(viewItem), View.OnClickListener
////        , View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener
//    {
//        val textViewName: TextView = viewItem.findViewById(R.id.txt_name)
//        val imageView: ImageView = viewItem.findViewById(R.id.img_upload)
//
//        init {
//            viewItem.setOnClickListener(this)
//        }
//
////        override fun onMenuItemClick(p0: MenuItem?): Boolean {
////            if (listener != null) {
////                val position = adapterPosition
////                if (position != RecyclerView.NO_POSITION) {
////                    when (p0?.itemId) {
////                        1 -> {
////                            listener!!.onWhatEverClick(position)
////                            return true
////                        }
////                        2 -> {
////                            listener!!.onDeleteClick(position)
////                            return true
////                        }
////                        3 -> {
////                            listener!!.onDownloadClick(position,items)
////                            return true
////                        }
////                    }
////                }
////            }
////            return false
////        }
//
//        //        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
////            p0?.setHeaderTitle("Select Action")
////            val doWatEver = p0?.add(Menu.NONE, 1, 1, "Do Whatever: ")
////            val delete = p0?.add(Menu.NONE, 2, 2, "Do Delete: ")
////            val save = p0?.add(Menu.NONE, 3, 3, "Do Save: ")
////
////            doWatEver?.setOnMenuItemClickListener(this)
////
////            delete?.setOnMenuItemClickListener(this)
////
////            save?.setOnMenuItemClickListener(this)
////        }
////
//        override fun onClick(p0: View?) {
//            if (listener != null) {
//                Log.d("AAAdapterOnclick",adapterPosition.toString())
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    listener!!.onItemClick(position)
//                }
//            }
//        }
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<ItemGridAdapter> {
//        override fun createFromParcel(parcel: Parcel): ItemGridAdapter {
//            return ItemGridAdapter(parcel)
//        }
//
//        override fun newArray(size: Int): Array<ItemGridAdapter?> {
//            return arrayOfNulls(size)
//        }
//    }

}
