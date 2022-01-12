package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.User

class SearchContactAdapter(var users: MutableList<User>, private val clickListener: ClickListener):
    RecyclerView.Adapter<SearchContactAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var phone: TextView? = null
        var email: TextView? = null
        var add: Button? = null

        init {
            phone = itemView.findViewById(R.id.textViewPhoneResult)
            email = itemView.findViewById(R.id.textViewEmailResult)
            add = itemView.findViewById(R.id.addFriend)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_search_result, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]
        holder.phone?.text = user.phone
        holder.email?.text = user.email
        holder.add?.setOnClickListener {
            clickListener.onAddClick(it, user._id)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    interface ClickListener {
        fun onAddClick(view: View, _id: String)
    }
}