package com.vshabanov.shoppinglist.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.ShoppingList

class HomeViewModel : ViewModel() {

    private var _shoppingList = MutableLiveData<MutableList<ShoppingList>>()

    val data = DataBaseHelper().readList(object : DataBaseHelper.ListStatus {
        override fun dataIsLoaded(shoppingList: MutableList<ShoppingList>) {
            _shoppingList.value = shoppingList
        }
    })

    val shoppingList: LiveData<MutableList<ShoppingList>> = _shoppingList
}