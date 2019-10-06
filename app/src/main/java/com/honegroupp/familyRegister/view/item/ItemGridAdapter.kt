package com.honegroupp.familyRegister.view.itemList

import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.*
import android.view.LayoutInflater
import android.net.Uri
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.utility.ImageRotateUtil
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import com.squareup.picasso.Picasso
import com.honegroupp.familyRegister.utility.FilePathUtil
import android.widget.LinearLayout





class ItemGridAdapter:BaseAdapter{
    private var context : ItemUploadActivity? = null
    private var allUris: ArrayList<Uri>? = null


    constructor( context: ItemUploadActivity, allUris: ArrayList<Uri>){
        this.context = context
        this.allUris = allUris
    }


    override fun getView(position:Int, convertView: View?, parent: ViewGroup?): View {

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
        }else{

            //get the orientation and make sure image are at its original orientation
            val uri = allUris?.get(position)
            val path = FilePathUtil.getFilePathFromContentUri(uri!!, context!!)
            val orientation = ImageRotateUtil.getCameraPhotoOrientation(path!!).toFloat()

            //load the image the the view
            Picasso.get().load(uri).rotate(orientation).into(imageView)
        }

        imageView.setOnClickListener {
            //press the add button
            if (isAddButton(position)){
                context!!.selectImageInAlbum()
            }
        }


        val cancelBackground =
            view.findViewById<ImageView>(R.id.upload_image_cancel_background)
        val cancelButton = view.findViewById<ImageView>(R.id.upload_image_cancel_button)
        imageView.setOnLongClickListener {

            if (!isAddButton(position)) {
                Toast.makeText(  context,
                    "Long click detected" + position.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                //set background of cancel button
                cancelBackground.setImageResource(R.drawable.ic_circle_white_24dp)

                //set cancal button
                cancelButton.setImageResource(R.drawable.ic_cancel_red_24dp)
            }
            true
        }

        cancelButton.setOnClickListener{
            context!!.removeItem(position)
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
            return 1
        }
        return allUris!!.size + 1
    }

    /*check whether the image view is the last one (add button)*/
    private fun isAddButton(position: Int):Boolean{
        return position == count -1
    }




}
