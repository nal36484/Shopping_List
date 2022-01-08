package com.vshabanov.shoppinglist.ui.sendlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vshabanov.shoppinglist.data_classes.Friend

class SendListViewModel(application: Application): AndroidViewModel(application) {

    val _friends = MutableLiveData<MutableList<Friend>>().apply {
        value = arrayListOf()
    }

    val friends: LiveData<MutableList<Friend>> = _friends
}