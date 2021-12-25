package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.Data_classes.ShoppingItem
import com.vshabanov.shoppinglist.Adapters.ShoppingItemAdapter
import com.vshabanov.shoppinglist.ui.home.HomeFragment

class ListNameFragment : Fragment(), ShoppingItemAdapter.ClickListener {

    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var adapter: ShoppingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_name, container, false)
        initShoppingItemAdapter(rootView)
        return rootView
    }

    fun initShoppingItemAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ShoppingItemAdapter(items,this)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton: FloatingActionButton = view.findViewById(R.id.add_product)
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.product_Add_Fragment) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_add_name,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Shopping List"
    }

    override fun onItemClick(shoppingItem: ShoppingItem) {
        TODO("Not yet implemented")
    }
}