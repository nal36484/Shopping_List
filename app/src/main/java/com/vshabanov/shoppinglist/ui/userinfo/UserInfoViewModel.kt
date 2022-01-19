package com.vshabanov.shoppinglist.ui.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.User

class UserInfoViewModel: ViewModel() {
    private val _user = MutableLiveData<User>()

    val data = DataBaseHelper().getCurrentUser(object : DataBaseHelper.CurrentUserData {
        override fun dataIsLoaded(userData: User) {
            _user.value = userData
        }
    })

    val user: LiveData<User> = _user
}