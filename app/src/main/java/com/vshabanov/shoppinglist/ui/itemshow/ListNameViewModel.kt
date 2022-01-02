package com.vshabanov.shoppinglist.ui.itemshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.ShoppingItem


class ListNameViewModel: ViewModel() {

    private var _itemsList = MutableLiveData<MutableList<ShoppingItem>>()

    val itemsList: LiveData<MutableList<ShoppingItem>> = _itemsList
}