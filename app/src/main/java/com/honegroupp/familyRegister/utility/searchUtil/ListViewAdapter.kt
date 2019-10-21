package com.honegroupp.familyRegister.utility.searchUtil

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

/**
 * ListViewAdapter is an adapter to deal with data with listView
 * */
class ListViewAdapter(
    val items: ArrayList<Item>,
    val mActivity: AppCompatActivity
) : BaseAdapter() {

    private val nameList: ArrayList<String> = ArrayList()
    private val dateList: ArrayList<String> = ArrayList()
    private val imageURLList: ArrayList<String> = ArrayList()

    /**
     * the function is used to get the items' total number
     * */
    override fun getCount(): Int {
        return items.size
    }

    /**
     * the function is used to get the item object by its position in list
     * */
    override fun getItem(position: Int): Item {
        return items[position]
    }

    /**
     * the function is used to get item id by its position
     * */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * the function is used to present UI view for listView with necessary data
     * */
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        val layout: View = View.inflate(mActivity, R.layout.item_listview, null)
        val picture: ImageView = layout.findViewById(R.id.picture)
        val name: TextView = layout.findViewById(R.id.search_name)
        val date: TextView = layout.findViewById(R.id.search_date)

        for (item in items) {

            imageURLList.add(item.imageURLs[0])
            nameList.add(item.itemName)
            dateList.add(item.date)
        }

        //set image to imageView
        Picasso.get()
            .load(imageURLList[position])
            .placeholder(R.mipmap.loading_jewellery)
            .fit()
            .centerCrop()
            .into(picture)

        name.text = nameList[position]
        date.text = dateList[position]

        return layout
    }

}