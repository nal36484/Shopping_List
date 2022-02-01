package com.vshabanov.shoppinglist.login_fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel: ViewModel() {
    private var _registration = MutableLiveData<Boolean>(false)

    fun changeData() {
        if (_registration.value == false)
             _registration.value = true
    }

    val registration: LiveData<Boolean> = _registration
}