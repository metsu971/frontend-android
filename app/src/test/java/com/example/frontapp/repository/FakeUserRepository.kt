package com.example.frontapp.repository

import com.example.frontapp.model.User

class FakeUserRepository : UserRepository {
    val users = mutableListOf<User>()

    override suspend fun getUsers(): List<User> {
        return users.toList()
    }

    override suspend fun addUser(user: User) {
        users.add(user)
    }

    override suspend fun updateUser(id: Long, user: User) {
        val index = users.indexOfFirst { it.id == id }
        if (index != -1) {
            users[index] = users[index].copy(name = user.name)
        }
    }

    override suspend fun deleteUser(id: Long) {
        users.removeIf { it.id == id }
    }

}