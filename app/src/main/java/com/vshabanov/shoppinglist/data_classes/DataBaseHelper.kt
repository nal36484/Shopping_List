package com.vshabanov.shoppinglist.data_classes

import android.app.Application
import android.content.SharedPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class DataBaseHelper(){
    var database: FirebaseDatabase
    var referenceList: DatabaseReference
    val shoppingList: MutableList<ShoppingList> = arrayListOf()
    val shoppingItem: MutableList<ShoppingItem> = arrayListOf()
    var id: String? = Firebase.auth.currentUser?.uid
    var key: String? = null

    init {
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

    fun readItem(itemStatus: ItemStatus) {
        referenceList.child(key.toString()).child("shoppingItems")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoppingItem.clear()
                val post = snapshot.children
                post.forEach {
                    it.getValue(ShoppingItem::class.java)?.let { it1 -> shoppingItem.add(it1) }
                }
                itemStatus.dataIsLoaded(shoppingItem)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}


