package com.example.frontapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontapp.model.User
import com.example.frontapp.model.UserClient
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(onUserAdded: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("名前") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("メールアドレス") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isSubmitting = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        UserClient().createUser(User(name = name))
                        withContext(Dispatchers.Main) {
                            onUserAdded()
                            name = ""
                            email = ""
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        isSubmitting = false
                    }
                }
            },
            enabled = !isSubmitting
        ) {
            Text("登録する")
        }
    }
}