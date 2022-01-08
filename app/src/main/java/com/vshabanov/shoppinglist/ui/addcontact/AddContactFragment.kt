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
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.databinding.FragmentAddContactBinding

class AddContactFragment : Fragment() {

    private lateinit var addContactViewModel: AddContactViewModel
    private var _binding: FragmentAddContactBinding? = null
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
        addContactViewModel =
                ViewModelProvider(this).get(AddContactViewModel::class.java)

        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val code: EditText = view.findViewById(R.id.countryCode)
        val number: EditText = view.findViewById(R.id.phoneNumber)
        val search: ImageButton = view.findViewById(R.id.searchContact)
        search.setOnClickListener {
        }
    }

    override fun onStop() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}