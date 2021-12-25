package com.vshabanov.shoppinglist.Data_classes

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class DataBaseHelper() {
    var database: FirebaseDatabase
    var reference: DatabaseReference
    val shoppingList: MutableList<ShoppingList> = arrayListOf()
    init {
        database = FirebaseDatabase.getInstance()
        reference = database.getReference()
    }
    interface DataStatus {
        fun dataIsLoaded(shoppingList: MutableList<ShoppingList>)
        /*fun dataIsInserted()
        fun dataIsUpdated()
        fun dataIsDeleted()*/
    }

    fun readList(dataStatus: DataStatus) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoppingList.clear()

                val post = snapshot.children
                post.forEach {
                    it.getValue(ShoppingList::class.java)?.let { it1 -> shoppingList.add(it1) }
                }
                dataStatus.dataIsLoaded(shoppingList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun deletePos(_id: String) {
        reference.child(_id).removeValue()
    }
}


