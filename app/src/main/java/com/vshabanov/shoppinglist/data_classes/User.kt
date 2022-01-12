package com.vshabanov.shoppinglist.data_classes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var _id: String = "",
    var email: String = "",
    var friends: HashMap<String,Friend> = hashMapOf<String,Friend>(),
    var list: HashMap<String,ShoppingList> = hashMapOf<String,ShoppingList>(),
    var name: String = "",
    var phone: String = ""
) {

    @Exclude
    fun toMap(): Map<String,Any?> {
        return mapOf(
            "_id" to _id,
            "email" to email,
            "friends" to friends,
            "list" to list,
            "name" to name,
            "phone" to phone
        )
    }
}
