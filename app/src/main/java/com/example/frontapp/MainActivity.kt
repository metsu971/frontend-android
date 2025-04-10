package com.example.frontapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.frontapp.ui.theme.FrontAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import com.example.frontapp.ui.theme.UserFormScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    println("test0")
                    Greeting("Android")
                    UserListScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontAppTheme {
        Greeting("Android")
    }
}

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