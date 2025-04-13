package com.example.frontapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.frontapp.model.User
import com.example.frontapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(viewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("名前") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.addUser(User(name = name))
                name = ""
            },
            enabled = !isSubmitting,
            shape = RectangleShape
        ) {
            Text("登録する")
        }
    }
}