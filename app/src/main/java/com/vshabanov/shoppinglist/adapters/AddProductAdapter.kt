package com.vshabanov.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.data_classes.ShoppingItem
import com.vshabanov.shoppinglist.R

class AddProductAdapter(var items: MutableList<ShoppingItem>, private val clickListener: ClickListener):
    RecyclerView.Adapter<AddProductAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productName: TextView? = null
        var amount: EditText? = null

        init {
            productName = itemView.findViewById(R.id.productName)
            amount = itemView.findViewById(R.id.productAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_add_products,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.get(position)
        holder.productName?.setText(item.name)
        holder.amount?.setText(item.price)
        holder.productName?.setOnClickListener {
            clickListener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickListener {
        fun onItemClick(shoppingItem: ShoppingItem)
    }
}