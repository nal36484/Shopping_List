package com.vshabanov.shoppinglist.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.Data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.Data_classes.ShoppingList

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}