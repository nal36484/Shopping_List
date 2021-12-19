package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.Data_classes.ShoppingItem
import com.vshabanov.shoppinglist.Data_classes.ShoppingItemAdapter
import com.vshabanov.shoppinglist.Data_classes.ShoppingList

class ListNameFragment : Fragment() {

    var names: MutableList<ShoppingItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_name, container, false)
        val list: RecyclerView = rootView.findViewById(R.id.recyclerViewProducts)
        list.adapter = ShoppingItemAdapter(names)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton: FloatingActionButton = view.findViewById(R.id.button_add)
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.addListFragment) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_add_list,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}