package com.example.frontapp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class UserClient {
    private val client = OkHttpClient()
    private val gson = Gson()


    fun getUsers(): List<User> {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/users")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val body = response.body?.string() ?: throw Exception("Empty response")
            val userType = object : TypeToken<List<User>>() {}.type
            return gson.fromJson(body, userType)
        }
    }

    fun updateUser(id: Long, user: User) {
        val json = gson.toJson(user)
        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/users/$id")
            .put(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Unexpected code $response")
    }

    fun deleteUser(id: Long) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/users/$id")
            .delete()
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Unexpected code $response")
    }

    fun createUser(user: User) {
        val json = gson.toJson(user)
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/users") // POST先URL
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Unexpected code $response")
    }

}