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

// Dummy data class (jika belum global, bisa dipindah ke file terpisah)
data class Transaksi(val id: String, val user: String, val tanggal: String, val jumlah: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatTransaksiPage(onBack: () -> Unit) {
    val riwayat = remember {
        listOf(
            Transaksi("TRX001", "user1", "2024-04-10", 50000),
            Transaksi("TRX002", "user2", "2024-04-11", 75000),
            Transaksi("TRX003", "user1", "2024-04-12", 100000)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Riwayat Transaksi") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(riwayat) { trx ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${trx.id}", style = MaterialTheme.typography.bodyLarge)
                            Text("User: ${trx.user}")
                            Text("Tanggal: ${trx.tanggal}")
                            Text("Jumlah: Rp ${trx.jumlah}")
                        }
                    }
                }
            }
        }
    }
}