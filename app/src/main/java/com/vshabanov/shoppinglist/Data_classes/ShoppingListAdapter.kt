package com.vshabanov.shoppinglist.Data_classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R

class ShoppingListAdapter(var shoppingList: MutableList<ShoppingList>, private val clickListener: ClickListener):
    RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameList: TextView? = null
        var count: TextView? = null

        init {
            nameList = itemView.findViewById(R.id.name_list)
            count = itemView.findViewById(R.id.amount)
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
        holder.itemView.setOnClickListener {
            clickListener.onListClick(list)
        }
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    interface ClickListener {
        fun onListClick(shoppingList: ShoppingList)
    }
}





