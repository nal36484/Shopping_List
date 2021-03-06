package com.vshabanov.shoppinglist.ui.addproduct

import android.content.Context
import android.os.Bundle

import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.adapters.AddProductAdapter
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.ShoppingItem
import com.vshabanov.shoppinglist.databinding.FragmentAddProductBinding
import com.vshabanov.shoppinglist.util.hideKeyBoard

class AddProductFragment : Fragment() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.reference.child("users")
    val auth = Firebase.auth
    var listKey: String? = null

    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var adapter: AddProductAdapter
    private lateinit var clickListener : AddProductAdapter.ClickListener
    private lateinit var addProductViewModel: AddProductViewModel
    private var _binding: FragmentAddProductBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listKey = context?.getSharedPreferences("listId", Context.MODE_PRIVATE)
            ?.getString("listId","").toString()
        clickListener = object : AddProductAdapter.ClickListener {
            override fun onDeleteClick(shoppingItem: ShoppingItem) {
                listKey?.let { DataBaseHelper().deleteItem(it,shoppingItem._id) }
            }

            override fun onSpinnerClick(position: Int, _id: String) {
                val userId = auth.currentUser?.uid.toString()
                listKey?.let {
                    reference.child(userId).child("list").child(it).child("shoppingItems")
                        .child(_id).child("units").setValue(position.toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addProductViewModel =
            ViewModelProvider(this).get(AddProductViewModel::class.java)

        _binding = FragmentAddProductBinding.inflate(inflater,container,false)

        val root: View = binding.root
        initAddProductAdapter(root)
        addProductViewModel.itemsList.observe(viewLifecycleOwner) {
            view?.findViewById<RecyclerView>(R.id.recyclerViewAddProduct)?.adapter =
                AddProductAdapter(it, requireContext(), clickListener)
        }

        return root
    }

    private fun initAddProductAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAddProduct)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AddProductAdapter(items, requireContext(),clickListener)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productName: EditText = view.findViewById(R.id.nameProduct)
        val count: EditText = view.findViewById(R.id.amountProduct)
        val add: Button = view.findViewById(R.id.add)
        add.setOnClickListener {
            val name = productName.text.toString()
            var amount = count.text.toString()
            if (name=="")
                return@setOnClickListener
            else if((name != "") && (amount == "")) {
                amount = "1"
                writeNewPost(name, amount, listKey)
                productName.setText("")
                count.setText("")
            } else {
                writeNewPost(name, amount, listKey)
                productName.setText("")
                count.setText("")
            }
        }
    }

    private fun writeNewPost(name: String, amount: String, _id: String?) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = reference.push().key.toString()
        val userId = auth.currentUser?.uid
        val post = ShoppingItem(key,name, amount)
        val postValues = post.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "$userId/list/$_id/shoppingItems/$key" to postValues,
        )
        reference.updateChildren(childUpdates)
    }

    override fun onPause() {
        super.onPause()
        addProductViewModel.dataBaseHelper.refItems
            .removeEventListener(addProductViewModel.dataBaseHelper.refListener)
    }

    override fun onResume() {
        super.onResume()
        addProductViewModel.dataBaseHelper.refItems
            .addValueEventListener(addProductViewModel.dataBaseHelper.refListener)
    }

    override fun onStop() {
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        activity?.hideKeyBoard(requireView())
        super.onStop()
    }
}