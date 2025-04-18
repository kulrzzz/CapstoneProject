package com.example.capstoneproject.screens.root

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capstoneproject.screens.admin.User
import com.example.capstoneproject.ui.theme.CapstoneProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudAdminPage(onBack: () -> Unit) {
    val adminList = remember {
        mutableStateListOf(
            User("admin1", "admin1@email.com"),
            User("admin2", "admin2@email.com")
        )
    }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Manajemen Admin") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (username.isNotBlank() && email.isNotBlank()) {
                    adminList.add(User(username, email))
                    username = ""
                    email = ""
                }
            }) {
                Text("Tambah Admin")
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(adminList) { admin ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Username: ${admin.username}")
                            Text("Email: ${admin.email}")
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true ,
    device = "spec:width=1024dp,height=768dp,dpi=240"
)
@Composable
fun CrudAdminPagePreview() {
    CapstoneProjectTheme {
        CrudAdminPage(onBack = {})
    }
}

