package com.vshabanov.shoppinglist.adapters

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val request = requests[position]
        holder.who?.text = ("Пользователь ${request.email} хочет добавить вас в список контактов")
        holder.accept?.setOnClickListener {
            clickListener.onAcceptClick(request)
        }
        holder.decline?.setOnClickListener {
            clickListener.onDeclineClick(request)
        }
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    interface ClickListener {
        fun onAcceptClick(request: Friend)
        fun onDeclineClick(request: Friend)
    }
}