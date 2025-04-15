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
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaksiHarianPage(onBack: () -> Unit) {
    val tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

    val transaksiHariIni = remember {
        listOf(
            Transaksi("TRX201", "user1", tanggalHariIni, 20000),
            Transaksi("TRX202", "user3", tanggalHariIni, 40000)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Transaksi Harian") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(transaksiHariIni) { trx ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${trx.id}", style = MaterialTheme.typography.bodyLarge)
                            Text("User: ${trx.user}")
                            Text("Jumlah: Rp ${trx.jumlah}")
                            Text("Tanggal: ${trx.tanggal}", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}