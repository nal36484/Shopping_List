package com.vshabanov.shoppinglist.ui.itemshow

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.ShoppingItem

class ListNameViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext
    var listId = context.getSharedPreferences("listId", Context.MODE_PRIVATE).getString("listId","").toString()

    private var _itemsList = MutableLiveData<MutableList<ShoppingItem>>()

    val dataBaseHelper = DataBaseHelper()
    val data = dataBaseHelper.readItems(listId, object : DataBaseHelper.ItemStatus{
        override fun dataIsLoaded(shoppingItem: MutableList<ShoppingItem>) {
            _itemsList.value = shoppingItem
        }
    })

    val itemsList: LiveData<MutableList<ShoppingItem>> = _itemsList
}