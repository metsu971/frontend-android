package com.example.frontapp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

class UserClient {
    private val client = OkHttpClient()
    private val gson = Gson()


    fun getUsers(): List<User> {
        val request = Request.Builder()
            .url("/users")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val body = response.body?.string() ?: throw Exception("Empty response")
            val userType = object : TypeToken<List<User>>() {}.type
            return gson.fromJson(body, userType)
        }
    }
}