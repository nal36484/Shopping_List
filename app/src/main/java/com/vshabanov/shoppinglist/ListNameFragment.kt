package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.Data_classes.ShoppingItem
import com.vshabanov.shoppinglist.Data_classes.ShoppingItemAdapter

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
        val addButton: FloatingActionButton = view.findViewById(R.id.add_product)
        val nameList = arguments?.let { StartFragmentArgs.fromBundle(it).nameList }
        if (nameList=="")
                (requireActivity() as AppCompatActivity).supportActionBar?.title = "Новый список"
        else
                (requireActivity() as AppCompatActivity).supportActionBar?.title = nameList
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
}