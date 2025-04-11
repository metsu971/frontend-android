package com.example.frontapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontapp.model.User
import com.example.frontapp.model.UserClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserListScreen() {
    val users = remember { mutableStateOf<List<User>>(emptyList()) }

    Column {
        UserFormScreen(onUserAdded = {
            CoroutineScope(Dispatchers.IO).launch {
                val updated = UserClient().getUsers()
                withContext(Dispatchers.Main) {
                    users.value = updated
                }
            }
        })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // ボタン押下時にAPIを呼び出し
            println("test1")
            CoroutineScope(Dispatchers.IO).launch {
                val result = UserClient().getUsers()
                println("test!")
                println(result)
                withContext(Dispatchers.Main) {
                    users.value = result
                }
            }
        }) {
            Text("ユーザー一覧を取得")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users.value) { user ->
                Column {
                    Text(text = "名前: ${user.name}")
                    Row {
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                user.id?.let {
                                    UserClient().updateUser(it, user.copy(name = "更新後-名前"))
                                    val updated = UserClient().getUsers()
                                    withContext(Dispatchers.Main) {
                                        users.value = updated
                                    }
                                }
                            }
                        }) {
                            Text("更新")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                user.id?.let {
                                    UserClient().deleteUser(it)
                                    val updated = UserClient().getUsers()
                                    withContext(Dispatchers.Main) {
                                        users.value = updated
                                    }
                                }
                            }
                        }) {
                            Text("削除")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}