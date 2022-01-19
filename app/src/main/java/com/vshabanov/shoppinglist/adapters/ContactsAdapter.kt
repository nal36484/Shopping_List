package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.Friend

class ContactsAdapter(var friends: MutableList<Friend>, private val clickListener: ClickListener):
    RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    private val checkedState: MutableList<Boolean> = arrayListOf()

    init {
        for (i in friends)
            checkedState.add(false)
    }

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var avatar: ImageView? = null
            var friendName: TextView? = null
            var friendPhone: TextView? = null
            var checkBox: CheckBox? = null

            init {
                checkBox = itemView.findViewById(R.id.select_contact)
                friendName = itemView.findViewById(R.id.contact_name)
                friendPhone = itemView.findViewById(R.id.contact_phone)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_friends_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friends[position]
        holder.friendName?.text = friend.name
        holder.friendPhone?.text = friend.phone
        holder.checkBox?.isChecked = checkedState[position]
        holder.checkBox?.setOnClickListener {
            checkedState[position] = !checkedState[position]
            if (checkedState[position])
                clickListener.ItemCheked(it, friend._id)
            else
                clickListener.ItemUnCheked(it, friend._id)
        }
    }

    interface ClickListener {
        fun ItemCheked(view: View, _id: String)
        fun ItemUnCheked(view: View, _id: String)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    fun getChecked():MutableList<Boolean> {
        return checkedState
    }
}