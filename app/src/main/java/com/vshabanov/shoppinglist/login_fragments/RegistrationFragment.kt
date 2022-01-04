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
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.activity.LoginActivity
import com.vshabanov.shoppinglist.activity.MainActivity

class RegistrationFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var emailAddress: EditText
    lateinit var password: EditText
    lateinit var phone: EditText
    lateinit var registration: Button
    var reference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database?.reference?.child("users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registration = view.findViewById(R.id.apply_registration)
        emailAddress = view.findViewById(R.id.registrationEmailAddress)
        password = view.findViewById(R.id.registrationPassword)
        phone = view.findViewById(R.id.registrationPhone)
        registration()
    }

    private fun registration() {
        registration.setOnClickListener {
            if (TextUtils.isEmpty(emailAddress.text.toString())) {
                emailAddress.setError("Please enter email address")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(password.text.toString())) {
                password.setError("Please enter password")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(phone.text.toString())) {
                phone.setError("Please enter phone number")
                return@setOnClickListener
            }
            val credential = EmailAuthProvider.getCredential(emailAddress.text.toString(),password.text.toString())
            auth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = it.result?.user
                        val currentUserDb = reference?.child((currentUser?.uid!!))
                        currentUserDb?.child("email")?.setValue(emailAddress.text.toString())
                        currentUserDb?.child("password")?.setValue(password.text.toString())
                        currentUserDb?.child("phone")?.setValue(phone.text.toString())
                        Toast.makeText(context,"Registration Success.",Toast.LENGTH_LONG).show()
                        startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        Toast.makeText(context,it.exception.toString(),Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}