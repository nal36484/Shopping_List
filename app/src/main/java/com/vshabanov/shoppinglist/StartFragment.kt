package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.Data_classes.ShoppingList
import com.vshabanov.shoppinglist.Adapters.ShoppingListAdapter


class StartFragment : Fragment(), ShoppingListAdapter.ClickListener {

    var shoppingList: MutableList<ShoppingList> = arrayListOf()
    private lateinit var adapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)
        initShoppingListAdapter(rootView)
        return rootView
    }

    private fun initShoppingListAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ShoppingListAdapter(shoppingList,this)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton: FloatingActionButton = view.findViewById(R.id.button_add)
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.addListFragment) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_start,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //android.R.id.home -> findNavController().navigate(R.id.listNameFragment)
            R.id.personal -> findNavController().navigate(R.id.loginFragment)
        //MyDialogFragment().show(childFragmentManager,"custom")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onListClick(shoppingList: ShoppingList) {
        findNavController().navigate(R.id.listNameFragment)
    }

    override fun onMenuClick(shoppingList: ShoppingList,position : Int) {
        TODO("Not yet implemented")
    }
}