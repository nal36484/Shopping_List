package com.vshabanov.shoppinglist.login_fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.activity.MainActivity
import com.vshabanov.shoppinglist.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signIn: Button
    private lateinit var signInViewModel: SignInViewModel
    private var _binding: FragmentSignInBinding? = null
    // This property is only valid between onCreateView and
    //    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signInViewModel =
                ViewModelProvider(this).get(SignInViewModel::class.java)

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val root: View = binding.root
        signInViewModel.registration.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signIn = view.findViewById(R.id.button_sign_in)
        email = view.findViewById(R.id.edit_login)
        password = view.findViewById(R.id.edit_password)
        login(view)
    }

    private fun login(view: View) {
        signIn.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString())) {
                email.error = "Please enter email"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password.text.toString())) {
                email.error = "Please enter password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signInViewModel.changeData()
                    } else {
                        Toast.makeText(context,it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}