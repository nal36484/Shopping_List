package com.vshabanov.shoppinglist.data_classes

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.activity.MainActivity

class DataBaseHelper() {
    var database: FirebaseDatabase
    var referenceList: DatabaseReference
    val shoppingList: MutableList<ShoppingList> = arrayListOf()
    val shoppingItems: MutableList<ShoppingItem> = arrayListOf()
    var id: String?
    var shoppingItem: ShoppingItem? = null

    init {
        id = Firebase.auth.currentUser?.uid
        database = FirebaseDatabase.getInstance()
        referenceList = database.getReference().child("users").child(id.toString()).child("list")
    }

    fun updatePhones(contacts: MutableList<Friend>) {
        val reference = database.getReference("users")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { val post = it.getValue(User::class.java) ?: User()
                    contacts.forEach {
                        if (post.phone == it.phone) {
                            //Log.d(MainActivity.TAG, "Fail")
                            id?.let { it1 ->
                                database.getReference().child("phones_contacts").child(it1)
                                    .child(post._id).child("phone")
                                    .setValue(post.phone)
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
        /*fun dataIsInserted()
        fun dataIsUpdated()
        fun dataIsDeleted()*/
    }
    interface ItemStatus {
        fun dataIsLoaded(shoppingItem: MutableList<ShoppingItem>)
        /*fun dataIsInserted()
        fun dataIsUpdated()
        fun dataIsDeleted()*/
    }

    fun readList(listStatus: ListStatus) {
        referenceList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoppingList.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(ShoppingList::class.java)?.let { it1 -> shoppingList.add(it1) }
                }
                listStatus.dataIsLoaded(shoppingList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun deletePos(_id: String) {
        referenceList.child(_id).removeValue()
    }

    fun readItems(key: String, itemStatus: ItemStatus) {
        referenceList.child(key).child("shoppingItems")
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
                TODO("Not yet implemented")
            }
        })
    }

    /*fun readItem(listKey: String, itemKey: String): ShoppingItem? {
        referenceList.child(listKey).child("shoppingItems")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.key.toString() == itemKey) {
                        shoppingItem = snapshot.getValue(ShoppingItem::class.java)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return shoppingItem
    }*/
}


