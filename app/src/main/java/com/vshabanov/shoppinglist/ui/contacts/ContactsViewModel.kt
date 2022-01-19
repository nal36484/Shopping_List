package com.vshabanov.shoppinglist.ui.contacts


import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.Friend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext
    val list: MutableList<Friend> = arrayListOf()

    @SuppressLint("Recycle", "Range")
    fun readContacts() {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        list.clear()
        while (cursor?.moveToNext() == true) {
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val friend = Friend()
            friend.name = name
            friend.phone = phone.replace(Regex("[\\s,-]"),"")
            list.add(friend)
        }
        cursor?.close()
        CoroutineScope(Dispatchers.IO).launch {
            DataBaseHelper().updatePhones(list)
        }
    }

    private var _friends = MutableLiveData<MutableList<Friend>>()

    val data = DataBaseHelper().readFriends(object : DataBaseHelper.FriendStatus {
        override fun dataIsLoaded(friends: MutableList<Friend>) {
            _friends.value = friends
        }
    })

    val friends: LiveData<MutableList<Friend>> = _friends
}