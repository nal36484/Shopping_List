package com.vshabanov.shoppinglist.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.Data_classes.ShoppingItem
import com.vshabanov.shoppinglist.R

class ShoppingItemAdapter(var items: MutableList<ShoppingItem>, private val clickListener: ClickListener):
    RecyclerView.Adapter<ShoppingItemAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productName: CheckBox? = null
        var amount: TextView? = null
        var price: TextView? = null

        init {
            productName = itemView.findViewById(R.id.product)
            amount = itemView.findViewById(R.id.productCount)
            price = itemView.findViewById(R.id.productPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_products,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.get(position)
        holder.productName?.setText(item.name)
        holder.amount?.setText(item.amount)
        holder.price?.setText(item.price.toString()+"₽")
        holder.itemView.setOnClickListener {
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