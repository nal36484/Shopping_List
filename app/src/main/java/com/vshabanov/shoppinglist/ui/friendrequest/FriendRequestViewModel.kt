package com.vshabanov.shoppinglist.ui.friendrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Friend

class FriendRequestViewModel: ViewModel() {

    val _friendRequests = MutableLiveData<MutableList<Friend>>()

    val dataBaseHelper = DataBaseHelper()
    val data = dataBaseHelper.getFriendRequests(object : DataBaseHelper.FriendRequests {
        override fun dataIsLoaded(requests: MutableList<Friend>) {
            _friendRequests.value = requests
        }
    })
    val friendRequests: LiveData<MutableList<Friend>> = _friendRequests
}