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
import com.google.firebase.auth.FirebaseAuth
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.activity.MainActivity

class SignInFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signIn = view.findViewById(R.id.button_sign_in)
        email = view.findViewById(R.id.edit_login)
        password = view.findViewById(R.id.edit_password)
        login(view)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
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
                        startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        Toast.makeText(context,it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}