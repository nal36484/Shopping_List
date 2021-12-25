package com.vshabanov.shoppinglist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vshabanov.shoppinglist.Data_classes.ShoppingList
import com.vshabanov.shoppinglist.Adapters.ShoppingListAdapter
import com.vshabanov.shoppinglist.Data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ShoppingListAdapter.ClickListener {

    var shoppingList: MutableList<ShoppingList> = arrayListOf()
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        homeViewModel.shoppingList.observe(viewLifecycleOwner,{
            view?.findViewById<RecyclerView>(R.id.recyclerViewList)?.adapter = ShoppingListAdapter(it,this)
        })
        initShoppingListAdapter(root)
        return root
    }

    private fun initShoppingListAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewList)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton: FloatingActionButton = view.findViewById(R.id.button_add)
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.addListFragment) }
    }

    fun showMenu(view: View,shoppingList: ShoppingList) {
        val menupop = PopupMenu(context,view)
        val inflater = menupop.menuInflater
        inflater.inflate(R.menu.menu,menupop.menu)

        menupop.setOnMenuItemClickListener (PopupMenu.OnMenuItemClickListener { item:MenuItem? ->
            when (item?.itemId) {
                R.id.action_settings -> Toast.makeText(context, "Отправлено", Toast.LENGTH_SHORT).show()
                R.id.save_settings -> Toast.makeText(context, "Изменить невозможно", Toast.LENGTH_SHORT).show()
                R.id.delete_settings -> shoppingList._id?.let { DataBaseHelper().deletePos(it) }
            }
            true
        })
        menupop.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onListClick(shoppingList: ShoppingList) {
                findNavController().navigate(R.id.listNameFragment)
    }

    override fun onMenuClick(shoppingList: ShoppingList,position: Int) {
        view?.let { showMenu(it.findViewById(R.id.menu_status),shoppingList) }
    }

    fun createList() {
        var reference = FirebaseDatabase.getInstance().getReference()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.children
                    post.forEach {
                        it ->
                        it.getValue(ShoppingList::class.java)?.let { it1 -> shoppingList.add(it1) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}