package com.honegroupp.familyRegister.view.item

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.controller.ItemController.Companion.editItem
import com.honegroupp.familyRegister.model.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit.*

class ItemEdit : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // get extra from Item Detail(DetailSlide)
        val itemKey = intent.getStringExtra("ItemKey")
        val currFamilyId = intent.getStringExtra("FamilyId")

        // retrieve Item
        lateinit var currItem: Item
        val rootPath = "/"
        val databaseRef = FirebaseDatabase.getInstance().getReference(rootPath)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Don't ignore errors!
                Log.d("TAG", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                currItem =
                    p0
                        .child(FirebaseDatabaseManager.FAMILY_PATH)
                        .child(currFamilyId)
                        .child("items")
                        .child(itemKey)
                        .getValue(Item::class.java) as Item

                // set current item to view
                Picasso.get()
                    .load(currItem.imageURLs[0])
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(editItemImage)
                findViewById<EditText>(R.id.editName).setText(currItem.itemName)
                findViewById<EditText>(R.id.editDescription).setText(currItem.itemDescription)

                // set on click listener
                editConfirm.setOnClickListener{
                    if(editName.text.toString() == ""){
                        Toast.makeText(this@ItemEdit, "Item name should not leave blank",Toast.LENGTH_SHORT).show()
//                    }else if(numberOfImages == 0) {
//                        Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show()
//                    }else if(numberOfImages != imagePathList.size){
//                        Toast.makeText(this, "Please wait for uploading image", Toast.LENGTH_SHORT).show()
                    }else {
                        val updatedItem = Item(
                            itemName = editName.text.toString(),
                            itemDescription = editDescription.text.toString(),
                            itemOwnerUID = currItem.itemOwnerUID,
                            imageURLs = currItem.imageURLs,
                            isPublic = currItem.isPublic
                        )
                        // upload to update item
                        val itemPath =
                            FirebaseDatabaseManager.FAMILY_PATH + currFamilyId + "/" + "items/" + itemKey
                        val databaseRef = FirebaseDatabase.getInstance().getReference(itemPath)

                        databaseRef.child("").setValue(updatedItem)

                        // Go back to the previous activity
                        this@ItemEdit.finish()
                    }
                }
            }

        })
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