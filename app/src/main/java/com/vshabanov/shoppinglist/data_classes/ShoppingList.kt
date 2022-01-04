package com.vshabanov.shoppinglist.data_classes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingList(
    var _id: String = "",
    var name: String = "Новый список",
    var count: String = "0/0",
    var shoppingItem: MutableList<ShoppingItem> = arrayListOf()
) {

    @Exclude
   fun toMap(): Map<String,Any?> {
       return mapOf(
           "_id" to _id,
           "name" to name,
           "count" to count,
           "shoppingItem" to shoppingItem
       )
   }
}