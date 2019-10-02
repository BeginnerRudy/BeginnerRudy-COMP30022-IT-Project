package com.honegroupp.familyRegister.view.item

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slide_detail_images_layout.view.*
import kotlinx.android.synthetic.main.slide_dimage_layout.view.*

class DetailImagesSliderAdapter(val items: ArrayList<String>, val context: Context) : PagerAdapter(), View.OnClickListener {

    var listener: OnItemClickerListener? = null
    lateinit var currSlideImageView: ImageView
    var currposition = 0

    interface OnItemClickerListener {
        fun onImageClick(position: Int, items: ArrayList<String>)
//        fun setMenu(slideImageView: ImageView)
        fun onDownloadClick(position: Int)
        fun onShareClick(imageView: ImageView)
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
            .placeholder(R.drawable.loading_jewellery)
            .into(slideImageView)

//        listener!!.setMenu(slideImageView)

        view.findViewById<ImageView>(R.id.detail_images).setOnClickListener{
            Log.d("ddddtailclickonviewp",items.toString())
            listener!!.onImageClick(position, items)
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
        var shareLinearLayout = view.findViewById<LinearLayout>(R.id.shareLinearLayout)
        var uploadLinearLayout = view.findViewById<LinearLayout>(R.id.uploadLinearLayout)
        var copyLinearLayout = view.findViewById<LinearLayout>(R.id.copyLinearLayout)

        shareLinearLayout.setOnClickListener(this)
        uploadLinearLayout.setOnClickListener(this)
        copyLinearLayout.setOnClickListener(this)

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
                    Log.d("ooooclick", currposition.toString())
                    bottomSheetDialog.dismiss()
                }
                R.id.uploadLinearLayout -> {
                    listener!!.onDownloadClick(currposition)
                    Log.d("ooooclick", "upload")
                    bottomSheetDialog.dismiss()
                }
                R.id.copyLinearLayout -> {
                    Log.d("ooooclick", "copy")
                    bottomSheetDialog.dismiss()
                }
            }
        }
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

}