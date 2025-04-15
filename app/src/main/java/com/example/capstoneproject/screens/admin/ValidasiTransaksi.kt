package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidasiTransaksiPage(onBack: () -> Unit = {}) {
    var kodeTransaksi by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Validasi Transaksi") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = kodeTransaksi,
            onValueChange = { kodeTransaksi = it },
            label = { Text("Masukkan Kode Transaksi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Dummy validasi sementara
            isValid = kodeTransaksi.startsWith("TRX")
        }) {
            Text("Validasi")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (isValid) {
            true -> Text("✅ Transaksi valid", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
            false -> Text("❌ Transaksi tidak ditemukan atau tidak valid", color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
            null -> {}
        }
    }
}