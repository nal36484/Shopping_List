package com.vshabanov.shoppinglist.Data_classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R

class ShoppingListAdapter(private var names: MutableList<ShoppingList>):
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
        val shoppingList = names.get(position)
        holder.nameList?.setText(shoppingList.name)
        holder.count?.setText(shoppingList.count)
    }

    override fun getItemCount(): Int {
        return names.size
    }
}



