package com.vshabanov.shoppinglist.ui.slideshow

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

@SuppressLint("StaticFieldLeak")
class SlideshowViewModel(application: Application) : AndroidViewModel(application) {

    val context = application.applicationContext
    var settings = context.getSharedPreferences("listId", MODE_PRIVATE).getString("listId","")
    private val _text = MutableLiveData<String>().apply {
        value = settings
    }

    val text: LiveData<String> = _text
}