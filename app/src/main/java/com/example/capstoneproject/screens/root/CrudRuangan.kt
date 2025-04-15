package com.example.capstoneproject.screens.root

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Data class ruangan
data class Ruangan(val nama: String, val kapasitas: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudRuanganPage(onBack: () -> Unit) {
    val daftarRuangan = remember {
        mutableStateListOf(
            Ruangan("Ruang Meeting A", 10),
            Ruangan("Ruang Workshop B", 25)
        )
    }

    var nama by remember { mutableStateOf("") }
    var kapasitasText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Manajemen Ruangan") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Ruangan") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = kapasitasText,
                onValueChange = { kapasitasText = it },
                label = { Text("Kapasitas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val kapasitas = kapasitasText.toIntOrNull()
                if (nama.isNotBlank() && kapasitas != null) {
                    daftarRuangan.add(Ruangan(nama, kapasitas))
                    nama = ""
                    kapasitasText = ""
                }
            }) {
                Text("Tambah Ruangan")
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(daftarRuangan) { ruangan ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nama: ${ruangan.nama}")
                            Text("Kapasitas: ${ruangan.kapasitas} orang")
                        }
                    }
                }
            }
        }
    }
}