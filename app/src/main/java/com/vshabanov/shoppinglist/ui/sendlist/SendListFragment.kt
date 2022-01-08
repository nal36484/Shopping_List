package com.vshabanov.shoppinglist.ui.sendlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.adapters.ContactsAdapter
import com.vshabanov.shoppinglist.data_classes.Friend
import com.vshabanov.shoppinglist.databinding.FragmentSendListBinding

class SendListFragment : Fragment() {

    var friends: MutableList<Friend> = arrayListOf()
    private lateinit var adapter: ContactsAdapter

    private lateinit var sendListViewModel: SendListViewModel
    private var _binding: FragmentSendListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        sendListViewModel.friends.observe(viewLifecycleOwner, {
            view?.findViewById<RecyclerView>(R.id.recyclerViewFriends)?.adapter = ContactsAdapter(it)
        })

        return root
    }

    private fun initContactsAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewFriends)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ContactsAdapter(friends)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}