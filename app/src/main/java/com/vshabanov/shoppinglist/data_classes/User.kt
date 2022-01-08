package com.vshabanov.shoppinglist.data_classes

data class User(
    var _id: String = "",
    var email: String = "",
    var friends: HashMap<String,Friend> = hashMapOf<String,Friend>(),
    var list: HashMap<String,ShoppingList> = hashMapOf<String,ShoppingList>(),
    var name: String = "",
    var phone: String = ""
)
