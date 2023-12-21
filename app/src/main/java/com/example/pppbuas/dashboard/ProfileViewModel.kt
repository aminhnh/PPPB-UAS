package com.example.pppbuas.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pppbuas.user.AppUser
import com.example.pppbuas.user.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {

    private val _userData = MutableLiveData<AppUser?>()
    val userData: MutableLiveData<AppUser?> get() = _userData


    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            val user = UserManager.getUserData(userId)

            // Update the LiveData with the fetched user data
            _userData.postValue(user)
        }
    }
}
