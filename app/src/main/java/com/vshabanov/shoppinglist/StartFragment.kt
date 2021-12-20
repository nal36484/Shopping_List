package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.Data_classes.ShoppingList
import com.vshabanov.shoppinglist.Data_classes.ShoppingListAdapter


class StartFragment : Fragment() {

    var names: MutableList<ShoppingList> = arrayListOf()
    val shoppingListAdapter: ShoppingListAdapter = ShoppingListAdapter(names)

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)
        val list: RecyclerView = rootView.findViewById(R.id.recyclerViewList)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = ShoppingListAdapter(names)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton: FloatingActionButton = view.findViewById(R.id.button_add)
        /*val args: StartFragmentArgs by navArgs()
        val nameList = args.nameList
        if (args.nameList=="")
            names.add(ShoppingList())
        else
            names.add(ShoppingList(nameList))*/

        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.addListFragment) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_start,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}