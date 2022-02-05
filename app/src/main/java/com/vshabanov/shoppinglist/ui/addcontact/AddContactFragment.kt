package com.vshabanov.shoppinglist.ui.addcontact

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.adapters.SearchContactAdapter
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.User
import com.vshabanov.shoppinglist.databinding.FragmentAddContactBinding
import com.vshabanov.shoppinglist.util.hideKeyBoard

class AddContactFragment : Fragment() {

    var users: MutableList<User> = arrayListOf()
    private lateinit var adapter: SearchContactAdapter
    private lateinit var clickListener: SearchContactAdapter.ClickListener

    private lateinit var addContactViewModel: AddContactViewModel
    private var _binding: FragmentAddContactBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clickListener = object : SearchContactAdapter.ClickListener {
            override fun onAddClick(user: User) {
                DataBaseHelper().friendRequest(user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addContactViewModel =
                ViewModelProvider(this).get(AddContactViewModel::class.java)

        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initSearchContactAdapter(root)

        return root
    }

    private fun initSearchContactAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSearchResult)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchContactAdapter(users, clickListener)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number: EditText = view.findViewById(R.id.phoneNumber)
        val search: ImageButton = view.findViewById(R.id.searchContact)
        search.setOnClickListener {
            val phone = number.text.toString()
            if (phone == "") {
                return@setOnClickListener
            } else {
                DataBaseHelper().searchContact(phone, object : DataBaseHelper.UserSearch {
                    override fun dataIsLoaded(users: MutableList<User>) {
                        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSearchResult)
                        recyclerView.adapter = SearchContactAdapter(users, clickListener)
                    }
                })
            }
        }
    }

    override fun onStop() {
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        activity?.hideKeyBoard(requireView())
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}