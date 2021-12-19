package com.vshabanov.shoppinglist.Data_classes

data class ShoppingItem(
    var name: String,
    var amount: Int = 1,
    var price: Int = 0,
    //var ShopName: String?
) {}
