package com.vshabanov.shoppinglist.Data_classes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R

class ShoppingItemAdapter(private var names: List<ShoppingItem>):
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
        val productName = names.get(position)
        holder.productName?.setText(productName.name)
        holder.amount?.setText(productName.amount)
        holder.price?.setText(productName.price.toString()+"₽")
    }

    override fun getItemCount(): Int {
        return names.size
    }
}