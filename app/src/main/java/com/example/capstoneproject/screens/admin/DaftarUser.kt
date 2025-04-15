package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Dummy user data class
data class User(val username: String, val email: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarUserPage(onBack: () -> Unit) {
    val daftarUser = remember {
        listOf(
            User("user1", "user1@email.com"),
            User("user2", "user2@email.com"),
            User("user3", "user3@email.com")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Daftar User") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(daftarUser) { user ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Username: ${user.username}", style = MaterialTheme.typography.bodyLarge)
                            Text("Email: ${user.email}")
                        }
                    }
                }
            }
        }
    }
}