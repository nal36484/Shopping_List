package com.vshabanov.shoppinglist.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.adapters.NotificationsAdapter
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Message
import com.vshabanov.shoppinglist.data_classes.ShoppingList
import com.vshabanov.shoppinglist.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.reference.child("users")
    val auth = Firebase.auth

    var messages: MutableList<Message> = arrayListOf()
    private lateinit var adapter: NotificationsAdapter
    private lateinit var clickListener: NotificationsAdapter.ClickListener

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clickListener = object : NotificationsAdapter.ClickListener {
            override fun onAcceptClick(message: Message) {
                var shoppingList : ShoppingList
                DataBaseHelper().createList(message.from, message.listId, object : DataBaseHelper.CurrentList {
                    override fun dataIsLoaded(list: ShoppingList) {
                        shoppingList = list
                        writeNewPost(shoppingList)
                    }
                })
                DataBaseHelper().deleteMessage(message._id)
                Toast.makeText(context, "Список добавлен", Toast.LENGTH_SHORT).show()
            }

            override fun onDeclineClick(message: Message) {
                DataBaseHelper().deleteMessage(message._id)
                Toast.makeText(context, "Отклонено", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initNotificationsAdapter(root)
        notificationsViewModel.messages.observe(viewLifecycleOwner) {
            view?.findViewById<RecyclerView>(R.id.recyclerViewNotifications)?.adapter =
                NotificationsAdapter(it, clickListener)
        }

        return root
    }

    private fun initNotificationsAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NotificationsAdapter(messages, clickListener)
        recyclerView.adapter = adapter
    }

    private fun writeNewPost(shoppingList: ShoppingList) {
        val userId = auth.currentUser?.uid
        val postValues = shoppingList.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "$userId/list/${shoppingList._id}" to postValues,
        )
        reference.updateChildren(childUpdates)
    }
}