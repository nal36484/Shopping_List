package com.vshabanov.shoppinglist.ui.itemshow

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
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

class ListNameFragment : Fragment() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var id: String? = Firebase.auth.currentUser?.uid
    var referenceList: DatabaseReference =
        database.reference.child("users").child(id.toString()).child("list")


    private lateinit var settings: SharedPreferences
    var items: MutableList<ShoppingItem> = arrayListOf()
    private lateinit var adapter: ShoppingItemAdapter
    private lateinit var click: ShoppingItemAdapter.ClickListener
    private lateinit var listNameViewModel: ListNameViewModel
    private var _binding: FragmentListNameBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("listId", Context.MODE_PRIVATE)!!
        click = object : ShoppingItemAdapter.ClickListener {
            override fun onItemClick(shoppingItem: ShoppingItem) {
                val editor = settings.edit()
                editor.putString("itemId",shoppingItem._id.toString())
                editor.apply()
                view?.findNavController()?.navigate(R.id.editProductFragment)
            }

            override fun onPriceClick(view: View, shoppingItem: ShoppingItem) {
                val textView = view.findViewById<TextView>(R.id.textViewPrice)
                val editText = view.findViewById<EditText>(R.id.editTextPrice)
                textView.visibility = View.GONE
                editText.visibility = View.VISIBLE
                editText.setSelectAllOnFocus(true)
                editText.requestFocus()
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            var price = editText.text.toString()
                            if (price == "")
                                price = "0"
                            referenceList.child(listNameViewModel.listId).child("shoppingItems")
                                .child(shoppingItem._id).child("price").setValue(price)
                            if (price == shoppingItem.price) {
                                textView.visibility = View.VISIBLE
                                editText.visibility = View.GONE
                            }
                            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                            return true
                        }
                        return false
                    }
                })
            }

            override fun onChecked(shoppingItem: ShoppingItem) {
                val status =
                    if (shoppingItem.status == "false") {
                        "true"
                    } else {
                        "false"
                    }
                referenceList.child(listNameViewModel.listId).child("shoppingItems")
                    .child(shoppingItem._id).child("status").setValue(status)
            }
        }
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
        initShoppingItemAdapter(root)
        listNameViewModel.itemsList.observe(viewLifecycleOwner) {
            view?.findViewById<RecyclerView>(R.id.recyclerViewProducts)?.adapter =
                ShoppingItemAdapter(it, click)
        }

        return root
    }

    private fun initShoppingItemAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ShoppingItemAdapter(items,click)
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
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        listNameViewModel.dataBaseHelper.refItems
            .removeEventListener(listNameViewModel.dataBaseHelper.refListener)
        var itemCount = 0
        var statusCount = 0
        listNameViewModel.itemsList.observe(viewLifecycleOwner) {
            itemCount = it.size
            it.forEach {
                if (it.status == "true")
                    statusCount++
            }
        }
        referenceList.child(listNameViewModel.listId).child("count")
            .setValue("$statusCount/$itemCount")
    }

    override fun onResume() {
        super.onResume()
        listNameViewModel.dataBaseHelper.refItems
            .addValueEventListener(listNameViewModel.dataBaseHelper.refListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

