package com.vshabanov.shoppinglist.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.data_classes.ShoppingList
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.activity.MainActivity
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper

class ShoppingListAdapter(var shoppingList: MutableList<ShoppingList>, private val clickListener: ClickListener):
    RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameList: FrameLayout? = null
        var count: TextView? = null
        var menu: ImageButton? = null
        var textName: TextView? = null
        var editName: EditText? = null

        init {
            nameList = itemView.findViewById(R.id.name_list)
            count = itemView.findViewById(R.id.amount)
            menu = itemView.findViewById(R.id.menu_status)
            textName = itemView.findViewById(R.id.textViewListName)
            editName = itemView.findViewById(R.id.editTextListName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_list_items,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = shoppingList[position]
        holder.textName?.text = list.name
        holder.editName?.setText(list.name)
        holder.editName?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    var name = holder.editName?.text.toString()
                    if (name == "")
                        name = "Новый Список"
                    DataBaseHelper().setName(list._id, name)
                    list.stateName = "false"
                    notifyDataSetChanged()
                    return true
                }
                return false
            }
        })
        holder.count?.text = list.count
        holder.nameList?.setOnClickListener {
            clickListener.onListClick(list)
        }
        holder.menu?.setOnClickListener{
            clickListener.onMenuClick(it, list)
        }
        if (list.stateName == "false") {
            holder.textName?.visibility = View.VISIBLE
            holder.editName?.visibility = View.GONE
        } else {
            holder.textName?.visibility = View.GONE
            holder.editName?.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    interface ClickListener {
        fun onListClick(shoppingList: ShoppingList)
        fun onMenuClick(view: View, shoppingList: ShoppingList)
    }
}





