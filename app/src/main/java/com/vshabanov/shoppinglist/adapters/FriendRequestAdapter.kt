package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.Friend

class FriendRequestAdapter(var requests: MutableList<Friend>, private val clickListener: ClickListener):
    RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var who: TextView? = null
            var accept: ImageButton? = null
            var decline: ImageButton? = null

            init {
                who = itemView.findViewById(R.id.textViewWho)
                accept = itemView.findViewById(R.id.acceptFriendRequest)
                decline = itemView.findViewById(R.id.declineFriendRequest)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_friend_request, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request = requests[position]
        holder.who?.text = ("Пользователь ${request.email} хочет добавить вас в список контактов")
        holder.accept?.setOnClickListener {
            clickListener.onAcceptClick(it, request)
        }
        holder.decline?.setOnClickListener {
            clickListener.onDeclineClick(it, request)
        }
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    interface ClickListener {
        fun onAcceptClick(view: View, request: Friend)
        fun onDeclineClick(view: View, request: Friend)
    }
}