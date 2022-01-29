package com.vshabanov.shoppinglist.ui.contacts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.adapters.ContactsAdapter
import com.vshabanov.shoppinglist.adapters.FriendsAdapter
import com.vshabanov.shoppinglist.data_classes.Friend
import com.vshabanov.shoppinglist.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    var friends: MutableList<Friend> = arrayListOf()
    private lateinit var adapter: FriendsAdapter
    var request: Int = 1

    private lateinit var contactsViewModel: ContactsViewModel
    private var _binding: FragmentContactsBinding? = null
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
        contactsViewModel =
                ViewModelProvider(this).get(ContactsViewModel::class.java)

        _binding = FragmentContactsBinding.inflate(inflater,container,false)
        val root: View = binding.root
        initContactsAdapter(root)
        contactsViewModel.friends.observe(viewLifecycleOwner) {
            view?.findViewById<RecyclerView>(R.id.recyclerViewContacts)?.adapter =
                FriendsAdapter(it)
        }

        return root
    }

    private fun initContactsAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FriendsAdapter(friends)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { checkPermission(it) }
        val searchContact: FloatingActionButton = view.findViewById(R.id.search_contact)
        searchContact.setOnClickListener{
            view.findNavController().navigate(R.id.addContactFragment)
        }
    }
    private fun checkPermission(context: Context) {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            contactsViewModel.readContacts()
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_CONTACTS), request)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == request) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactsViewModel.readContacts()
            }
        } else {
            Toast.makeText(context, "Доступ к контактам отклонён", Toast.LENGTH_SHORT).show()
        }
    }
}