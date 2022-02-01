package com.vshabanov.shoppinglist.ui.sendlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Friend
import com.vshabanov.shoppinglist.data_classes.ShoppingList

class SendListViewModel(application: Application): AndroidViewModel(application) {

    private val _friends = MutableLiveData<MutableList<Friend>>()

    val dataBaseHelper = DataBaseHelper()
    val data = dataBaseHelper.readFriends(object : DataBaseHelper.FriendStatus {
        override fun dataIsLoaded(friends: MutableList<Friend>) {
            _friends.value = friends
        }
    })

    val friends: LiveData<MutableList<Friend>> = _friends
}