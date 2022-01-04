package com.vshabanov.shoppinglist.data_classes

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

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

    fun readItem(listKey: String, itemKey: String): ShoppingItem? {
        referenceList.child(listKey).child("shoppingItems").child(itemKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue(ShoppingItem::class.java)
                    shoppingItem = post
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return shoppingItem
    }
}


