package com.vshabanov.shoppinglist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vshabanov.shoppinglist.Data_classes.ShoppingList

class AddListFragment : Fragment() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.getReference()

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
        val editText = view.findViewById<EditText>(R.id.list_name)

        createButton.setOnClickListener {
            val name = editText.text.toString()
            if (name=="")
                reference.push().setValue(ShoppingList())
            else
                reference.push().setValue(ShoppingList(name = name))
            val action = AddListFragmentDirections.actionAddListFragmentToListNameFragment(name)
            view.findNavController().navigate(action) }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
        super.onStop()
    }
}

