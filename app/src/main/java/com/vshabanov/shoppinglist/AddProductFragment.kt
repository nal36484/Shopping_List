package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.Adapters.AddProductAdapter
import com.vshabanov.shoppinglist.Data_classes.ShoppingItem

class Product_Add_Fragment : Fragment(), AddProductAdapter.ClickListener {

    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var adapter: AddProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_product, container, false)
        initAddProductAdapter(rootView)
        return rootView
    }

    fun initAddProductAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAddProduct)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AddProductAdapter(items,this)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_add_product,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        //(requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onStop() {
        super.onStop()
        //(requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onItemClick(shoppingItem: ShoppingItem) {
        TODO("Not yet implemented")
    }
}