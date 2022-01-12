package com.vshabanov.shoppinglist.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Friend
import com.vshabanov.shoppinglist.data_classes.Message

class NotificationsViewModel: ViewModel() {

    private val _messages = MutableLiveData<MutableList<Message>>()

    val dataBaseHelper = DataBaseHelper()
    var notifications: MutableList<Message> = arrayListOf()
    var _friends: MutableList<Friend> = arrayListOf()

    val data = dataBaseHelper.readFriends(object : DataBaseHelper.FriendStatus {
        override fun dataIsLoaded(friends: MutableList<Friend>) {
            _friends = friends
        }
    })

    val dataMessages = dataBaseHelper.readMessages(object : DataBaseHelper.MessageStatus {
        override fun dataIsLoaded(messages: MutableList<Message>) {
            notifications = messages
            for (message in notifications) {
                for (friend in _friends) {
                    if (message.from == friend._id) {
                        message.nameFrom = friend.name
                        message.phoneFrom = friend.phone
                    }
                }
            }
            _messages.value = notifications
        }
    })

    val messages: LiveData<MutableList<Message>> = _messages
}