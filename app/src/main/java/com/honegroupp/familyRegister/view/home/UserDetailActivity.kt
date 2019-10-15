package com.honegroupp.familyRegister.view.home
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.*
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.backend.FirebaseDatabaseManager
import com.honegroupp.familyRegister.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user.*


class UserDetailActivity : AppCompatActivity() {
    lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Configure the toolbar setting
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.change_user_photo)
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp)
        toolbar.setNavigationOnClickListener{
            finish()
        }

        //get the uid from intent
        val uid = intent.getStringExtra("UserID")


        // retrieve User
        lateinit var currUser: User
        val rootPath = "/"
        databaseRef = FirebaseDatabase.getInstance().getReference(rootPath)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Don't ignore errors!
                Log.d("TAG", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val t = object : GenericTypeIndicator<ArrayList<String>>() {}

                // get current Item from data snap shot
                currUser = p0
                        .child(FirebaseDatabaseManager.USER_PATH)
                        .child(uid)
                        .getValue(User::class.java) as User

                //set user's name in the page
                val currUserName = currUser.username
                findViewById<TextView>(R.id.username).text = currUserName

                //get the image URL
                val imageView = findViewById<ImageView>(R.id.user_image)
                val imageUrl = currUser.imageUrl

                // Load image to ImageView via its URL from Firebase Storage
                if(imageUrl!="") {
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.mipmap.loading_jewellery)
                        .fit()
                        .centerCrop()
                        .into(imageView)
                }

                //click edit button
                user_edit.setOnClickListener{
                    startEdit(uid,currUserName,currUser.imageUrl,currUser.isFamilyOwner.toString(),currUser.familyId)
                }


            }
        })
        
    }

    //start the edit user activity
    fun startEdit(uid:String, usernameString:String, imageUrl:String,isFamilyOwner:String,familyId:String){
        val intent = Intent(this, UserEditActivity::class.java)
        intent.putExtra("familyId",familyId)
        intent.putExtra("UserID", uid)
        intent.putExtra("UserName", usernameString)
        intent.putExtra("ImageUrl", imageUrl)
        intent.putExtra("IsFamilyOwner",isFamilyOwner)
        startActivity(intent)
    }

}
