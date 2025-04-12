package com.example.frontapp.repository

import com.example.frontapp.model.User
import com.example.frontapp.model.UserClient

class UserRepositoryImpl : UserRepository {
    private val client = UserClient()

    override suspend fun getUsers(): List<User> {
        return client.getUsers()
    }

    override suspend fun addUser(user: User) {
        return client.createUser(user)
    }

    override suspend fun updateUser(id: Long, user: User) {
        return client.updateUser(id, user)
    }

    override suspend fun deleteUser(id: Long) {
        return client.deleteUser(id)
    }
}