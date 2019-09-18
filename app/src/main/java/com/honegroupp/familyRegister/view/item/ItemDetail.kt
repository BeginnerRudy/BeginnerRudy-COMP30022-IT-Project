package com.honegroupp.familyRegister.view.item

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class ItemDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageView = ImageView(this)

        val imgResId = R.drawable.cp
        var resId = imgResId
        Picasso.get()
            .load("https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/cxz%2FFurniture%2Fxz?alt=media&token=bb5101a1-c05e-4844-b008-fe2205f42359")
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(detailimage);

        click(changebutton)
    }

    fun click(button: Button){
        button.setOnClickListener {
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
        }
    }
}