package com.vshabanov.shoppinglist.ui.itemshow

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.data_classes.ShoppingItem
import com.vshabanov.shoppinglist.adapters.ShoppingItemAdapter
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.ShoppingList
import com.vshabanov.shoppinglist.databinding.FragmentListNameBinding

class ListNameFragment : Fragment(), ShoppingItemAdapter.ClickListener {

    private lateinit var settings: SharedPreferences
    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var adapter: ShoppingItemAdapter
    private lateinit var listNameViewModel: ListNameViewModel
    private var _binding: FragmentListNameBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("listId", Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listNameViewModel =
            ViewModelProvider(this).get(ListNameViewModel::class.java)

        _binding = FragmentListNameBinding.inflate(inflater,container,false)

        val root: View = binding.root
        listNameViewModel.itemsList.observe(viewLifecycleOwner,{
            view?.findViewById<RecyclerView>(R.id.recyclerViewProducts)?.adapter = ShoppingItemAdapter(it,this)
        })
        initShoppingItemAdapter(root)
        return root
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
        //(requireActivity() as AppCompatActivity).supportActionBar?.title = args.toString()
        addButton.setOnClickListener {
            val args = arguments?.let { ListNameFragmentArgs.fromBundle(it).name}
            val action = ListNameFragmentDirections.actionListNameFragmentToProductAddFragment(args.toString())
            view.findNavController().navigate(action) }
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Shopping List"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(view: View,shoppingItem: ShoppingItem) {
        val editor = settings.edit()
        editor.putString("itemId",shoppingItem._id.toString())
        editor.apply()
        view.findNavController().navigate(R.id.editProductFragment)
    }
}