package com.honegroupp.familyRegister.view.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.honegroupp.familyRegister.R
import com.honegroupp.familyRegister.model.User
import com.squareup.picasso.Picasso

/**
 * This class is responsible for the adapter of the recycler in the ViewFamilyActivity
 *
 * */
class ViewFamilyAdapter(
    private val users: ArrayList<User>,
    val mActivity: AppCompatActivity
) : RecyclerView.Adapter<ViewFamilyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(mActivity).inflate(R.layout.family_member, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currUser = users[position]
        if (currUser.isFamilyOwner) {
            holder.ownerStar.visibility = View.VISIBLE
            holder.isOwnerText.visibility = View.VISIBLE
        } else {
            holder.ownerStar.visibility = View.INVISIBLE
            holder.isOwnerText.visibility = View.INVISIBLE
        }

        if (users[position].imageUrl != ""){
            Picasso.get()
                .load(users[position].imageUrl)
                .placeholder(R.mipmap.loading_jewellery)
                .fit()
                .centerCrop()
                .into(holder.memberImage)
        }
        holder.memberName.visibility = View.VISIBLE
        holder.memberName.text = currUser.username
    }


    override fun getItemCount(): Int {
        Log.d("ABCDEFG", users.size.toString())
        return users.size
    }

    inner class ViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val memberImage: ImageView = viewItem.findViewById(R.id.member_image)
        val memberName: TextView = viewItem.findViewById(R.id.member_name)
        val ownerStar: ImageView = viewItem.findViewById(R.id.owner_star)
        val isOwnerText: TextView = viewItem.findViewById(R.id.is_owner)
    }

}