package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.data_classes.ShoppingList
import com.vshabanov.shoppinglist.R

class ShoppingListAdapter(var shoppingList: MutableList<ShoppingList>, private val clickListener: ClickListener):
    RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameList: TextView? = null
        var count: TextView? = null
        var menu: ImageButton? = null

        init {
            nameList = itemView.findViewById(R.id.name_list)
            count = itemView.findViewById(R.id.amount)
            menu = itemView.findViewById(R.id.menu_status)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_items,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = shoppingList.get(position)
        holder.nameList?.setText(list.name)
        holder.count?.setText(list.count)
        holder.nameList?.setOnClickListener {
            clickListener.onListClick(it,list)
        }
        holder.menu?.setOnClickListener{
            clickListener.onMenuClick(it,list,position)
        }
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    interface ClickListener {
        fun onListClick(view: View, shoppingList: ShoppingList)
        fun onMenuClick(view: View, shoppingList: ShoppingList, position: Int)
    }
}





