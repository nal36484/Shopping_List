package com.vshabanov.shoppinglist.adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.data_classes.ShoppingItem
import com.vshabanov.shoppinglist.R
import android.widget.AdapterView
import com.vshabanov.shoppinglist.activity.MainActivity

class AddProductAdapter(var items: MutableList<ShoppingItem>, var context: Context,
                        private val clickListener: ClickListener):
    RecyclerView.Adapter<AddProductAdapter.MyViewHolder>() {

    private val units: ArrayList<String> = arrayListOf("Штуки","Литры","Килограммы","Граммы")

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productName: TextView? = null
        var amount: TextView? = null
        var delete: ImageButton? = null
        var spinner: Spinner? = null

        init {
            productName = itemView.findViewById(R.id.productName)
            amount = itemView.findViewById(R.id.productAmount)
            delete = itemView.findViewById(R.id.productDelete)
            spinner = itemView.findViewById(R.id.spinnerSelectUnits)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_list_add_products,parent,false)
        val spinner = itemView.findViewById<Spinner>(R.id.spinnerSelectUnits)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, units)
        spinner.adapter = adapter
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.productName?.text = item.name
        holder.amount?.text = item.amount
        holder.delete?.setOnClickListener {
            clickListener.onDeleteClick(item)
        }
        holder.spinner?.setSelection(item.units.toInt())
        holder.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view != null) {
                    clickListener.onSpinnerClick(position, item._id)
                }
                return
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickListener {
        //fun onItemClick(amount: String, shoppingItem: ShoppingItem)
        fun onDeleteClick(shoppingItem: ShoppingItem)
        fun onSpinnerClick(position: Int, _id: String)
    }
}