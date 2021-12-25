package com.vshabanov.shoppinglist.Data_classes

data class ShoppingList(
    var _id: String? = "",
    var name: String? = "Новый список",
    var count: String? = "0/0",
    var shoppingItem: MutableList<ShoppingItem>? = arrayListOf()
) {

    /*@Exclude
   fun toMap(): Map<String,Any?> {
       return mapOf(
           "uid" to _id,
           "name" to name,
           "count" to count,
           "shoppingItem" to shoppingItem
       )
   }*/
}