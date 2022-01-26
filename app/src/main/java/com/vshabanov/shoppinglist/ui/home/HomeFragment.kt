package com.vshabanov.shoppinglist.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.data_classes.ShoppingList
import com.vshabanov.shoppinglist.adapters.ShoppingListAdapter
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.databinding.FragmentHomeBinding

class HomeFragment() : Fragment() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var id: String? = Firebase.auth.currentUser?.uid
    var referenceList: DatabaseReference =
        database.reference.child("users").child(id.toString()).child("list")

    private lateinit var settings: SharedPreferences
    var shoppingList: MutableList<ShoppingList> = arrayListOf()
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var click: ShoppingListAdapter.ClickListener

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("listId", Context.MODE_PRIVATE)!!
        click = object : ShoppingListAdapter.ClickListener {
            override fun onListClick(shoppingList: ShoppingList) {
                val editor = settings.edit()
                editor.putString("listId",shoppingList._id)
                editor.apply()

                val action = HomeFragmentDirections.actionNavHomeToListNameFragment(shoppingList._id)
                view?.findNavController()?.navigate(action)
            }

            override fun onMenuClick(view: View, shoppingList: ShoppingList) {
                showMenu(view.findViewById(R.id.menu_status), shoppingList)
            }
        }
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
        initShoppingListAdapter(root)
        homeViewModel.shoppingList.observe(viewLifecycleOwner,{
            view?.findViewById<RecyclerView>(R.id.recyclerViewList)?.adapter = ShoppingListAdapter(it,click)
        })

        return root
    }

    private fun initShoppingListAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ShoppingListAdapter(shoppingList,click)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton: FloatingActionButton = view.findViewById(R.id.button_add)
        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.addListFragment) }
    }

    private fun showMenu(view: View, shoppingList: ShoppingList) {
        val menuPop = PopupMenu(context,view)
        val inflater = menuPop.menuInflater
        inflater.inflate(R.menu.menu,menuPop.menu)

        menuPop.setOnMenuItemClickListener (PopupMenu.OnMenuItemClickListener { item:MenuItem? ->
            when (item?.itemId) {
                R.id.action_settings -> if (isAnonymous()) {
                    Toast.makeText(context, "Необходима авторизация", Toast.LENGTH_SHORT).show()
                } else {
                    val action = HomeFragmentDirections.actionNavHomeToSendListFragment(shoppingList._id)
                    view.findNavController().navigate(action)
                }
                R.id.save_settings -> Toast.makeText(context, "Изменить невозможно", Toast.LENGTH_SHORT).show()
                R.id.delete_settings -> DataBaseHelper().deleteList(shoppingList._id)
            }
            true
        })
        menuPop.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onListClick(view: View,shoppingList: ShoppingList) {
//        val editor = settings.edit()
//        editor.putString("listId",shoppingList._id)
//        editor.apply()
//
//        val action = HomeFragmentDirections.actionNavHomeToListNameFragment(shoppingList._id)
//                view.findNavController().navigate(action)
//    }
//
//    override fun onMenuClick(view: View, shoppingList: ShoppingList) {
//        showMenu(view.findViewById(R.id.menu_status), shoppingList)
//    }

    private fun isAnonymous(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.isAnonymous == true
    }

    /*private fun changeListName(view: View, shoppingList: ShoppingList) {
        val textView = view.findViewById<TextView>(R.id.textViewListName)
        val editText = view.findViewById<EditText>(R.id.editTextListName)
        textView.visibility = View.GONE
        editText.visibility = View.VISIBLE
        editText.setSelectAllOnFocus(true)
        editText.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    var name = editText.text.toString()
                    if (name == "")
                        name = "Новый Список"
                    referenceList.child(shoppingList._id).child("name").setValue(name)
                    if (name == shoppingList.name) {
                        textView.visibility = View.VISIBLE
                        editText.visibility = View.GONE
                    }
                    return true
                }
                return false
            }
        })
    }*/
}