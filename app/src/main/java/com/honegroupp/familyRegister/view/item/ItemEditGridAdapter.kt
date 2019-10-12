package com.honegroupp.familyRegister.view.item

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.*
import android.widget.*
import android.view.LayoutInflater
import android.net.Uri
import android.os.Build
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.utility.ImageRotateUtil
import com.honegroupp.familyRegister.view.item.ItemEdit
import com.squareup.picasso.Picasso
import com.honegroupp.familyRegister.utility.FilePathUtil
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_upload_page.*


class ItemEditGridAdapter(
    private val context: ItemEdit,
    private var detailImageUrls: ArrayList<String>,
    private val allUris: ArrayList<Uri>
) : BaseAdapter() {

    override fun getView(position:Int, convertView: View?, parent: ViewGroup?): View {

        val uriPosition = position - detailImageUrls.size
        val urlMaxPosition = detailImageUrls.size - 1
        val currentIsUrl = position <= urlMaxPosition
        // Inflate the view
        val inflater = parent?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var view = inflater.inflate(com.honegroupp.familyRegister.R.layout.upload_image,null)

        //set imageView size
        val imageView = view.findViewById<ImageView>(R.id.upload_image)
        val linearLayout = RelativeLayout.LayoutParams(330, 310)
        imageView.layoutParams = linearLayout

        if (isAddButton(position)){

            //load the the add sign to the imagView
            //set background colour
            imageView.setBackgroundColor(Color.rgb(240, 240, 240))
            imageView.setImageResource(R.drawable.add_thin_grey_512)
            imageView.setPadding(100,100,100,100)
            val layoutParams = RelativeLayout.LayoutParams(110, 110)
//            imageView.layoutParams = layoutParams
        } else if (currentIsUrl) {
            Picasso.get()
                .load(detailImageUrls[position])
                .placeholder(R.mipmap.loading_jewellery)
                .resize(360,360)
                .into(imageView)
        } else {

            //get the orientation and make sure image are at its original orientation
            val uri = allUris?.get(uriPosition)
            val path = FilePathUtil.getFilePathFromContentUri(uri!!, context!!)
            val orientation = ImageRotateUtil.getCameraPhotoOrientation(path!!).toFloat()

            //load the image the the view
            Picasso.get().load(uri).resize(330, 310).centerCrop().rotate(orientation).into(imageView)
        }

        //press the add button
        if (isAddButton(position)) {
            imageView.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED
                    ) {
                        //permission denied
                        context!!.requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1000
                        )
                    } else {
                        //permission already granted
                        context!!.selectImageInAlbum()
//                        context!!.Toast.makeText(this,"HAS PREMISSION11",Toast.LENGTH_SHORT).show()


                    }
                } else {
                    //system os less than mashmallow
//                    Toast.makeText(this,"less than mashmallow",Toast.LENGTH_SHORT).show()
                    context!!.selectImageInAlbum()
                }
            }
        }

        if (!isAddButton(position)) {
            val cancelBackground =
                view.findViewById<ImageView>(R.id.upload_image_cancel_background)
            val cancelButton = view.findViewById<ImageView>(R.id.upload_image_cancel_button)

            //set background of cancel button
            cancelBackground.setImageResource(R.drawable.ic_circle_white_24dp)
            cancelBackground.visibility = View.INVISIBLE

            //set cancal button
            cancelButton.setImageResource(R.drawable.ic_cancel_red_24dp)
            cancelButton.isEnabled = false
            cancelButton.visibility = View.INVISIBLE

            imageView.setOnLongClickListener {
                // long click to enable and disable cancelButton
                if (cancelButton.isEnabled) {
                    cancelButton.isEnabled = false
                    cancelButton.visibility = View.INVISIBLE
                    cancelBackground.visibility = View.INVISIBLE
                } else {
                    cancelButton.isEnabled = true
                    cancelButton.visibility = View.VISIBLE
                    cancelBackground.visibility = View.VISIBLE
                }
                true
            }

            cancelButton.setOnClickListener {
                if (currentIsUrl) {
                    context!!.removeItem(currentIsUrl, position)
                } else {
                    context!!.removeItem(currentIsUrl, uriPosition)
                }
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
        if (allUris == null){
            return detailImageUrls.size + 1
        }
        return detailImageUrls.size + allUris!!.size + 1
    }

    /*check whether the image view is the last one (add button)*/
    private fun isAddButton(position: Int):Boolean{
        return position == count -1
    }
}
