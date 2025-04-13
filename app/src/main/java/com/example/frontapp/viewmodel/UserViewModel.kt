package com.example.frontapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontapp.model.User
import com.example.frontapp.repository.UserRepository
import com.example.frontapp.repository.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepositoryImpl(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    fun fetchUsers() {
        viewModelScope.launch(dispatcher) {
            val result = repository.getUsers()
            _users.value = result
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch(dispatcher) {
            _isSubmitting.value = true
            repository.addUser(user)
            fetchUsers()
            _isSubmitting.value = false
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(dispatcher) {
            user.id?.let {
                repository.updateUser(it, user)
                fetchUsers()
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch(dispatcher) {
            repository.deleteUser(userId)
            fetchUsers()
        }
    }
}