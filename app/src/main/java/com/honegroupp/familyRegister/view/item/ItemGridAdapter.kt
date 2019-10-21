package com.honegroupp.familyRegister.view.itemList

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.utility.FilePathUtil
import com.honegroupp.familyRegister.utility.ImageRotateUtil
import com.honegroupp.familyRegister.view.item.GridViewItem
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import com.squareup.picasso.Picasso

/**
 *Thus adapter is to adapt the images in to the gridview
 * */
class ItemGridAdapter(
    private val context: ItemUploadActivity,
    private val allUris: ArrayList<Uri>
) : BaseAdapter() {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        // Inflate the view
        val inflater =
                parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //inflate the view
        var view = inflater.inflate(R.layout.upload_image, null)

        //set imageView size
        val imageView = view.findViewById<GridViewItem>(R.id.upload_image)


        //show the image
        if (isAddButton(position)) {
            //load the the add sign to the imageView
            //set background colour
            imageView.setBackgroundColor(Color.rgb(240, 240, 240))
            imageView.setImageResource(R.drawable.add_thin_blue_512)

        } else {

            //get the orientation and make sure image are at its original orientation
            val uri = allUris?.get(position)
            val path = FilePathUtil.getFilePathFromContentUri(uri!!, context!!)
            val orientation =
                    ImageRotateUtil.getCameraPhotoOrientation(path!!).toFloat()

            //load the image the the view
            Picasso.get().load(uri).resize(1000, 1000).centerCrop()
                .rotate(orientation).into(imageView)
        }

        imageView.setOnClickListener {
            //press the add button
            if (isAddButton(position)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        context!!.requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1000)
                    } else {
                        //permission already granted
                        context!!.selectImageInAlbum()
                    }
                } else {
                    //system os less than mashmallow
                    context!!.selectImageInAlbum()
                }
            }
        }

        if (!isAddButton(position)) {
            val cancelBackground =
                    view
                        .findViewById<ImageView>(R.id.upload_image_cancel_background)
            val cancelButton =
                    view
                        .findViewById<ImageView>(R.id.upload_image_cancel_button)

            //set background of cancel button
            cancelBackground.setImageResource(R.drawable.ic_circle_white_24dp)
            cancelBackground.visibility = View.INVISIBLE

            //set cancel button
            cancelButton.setImageResource(R.drawable.ic_cancel_red_24dp)
            cancelButton.isEnabled = false
            cancelButton.visibility = View.INVISIBLE

            imageView.setOnLongClickListener {

                // long click to enable and disable cancelButton
                if (cancelButton.isEnabled) {
                    // long click to enable
                    cancelButton.isEnabled = false
                    cancelButton.visibility = View.INVISIBLE
                    cancelBackground.visibility = View.INVISIBLE
                } else {
                    // long click to disable
                    cancelButton.isEnabled = true
                    cancelButton.visibility = View.VISIBLE
                    cancelBackground.visibility = View.VISIBLE
                }
                true
            }
            cancelButton.setOnClickListener {
                context!!.removeItem(position)
            }
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        if (allUris == null) {
            return 1
        }
        return allUris!!.size + 1
    }

    /**
     * check whether the image view is the last one (add button)
     * */
    private fun isAddButton(position: Int): Boolean {
        return position == count - 1
    }

}
