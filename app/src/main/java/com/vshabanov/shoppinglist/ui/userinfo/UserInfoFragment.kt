package com.vshabanov.shoppinglist.ui.userinfo

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CAMERA_BUTTON
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.reference.child("users")
    var currentId: String = Firebase.auth.currentUser?.uid.toString()

    private lateinit var userInfoViewModel: UserInfoViewModel
    private var _binding: FragmentUserInfoBinding? = null

    var phone: EditText? = null
    var userName: EditText? = null
    var photo: ImageView? = null

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
        userInfoViewModel =
                ViewModelProvider(this).get(UserInfoViewModel::class.java)

        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        phone = binding.editTextPhone
        userName = binding.editTextUserName
        photo = binding.imageViewPhoto
        userInfoViewModel.user.observe(viewLifecycleOwner, {
            userName?.setText(it.name)
            phone?.setText(it.phone)
            Picasso.get()
                .load(it.photo)
                .placeholder(R.drawable.ic_default_user)
                .into(photo)
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val save: Button = view.findViewById(R.id.saveState)
        save.setOnClickListener {
            val name = userName?.text.toString()
            val phone = phone?.text.toString()
            val phoneVerification = Regex("""^(\+\d|\d)+\d{10}""")
            if (phone.matches(phoneVerification)) {
                reference.child(currentId).child("name").setValue(name)
                reference.child(currentId).child("phone").setValue(phone)
                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Неверный формат номера телефона", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        val image: ImageButton = view.findViewById(R.id.imageButtonSetPhoto)
        image.setOnClickListener {
            context?.let { it1 -> checkPermission(it1) }
        }
    }
    private fun checkPermission(context: Context) {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            setPhoto()
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun setPhoto() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val intentList: ArrayList<Intent> = arrayListOf(camera, pickPhoto)
        startActivityForResult(camera,1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPhoto()
            }
        } else {
            Toast.makeText(context, "Доступ к камере отклонён", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val photoBitMap = data?.extras?.get("data") as Bitmap
            photo?.setImageBitmap(photoBitMap)
        }
    }
}