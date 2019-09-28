package com.honegroupp.familyRegister.view.itemList

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.*
import android.view.LayoutInflater
import android.net.Uri
import android.widget.Toast.*
import com.honegroupp.familyRegister.view.item.ItemUploadActivity
import com.squareup.picasso.Picasso

import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.R.drawable.ic_add_black_24dp


class ItemGridAdapter:BaseAdapter{
    private var context : ItemUploadActivity? = null
    private var allUris: ArrayList<Uri>? = null


    constructor( context: ItemUploadActivity, allUris: ArrayList<Uri>){
        this.context = context
        this.allUris = allUris
    }

    @SuppressLint("ResourceType")
    override fun getView(position:Int, convertView: View?, parent: ViewGroup?): View {

        // Inflate the view
        val inflater = parent?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        var view = inflater.inflate(com.honegroupp.familyRegister.R.layout.upload_image,null)

        //set imageView size
        val imageView = view.findViewById<ImageView>(R.id.upload_image)
        val linearLayout = RelativeLayout.LayoutParams(330, 310)
        imageView.layoutParams = linearLayout

        if (position == count -1){

            //load the the add sign to the imagView
            imageView.setImageResource(R.drawable.ic_add_black_24dp)
        }else{

            //load the image to view
            Picasso.get().load(allUris?.get(position)).into(imageView)
        }



        imageView.setOnClickListener {
            //press the add button
            if(position == count -1){
                context!!.selectImageInAlbum()
            }
        }




        imageView.setOnLongClickListener {
            Toast.makeText(context, "Long click detected" + position.toString(), Toast.LENGTH_SHORT).show()

            //set background of cancel button
            val cancelBackground = view.findViewById<ImageView>(R.id.upload_image_cancel_background)
            cancelBackground.setImageResource(R.drawable.ic_circle_white_24dp)

            //set cancal button
            val cancelButton = view.findViewById<ImageView>(R.id.upload_image_cancel_button)
            cancelButton.setImageResource(R.drawable.ic_cancel_red_24dp)
            true
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

    fun isAddBUtton(position: Int):Boolean{
        if (position == count -1){
            return true
        }
    }

}
