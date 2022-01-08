package com.vshabanov.shoppinglist.ui.addlist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.ShoppingList

class AddListFragment : Fragment() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.getReference().child("users")
    val auth = Firebase.auth

    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = context?.getSharedPreferences("listId", Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createButton: Button = view.findViewById(R.id.button_create)
        val listName = view.findViewById<EditText>(R.id.list_name)

        createButton.setOnClickListener {
            val name = listName.text.toString()
            if (name=="")
                writeNewPost("Новый список")
            else
                writeNewPost(name)
            listName.setText("")
            val action = AddListFragmentDirections.actionAddListFragmentToListNameFragment(name)
            view.findNavController().navigate(action) }
    }

    override fun onStop() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
        super.onStop()
    }

    private fun writeNewPost(name: String) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = reference.push().key.toString()
        val userId = auth.currentUser?.uid
        val post = ShoppingList(key, name)
        val postValues = post.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "$userId/list/$key" to postValues,
        )
        reference.updateChildren(childUpdates)
        val editor = settings.edit()
        editor.putString("listId",key)
        editor.apply()
    }
}

