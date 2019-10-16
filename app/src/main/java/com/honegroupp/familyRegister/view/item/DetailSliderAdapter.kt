package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.controller.ShowPageController
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso

class DetailSliderAdapter(
    private val items: ArrayList<Item>,
    private val userId: String,
    private val context: Context
) : PagerAdapter(),
    DetailImagesSliderAdapter.OnItemImageClickerListener {

    var listener: OnItemClickerListener? = null

    interface OnItemClickerListener {
        fun onItemClick(position: Int)
        fun onShareClick(imageView: ImageView)
        fun onDownloadClick(position: Int)
        fun onDeleteClick(position: Int)
        fun onEditClick(itemKey: String?)
        fun onBackClick()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    // notify data set change
    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_detail_layout, container, false)

        // set slides of images
        var imagesSlideViewPager = view.findViewById<ViewPager>(R.id.detail_images_slideViewPager)
        var imagesSliderAdapter = DetailImagesSliderAdapter(
            items[position].imageURLs,
            items[position].itemOwnerUID == userId,
            context
        )
        imagesSlideViewPager.adapter = imagesSliderAdapter
        imagesSliderAdapter.listener = this


        val slideName = view.findViewById<TextView>(R.id.item_name)
        val slideDescription = view.findViewById<TextView>(R.id.detail_desc)
        val slideDate = view.findViewById<TextView>(R.id.detail_date)
        val showButton = view.findViewById<MaterialFavoriteButton>(R.id.detail_favorite_button)
        showButton.isFavorite = false
        showButton.setFavoriteResource(R.drawable.ic_favorite_red_24dp)

        val currItemUploads = items[position]

//        val slideToolbar =
//            view.findViewById<com.google.android.material.appbar.CollapsingToolbarLayout>(R.id.detial_collapsing_toolbar)
//        slideToolbar.title = currItemUploads.itemName


        // show item name
        slideName.text = currItemUploads.itemName

        // show item description
        slideDescription.text = currItemUploads.itemDescription

        // show item date
        val dateParts = currItemUploads.date.split("/")
        val newDate = dateParts[2] + "." + dateParts[1] + "." + dateParts[0]
        slideDate.text = newDate

        // show item material
        if (currItemUploads.itemMaterial.isNotEmpty()) {
            view.findViewById<TextView>(R.id.material_description).visibility = View.VISIBLE
            val slideMaterial = view.findViewById<TextView>(R.id.detail_material)
            slideMaterial.visibility = View.VISIBLE
            slideMaterial.text = currItemUploads.itemMaterial
        }

        // set show button, solid heart if it is shown in show page
        if (userId in items[position].showPageUids) {
            showButton.isFavorite = true
        }

        view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar).setNavigationOnClickListener(){
            listener!!.onBackClick()
        }

        // show button logic
        showButton.setOnClickListener {
            ShowPageController.manageShow(currItemUploads, userId)
        }

        // click on edit button
        if (items[position].itemOwnerUID == userId) {
            view.findViewById<Button>(R.id.detail_edit).setOnClickListener {
                listener!!.onEditClick(items[position].key)
            }
        } else {
            view.findViewById<Button>(R.id.detail_edit).visibility = View.INVISIBLE
        }

        container.addView(view)
        return view
    }

    // on share click in menu
    override fun onShareClick(imageView: ImageView) {
        listener!!.onShareClick(imageView)
    }

    // on download click in menu
    override fun onDownloadClick(position: Int) {
        listener!!.onDownloadClick(position)
    }

    // on delete click in menu
    override fun onDeleteClick(position: Int) {
        listener!!.onDeleteClick(position)
    }

    // on image click
    override fun onImageClick(position: Int) {
        listener!!.onItemClick(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CoordinatorLayout)
    }

}