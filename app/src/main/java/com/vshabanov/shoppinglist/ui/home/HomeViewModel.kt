package com.vshabanov.shoppinglist.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.Data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.Data_classes.ShoppingList

class HomeViewModel : ViewModel() {

   // private val _text = MutableLiveData<String>().apply {
        //value = "This is home Fragment"
   // }
   // val text: LiveData<String> = _text
    private var _shoppingList = MutableLiveData<MutableList<ShoppingList>>()

    val data = DataBaseHelper().readList(object : DataBaseHelper.DataStatus {
        override fun dataIsLoaded(shoppingList: MutableList<ShoppingList>) {
            _shoppingList.value = shoppingList
        }
    })

    val shoppingList: LiveData<out MutableList<ShoppingList>> = _shoppingList

}