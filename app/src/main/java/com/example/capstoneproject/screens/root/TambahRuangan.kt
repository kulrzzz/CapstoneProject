package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TambahRuangan() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        Text(
            "Tambah Ruangan",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF0284C7)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nama Ruangan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Kategori Ruangan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Kapasitas") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Fasilitas") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Harga Sewa") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Ukuran Ruangan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Deskripsi Ruangan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Text("Upload Foto Ruangan", style = MaterialTheme.typography.bodyMedium)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📁 Pilih File", color = Color.Gray)
            }

            Button(
                onClick = { /* TODO: Action */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("➕ Tambahkan")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun TambahRuanganPreview() {
    TambahRuangan()
}
