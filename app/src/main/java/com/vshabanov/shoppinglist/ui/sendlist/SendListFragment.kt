package com.vshabanov.shoppinglist.ui.sendlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.activity.MainActivity
import com.vshabanov.shoppinglist.adapters.ContactsAdapter
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Friend
import com.vshabanov.shoppinglist.data_classes.ShoppingList
import com.vshabanov.shoppinglist.databinding.FragmentSendListBinding

class SendListFragment : Fragment() {

    var friends: MutableList<Friend> = arrayListOf()
    private lateinit var adapter: ContactsAdapter
    private lateinit var clickListener: ContactsAdapter.ClickListener
    private val selectedFriends: MutableList<String> = arrayListOf()

    private lateinit var sendListViewModel: SendListViewModel
    private var _binding: FragmentSendListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clickListener = object : ContactsAdapter.ClickListener {
            override fun itemChecked(_id: String) {
                selectedFriends.add(_id)
            }

            override fun itemUnChecked(_id: String) {
                selectedFriends.remove(_id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendListViewModel =
            ViewModelProvider(this).get(SendListViewModel::class.java)

        _binding = FragmentSendListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initContactsAdapter(root)
        sendListViewModel.friends.observe(viewLifecycleOwner) {
            view?.findViewById<RecyclerView>(R.id.recyclerViewFriends)?.adapter =
                ContactsAdapter(it, clickListener)
        }

        return root
    }

    private fun initContactsAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewFriends)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ContactsAdapter(friends, clickListener)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonSend: FloatingActionButton = view.findViewById(R.id.send_list)
        val listId = arguments?.let { SendListFragmentArgs.fromBundle(it).listId}
        buttonSend.setOnClickListener {
            if (selectedFriends.isEmpty()) {
                return@setOnClickListener
            }
            else {
                for (friend in selectedFriends) {
                    DataBaseHelper().sendMessage(friend, listId.toString())
                }
                Toast.makeText(context, "????????????????????", Toast.LENGTH_SHORT).show()
                view.findNavController().navigate(R.id.nav_home)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sendListViewModel.dataBaseHelper.refFriends
            .removeEventListener(sendListViewModel.dataBaseHelper.refListener)
    }

    override fun onResume() {
        super.onResume()
        sendListViewModel.dataBaseHelper.refFriends
            .addValueEventListener(sendListViewModel.dataBaseHelper.refListener)
    }
}