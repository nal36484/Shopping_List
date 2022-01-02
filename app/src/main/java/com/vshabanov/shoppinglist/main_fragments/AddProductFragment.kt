package com.vshabanov.shoppinglist

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.adapters.AddProductAdapter
import com.vshabanov.shoppinglist.data_classes.ShoppingItem

class AddProductFragment : Fragment(), AddProductAdapter.ClickListener {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.getReference().child("users")
    val auth = Firebase.auth

    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var adapter: AddProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        //setHasOptionsMenu(true)
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
        val productName: EditText = view.findViewById(R.id.nameProduct)
        val count: EditText = view.findViewById(R.id.amountProduct)
        val add: Button = view.findViewById(R.id.add)
        add.setOnClickListener {
            val name = productName.text.toString()
            val amount = count.text.toString()
            if (name=="")
                return@setOnClickListener
            else
                writeNewPost(name, amount)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_add_product,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*when(item.itemId) {
            R.id.personal -> startActivity(Intent(context, LoginActivity::class.java))
        }*/
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(shoppingItem: ShoppingItem) {
        TODO("Not yet implemented")
    }
    private fun writeNewPost(name: String, amount: String) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val args = arguments?.let { AddProductFragmentArgs.fromBundle(it).name.toString() }
        val key = reference.push().key
        val userId = auth.currentUser?.uid
        val post = ShoppingItem(name, amount)
        val postValues = post.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "$userId/list/$args/shoppingItems/$key" to postValues,
        )

        reference.updateChildren(childUpdates)
    }
}