package com.honegroupp.familyRegister.view.item

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honegroupp.familyRegister.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit.*

class ItemEdit : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        Picasso.get()
            .load("https://firebasestorage.googleapis.com/v0/b/fir-image-uploader-98bb7.appspot.com/o/cxz%2FFurniture%2Fxz?alt=media&token=bb5101a1-c05e-4844-b008-fe2205f42359")
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(detailimage)

        findViewById<EditText>(R.id.editName).setText("HAHHA")
        findViewById<EditText>(R.id.editDescription).setText("-LpNG2FGsrwYSWbvoZ0-\n" + "sadassa")

        val detailImageView = findViewById<ImageView>(R.id.detailimage)
        registerForContextMenu(detailImageView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu!!.setHeaderTitle("Choose your option");
        getMenuInflater().inflate(R.menu.item_detail_menu, menu);
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_1 -> {
                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.option_2 -> {
                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
}