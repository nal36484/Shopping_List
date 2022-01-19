package com.vshabanov.shoppinglist.data_classes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friend(
    var _id: String = "",
    var email: String = "",
    var name: String = "",
    var phone: String = "",
    var photo: String = "empty"
) {

    @Exclude
    fun toMap(): Map<String,Any?> {
        return mapOf(
            "_id" to _id,
            "email" to email,
            "name" to name,
            "phone" to phone
        )
    }
}
