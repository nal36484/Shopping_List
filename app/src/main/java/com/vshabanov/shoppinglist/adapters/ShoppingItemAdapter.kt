package com.vshabanov.shoppinglist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.data_classes.ShoppingItem
import com.vshabanov.shoppinglist.R

class ShoppingItemAdapter(var items: MutableList<ShoppingItem>, private val clickListener: ClickListener):
    RecyclerView.Adapter<ShoppingItemAdapter.MyViewHolder>() {

    private val checkedState: MutableList<Boolean> = arrayListOf()

    init {
        for (i in items)
            checkedState.add(false)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productName: CheckBox? = null
        var amount: TextView? = null
        var price: FrameLayout? = null
        var textPrice: TextView? = null
        var editPrice: EditText? = null

        init {
            productName = itemView.findViewById(R.id.product)
            amount = itemView.findViewById(R.id.productCount)
            price = itemView.findViewById(R.id.productPrice)
            textPrice = itemView.findViewById(R.id.textViewPrice)
            editPrice = itemView.findViewById(R.id.editTextPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_list_products,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.get(position)
        holder.productName?.text = item.name
        holder.amount?.text = item.amount
        holder.textPrice?.text = item.price.toString()+"₽"
        holder.editPrice?.setText(item.price.toString())
        holder.amount?.setOnClickListener {
            clickListener.onItemClick(it, item)
        }
        holder.productName?.isChecked = checkedState[position]
        holder.productName?.setOnClickListener {
            checkedState[position] = !checkedState[position]
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickListener {
        fun onItemClick(view: View,shoppingItem: ShoppingItem)
    }
    fun getChecked():MutableList<Boolean> {
        return checkedState
    }
}