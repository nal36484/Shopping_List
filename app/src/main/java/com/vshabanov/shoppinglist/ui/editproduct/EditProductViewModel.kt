package com.vshabanov.shoppinglist.ui.editproduct

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.ShoppingItem

class EditProductViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext
    var listKey = context.getSharedPreferences("listId", Context.MODE_PRIVATE).getString("listId","").toString()
    var itemKey = context.getSharedPreferences("listId", Context.MODE_PRIVATE).getString("itemId","").toString()

    val data = DataBaseHelper().readItems(listKey, object : DataBaseHelper.ItemStatus{
        override fun dataIsLoaded(shoppingItem: MutableList<ShoppingItem>) {
            for (item in shoppingItem)
                if (item._id == itemKey) {
                    _itemName.value = item.name
                    _itemAmount.value = item.amount
                    _itemPrice.value = item.price
                }
        }
    })

    private val _itemName = MutableLiveData<String>()
    private val _itemAmount = MutableLiveData<String>()
    private val _itemPrice = MutableLiveData<String>()

    val itemName: LiveData<String> = _itemName
    val itemAmount: LiveData<String> = _itemAmount
    val itemPrice: LiveData<String> = _itemPrice
}