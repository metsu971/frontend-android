package com.example.frontapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.frontapp.ui.theme.MaterialSoftGreen
import com.example.frontapp.ui.theme.MaterialSoftRed
import com.example.frontapp.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(viewModel: UserViewModel = remember { UserViewModel() }) {
    val users = viewModel.users.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        UserFormScreen(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                viewModel.fetchUsers()
            }) {
            Text("ユーザ一覧を取得")
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(users.value) { user ->
                Column {
                    Text(text = "名前: ${user.name}")
                    Row {
                        Button(
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialSoftGreen,
                                contentColor = Color.White
                            ), onClick = {
                                viewModel.updateUser(user.copy(name = "更新後-${user.name}"))
                            }) {
                            Text("更新")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialSoftRed,
                                contentColor = Color.White
                            ), onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    user.id?.let {
                                        viewModel.deleteUser(it)
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