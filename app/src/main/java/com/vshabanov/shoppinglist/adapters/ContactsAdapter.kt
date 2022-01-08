package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.Friend

class ContactsAdapter(var friends: MutableList<Friend>):
    RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var avatar: ImageView? = null
            var friendName: TextView? = null
            var friendPhone: TextView? = null

            init {
                friendName = itemView.findViewById(R.id.contact_name)
                friendPhone = itemView.findViewById(R.id.contact_phone)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friends.get(position)
        holder.friendName?.setText(friend.name)
        holder.friendPhone?.setText(friend.phone)
    }

    override fun getItemCount(): Int {
        return friends.size
    }
}