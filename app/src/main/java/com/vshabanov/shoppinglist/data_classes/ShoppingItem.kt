package com.vshabanov.shoppinglist.data_classes

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingItem(
    var _id: String = "",
    var name: String = "",
    var amount: String = "1",
    var price: String = "0",
    //var shopName: String?
) {

    @Exclude
    fun toMap(): Map<String,Any?> {
        return mapOf(
            "_id" to _id,
            "name" to name,
            "amount" to amount,
            "price" to price
        )
    }
}
