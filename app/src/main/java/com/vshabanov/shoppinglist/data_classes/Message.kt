package com.vshabanov.shoppinglist.data_classes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var _id: String = "",
    var from: String = "",
    var listId: String = "",
    var nameFrom: String = "",
    var phoneFrom: String = ""
) {

    @Exclude
    fun toMap(): Map<String,Any?> {
        return mapOf(
            "_id" to _id,
            "from" to from,
            "listId" to listId
        )
    }
}