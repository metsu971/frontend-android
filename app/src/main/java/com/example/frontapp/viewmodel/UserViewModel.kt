package com.example.frontapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontapp.model.User
import com.example.frontapp.model.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users : StateFlow<List<User>> = _users

    private fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserClient().getUsers()
            _users.value = result
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            user.id?.let {
                UserClient().updateUser(it, user.copy(name = "更新後-名前"))
                fetchUsers()
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            UserClient().deleteUser(userId)
            fetchUsers()
        }
    }
}