package com.vshabanov.shoppinglist.ui.itemshow

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
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

    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var settings: SharedPreferences
    private lateinit var listId: String
    private lateinit var adapter: ShoppingItemAdapter
    private lateinit var listNameViewModel: ListNameViewModel
    private var _binding: FragmentListNameBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("listId", Context.MODE_PRIVATE)!!
        listId = settings.getString("listId","").toString()
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
        itemList(items)
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

    override fun onItemClick(view: View,shoppingItem: ShoppingItem) {
        TODO("Not yet implemented")
    }
    fun itemList(items: MutableList<ShoppingItem>) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val id: String? = Firebase.auth.currentUser?.uid
        val referenceItem: DatabaseReference = database.getReference().child("users").child(id.toString()).child("list")
        referenceItem.child(listId).child("shoppingItems")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(ShoppingItem::class.java)?.let { it1 -> items.add(it1) }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}