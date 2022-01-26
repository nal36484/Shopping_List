package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.Friend

class FriendsAdapter(var friends: MutableList<Friend>): RecyclerView.Adapter<FriendsAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var avatar: ImageView? = null
            var friendName: TextView? = null
            var friendPhone: TextView? = null

            init {
                avatar = itemView.findViewById(R.id.avatar)
                friendName = itemView.findViewById(R.id.contact_name)
                friendPhone = itemView.findViewById(R.id.contact_phone)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_contacts_list, parent , false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friends[position]
        holder.friendName?.text = friend.name
        holder.friendPhone?.text = friend.phone
        Picasso.get()
            .load(friend.photo)
            .placeholder(R.drawable.ic_default_user)
            .into(holder.avatar)
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}