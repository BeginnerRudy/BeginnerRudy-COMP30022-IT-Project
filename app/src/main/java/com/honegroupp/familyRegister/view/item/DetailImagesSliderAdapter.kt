package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso

class DetailImagesSliderAdapter(val items: ArrayList<String>, val context: Context) : PagerAdapter(), View.OnClickListener {

    var listener: OnItemClickerListener? = null
    lateinit var currSlideImageView: ImageView
    var currposition = 0

    interface OnItemClickerListener {
        fun onImageClick(position: Int)
        fun onDownloadClick(position: Int)
        fun onShareClick(imageView: ImageView)
        fun onDeleteClick(position: Int)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        Log.d("dimgggggItemPosi",items.toString())
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        Log.d("ooonSlider",items[position])
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_detail_images_layout, container, false)

        var slideImageView = view.findViewById<ImageView>(R.id.detail_images)

        val currItemUrls = items[position]

        // Load image to ImageView via its URL from Firebase Storage
        Picasso.get()
            .load(currItemUrls)
            .placeholder(R.mipmap.loading_jewellery)
            .into(slideImageView)

        view.findViewById<ImageView>(R.id.detail_images).setOnClickListener{
            Log.d("ddddtailclickonviewp",items.toString())
            listener!!.onImageClick(position)
        }

        createBottomSheetDialog()

        view.setOnLongClickListener{
            showDialog()
            Log.d("longgggclicklis", "longclick")
            this.currSlideImageView = slideImageView
            this.currposition = position
            return@setOnLongClickListener true
        }

        container.addView(view)
        return view
    }

    lateinit var bottomSheetDialog: BottomSheetDialog

    private fun createBottomSheetDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
        val shareLinearLayout = view.findViewById<LinearLayout>(R.id.shareLinearLayout)
        val downloadLinearLayout = view.findViewById<LinearLayout>(R.id.downloadLinearLayout)
        val deleteLinearLayout = view.findViewById<LinearLayout>(R.id.deleteLinearLayout)


        shareLinearLayout.setOnClickListener(this)
        downloadLinearLayout.setOnClickListener(this)

        if (items.size > 1){
            deleteLinearLayout.setOnClickListener(this)
            deleteLinearLayout.setVisibility(View.VISIBLE)
        } else {
            deleteLinearLayout.setVisibility(View.INVISIBLE)
        }

        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(view)
    }

    fun showDialog() {
        bottomSheetDialog.show()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.shareLinearLayout -> {
                    listener!!.onShareClick(currSlideImageView)
                    bottomSheetDialog.dismiss()
                }
                R.id.downloadLinearLayout -> {
                    listener!!.onDownloadClick(currposition)
                    bottomSheetDialog.dismiss()
                }
                R.id.deleteLinearLayout -> {
                    listener!!.onDeleteClick(currposition)
                    bottomSheetDialog.dismiss()
                }
            }
        }
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

}