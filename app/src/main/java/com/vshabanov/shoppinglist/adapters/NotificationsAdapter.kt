package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.Message

class NotificationsAdapter(var messages: MutableList<Message>,private val clickListener: ClickListener):
    RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var from: TextView? = null
            var description: TextView? = null
            var accept: ImageButton? = null
            var decline: ImageButton? = null

            init {
                from = itemView.findViewById(R.id.textViewFrom)
                description = itemView.findViewById(R.id.textViewDescription)
                accept = itemView.findViewById(R.id.acceptList)
                decline = itemView.findViewById(R.id.declineList)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_notifications_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messages[position]
        holder.from?.text = ("Вам пришёл список, отправитель ${message.nameFrom}")
        holder.accept?.setOnClickListener {
            clickListener.onAcceptClick(message)
        }
        holder.decline?.setOnClickListener {
            clickListener.onDeclineClick(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    interface ClickListener {
        fun onAcceptClick(message: Message)
        fun onDeclineClick(message: Message)
    }
}