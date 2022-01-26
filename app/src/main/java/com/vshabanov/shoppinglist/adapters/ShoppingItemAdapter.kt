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
        var units: TextView? = null

        init {
            productName = itemView.findViewById(R.id.product)
            amount = itemView.findViewById(R.id.productCount)
            price = itemView.findViewById(R.id.productPrice)
            textPrice = itemView.findViewById(R.id.textViewPrice)
            editPrice = itemView.findViewById(R.id.editTextPrice)
            units = itemView.findViewById(R.id.productUnits)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_list_products,parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.productName?.text = item.name
        holder.amount?.text = item.amount
        holder.textPrice?.text = item.price + "₽"
        holder.editPrice?.setText(item.price)
        holder.amount?.setOnClickListener {
            clickListener.onItemClick(item)
        }
        holder.productName?.isChecked = item.status.toBoolean()
        holder.productName?.setOnClickListener {
            clickListener.onChecked(item)
        }
        holder.price?.setOnClickListener {
            clickListener.onPriceClick(it, item)
        }
        holder.units?.text = when (item.units) {
            "0" -> "шт"
            "1" -> "л"
            "2" -> "кг"
            "3" -> "г"
            else -> "шт"
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickListener {
        fun onItemClick(shoppingItem: ShoppingItem)
        fun onPriceClick(view: View, shoppingItem: ShoppingItem)
        fun onChecked(shoppingItem: ShoppingItem)
    }
    fun getChecked():MutableList<Boolean> {
        return checkedState
    }
}