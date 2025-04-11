package com.example.frontapp.repository

import com.example.frontapp.model.User

interface UserRepository {
    suspend fun getUsers() : List<User>
    suspend fun addUser(user: User)
    suspend fun updateUser(id: Long, user: User)
    suspend fun deleteUser(id: Long)
}