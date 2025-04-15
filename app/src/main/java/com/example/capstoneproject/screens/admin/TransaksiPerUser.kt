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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaksiPerUserPage(onBack: () -> Unit) {
    val semuaUser = listOf("user1", "user2", "user3")
    var selectedUser by remember { mutableStateOf(semuaUser.first()) }

    val transaksiUser = remember(selectedUser) {
        listOf(
            Transaksi("TRX101", selectedUser, "2024-04-10", 30000),
            Transaksi("TRX102", selectedUser, "2024-04-11", 45000)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Transaksi Per User") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Dropdown user
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedUser,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih User") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    semuaUser.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user) },
                            onClick = {
                                selectedUser = user
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(transaksiUser) { trx ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${trx.id}", style = MaterialTheme.typography.bodyLarge)
                            Text("Tanggal: ${trx.tanggal}")
                            Text("Jumlah: Rp ${trx.jumlah}")
                        }
                    }
                }
            }
        }
    }
}
