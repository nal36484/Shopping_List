package com.vshabanov.shoppinglist.ui.friendrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.adapters.FriendRequestAdapter
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Friend
import com.vshabanov.shoppinglist.data_classes.User
import com.vshabanov.shoppinglist.databinding.FragmentFriendRequestBinding

class FriendRequestFragment : Fragment(), FriendRequestAdapter.ClickListener {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.reference.child("users")
    val auth = Firebase.auth

    var requests: MutableList<Friend> = arrayListOf()
    private lateinit var adapter: FriendRequestAdapter

    private lateinit var friendRequestViewModel: FriendRequestViewModel
    private var _binding: FragmentFriendRequestBinding? = null
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
        friendRequestViewModel =
                ViewModelProvider(this).get(FriendRequestViewModel::class.java)
        _binding = FragmentFriendRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initFriendRequestAdapter(root)
        friendRequestViewModel.friendRequests.observe(viewLifecycleOwner, {
            view?.findViewById<RecyclerView>(R.id.recyclerViewFriendRequest)?.adapter =
                FriendRequestAdapter(it, this)
        })

        return root
    }

    private fun initFriendRequestAdapter(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewFriendRequest)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FriendRequestAdapter(requests, this)
        recyclerView.adapter = adapter
    }

    override fun onAcceptClick(view: View, request: Friend) {
        writeNewPost(request)
        DataBaseHelper().deleteRequest(request._id)
    }

    override fun onDeclineClick(view: View, request: Friend) {
        DataBaseHelper().deleteRequest(request._id)
    }

    private fun writeNewPost(request: Friend) {
        val currentUser = auth.currentUser?.uid
        val postFriend = request.toMap()
        DataBaseHelper().getCurrentUser(object : DataBaseHelper.CurrentUserData {
            override fun dataIsLoaded(userData: User) {
                val friend =
                    Friend(userData._id, userData.email, userData.name, userData.phone, userData.photo)
                val postMe = friend.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    "$currentUser/friends/${request._id}" to postFriend,
                    "${request._id}/friends/$currentUser" to postMe,
                )
                reference.updateChildren(childUpdates)
            }
        })
    }
}