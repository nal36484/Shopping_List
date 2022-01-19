package com.vshabanov.shoppinglist.ui.editproduct

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.databinding.FragmentEditProductBinding

class EditProductFragment : Fragment() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var id: String? = Firebase.auth.currentUser?.uid
    var referenceList: DatabaseReference =
        database.reference.child("users").child(id.toString()).child("list")


    private lateinit var editProductViewModel: EditProductViewModel
    private var _binding: FragmentEditProductBinding? = null

    var itemName: EditText? = null
    var itemAmount: EditText? = null
    var itemPrice: EditText? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editProductViewModel =
                ViewModelProvider(this).get(EditProductViewModel::class.java)

        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        val root: View = binding.root
        itemName = binding.editTextItemName
        editProductViewModel.itemName.observe(viewLifecycleOwner, {
            itemName?.setText(it)
        })
        itemAmount = binding.editTextItemAmount
        editProductViewModel.itemAmount.observe(viewLifecycleOwner, {
            itemAmount?.setText(it)
        })
        itemPrice = binding.editTextItemPrice
        editProductViewModel.itemPrice.observe(viewLifecycleOwner, {
            itemPrice?.setText(it)
        })

        return root
    }

    override fun onPause() {
        super.onPause()
        val name = itemName?.text.toString()
        val amount = itemAmount?.text.toString()
        val price = itemPrice?.text.toString()
        referenceList.child(editProductViewModel.listKey).child("shoppingItems")
            .child(editProductViewModel.itemKey).child("name").setValue(name)
        referenceList.child(editProductViewModel.listKey).child("shoppingItems")
            .child(editProductViewModel.itemKey).child("amount").setValue(amount)
        referenceList.child(editProductViewModel.listKey).child("shoppingItems")
            .child(editProductViewModel.itemKey).child("price").setValue(price)
    }

    override fun onStop() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
        super.onStop()
    }
}