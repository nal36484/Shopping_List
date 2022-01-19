package com.vshabanov.shoppinglist.data_classes

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.activity.MainActivity

class DataBaseHelper() {

    val shoppingList: MutableList<ShoppingList> = arrayListOf()
    val shoppingItems: MutableList<ShoppingItem> = arrayListOf()
    val friends: MutableList<Friend> = arrayListOf()
    val messages: MutableList<Message> = arrayListOf()
    val users: MutableList<User> = arrayListOf()
    val requests: MutableList<Friend> = arrayListOf()

    val currentId: String = Firebase.auth.currentUser?.uid.toString()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val refUsers : DatabaseReference = database.reference.child("users")
    private var refList: DatabaseReference = refUsers.child(currentId).child("list")
    private val refFriends : DatabaseReference = refUsers.child(currentId).child("friends")
    private val refMessages: DatabaseReference = database.reference.child("messages")
    private val refFriendRequests: DatabaseReference = database.reference.child("friend_requests")

    fun updatePhones(contacts: MutableList<Friend>) {
        val reference = refUsers
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { val post = it.getValue(User::class.java) ?: User()
                    contacts.forEach { contact ->
                        if (post.phone == contact.phone) {
                            val friend =
                                Friend(post._id, post.email, contact.name, contact.phone, post.photo)
                            currentId.let { it1 ->
                                database.getReference().child("users").child(it1)
                                    .child("friends").child(it.key.toString())
                                    .setValue(friend)
                                    .addOnFailureListener{
                                        Log.d(MainActivity.TAG, "Fail")
                                    }
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(MainActivity.TAG, "Fail")
            }
        })
    }

    interface ListStatus {
        fun dataIsLoaded(shoppingList: MutableList<ShoppingList>)
    }
    interface ItemStatus {
        fun dataIsLoaded(shoppingItem: MutableList<ShoppingItem>)
    }
    interface FriendStatus {
        fun dataIsLoaded(friends: MutableList<Friend>)
    }
    interface MessageStatus {
        fun dataIsLoaded(messages: MutableList<Message>)
    }
    interface CurrentList {
        fun dataIsLoaded(list: ShoppingList)
    }
    interface UserSearch {
        fun dataIsLoaded(users: MutableList<User>)
    }
    interface FriendRequests {
        fun dataIsLoaded(requests: MutableList<Friend>)
    }
    interface CurrentUserData {
        fun dataIsLoaded(userData: User)
    }

    fun getCurrentUser(currentUserData: CurrentUserData) {
        refUsers.child(currentId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue(User::class.java) ?: User()
                currentUserData.dataIsLoaded(post)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getFriendRequests(friendRequests: FriendRequests) {
        refFriendRequests.child(currentId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requests.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(Friend::class.java)?.let {it1 -> requests.add(it1)}
                }
                friendRequests.dataIsLoaded(requests)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun readMessages(messageStatus: MessageStatus) {
        refMessages.child(currentId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(Message::class.java)?.let { it1 -> messages.add(it1) }
                }
                messageStatus.dataIsLoaded(messages)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun createList(userId: String, listId: String, list: CurrentList) {
        refUsers.child(userId).child("list").child(listId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue(ShoppingList::class.java) ?: ShoppingList()
                    list.dataIsLoaded(post)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun readFriends(friendStatus : FriendStatus) {
        refFriends.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friends.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(Friend::class.java)?.let { it1 -> friends.add(it1) }
                }
                friendStatus.dataIsLoaded(friends)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun readList(listStatus: ListStatus) {
        refList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoppingList.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(ShoppingList::class.java)?.let { it1 -> shoppingList.add(it1) }
                }
                listStatus.dataIsLoaded(shoppingList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun deleteList(_id: String) {
        refList.child(_id).removeValue()
    }

    fun deleteItem(listId: String, itemId: String) {
        refList.child(listId).child("shoppingItems").child(itemId).removeValue()
    }

    fun deleteMessage(_id: String) {
        refMessages.child(currentId).child(_id).removeValue()
    }

    fun deleteRequest(_id: String) {
        refFriendRequests.child(currentId).child(_id).removeValue()
    }

    fun readItems(key: String, itemStatus: ItemStatus) {
        refList.child(key).child("shoppingItems")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoppingItems.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(ShoppingItem::class.java)?.let { it1 -> shoppingItems.add(it1) }
                }
                itemStatus.dataIsLoaded(shoppingItems)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun sendMessage(receivingUserId: String, listId: String) {
        if (receivingUserId == currentId)
            return

        val refReceivingUser = "messages/$receivingUserId"
        val messageKey = database.reference.child("messages").push().key

        val mapMessage = hashMapOf<String,Any>()
        mapMessage["_id"] = messageKey.toString()
        mapMessage["listId"] = listId
        mapMessage["from"] = currentId

        val mapDialog = hashMapOf<String,Any>()
        mapDialog["$refReceivingUser/$messageKey"] = mapMessage

        database.reference.updateChildren(mapDialog)
            //.addOnSuccessListener{ Log.d(MainActivity.TAG, "Success") }
            //.addOnFailureListener { Log.d(MainActivity.TAG, "Fail") }
    }

    fun searchContact(phone: String, userSearch: UserSearch) {
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                snapshot.children.forEach{
                    if (it.child("phone").value == phone) {
                        val post = it.getValue(User::class.java) ?: User()
                        users.add(post)
                    }
                }
                userSearch.dataIsLoaded(users)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun friendRequest(user: User) {
        if (user._id == currentId)
            return
        val refFriendsRequest = "friend_requests/${user._id}/$currentId"
        getCurrentUser(object : DataBaseHelper.CurrentUserData {
            override fun dataIsLoaded(userData: User) {
                val currentUser = Friend(userData._id, userData.email, userData.name, userData.phone)
                database.reference.child(refFriendsRequest).setValue(currentUser)
            }
        })
    }
}


