package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.capstoneproject.R
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ManajemenRuangan(
    onTambahRuanganClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Manajemen Ruangan",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF009DFF) // Warna biru muda
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTambahRuanganClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007BFF),
                contentColor = Color.White
            )
        ) {
            Text(text = "Tambah Ruangan")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(dummyRooms) { room ->
                    RoomItem(room)
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = room.no.toString(), modifier = Modifier.width(30.dp))
        Text(text = room.nama, modifier = Modifier.width(100.dp))
        Text(text = room.kategori, modifier = Modifier.width(120.dp))
        Text(text = room.kapasitas, modifier = Modifier.width(80.dp))
        Text(text = room.fasilitas, modifier = Modifier.width(80.dp))
        Text(text = room.harga, modifier = Modifier.width(100.dp))
        Text(text = room.ukuran, modifier = Modifier.width(80.dp))
        Text(
            text = "Lihat Disini",
            color = Color(0xFF007BFF),
            modifier = Modifier.width(80.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { /* Edit */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.adminafter),
                    contentDescription = "Edit",
                    tint = Color(0xFF007BFF)
                )
            }
            IconButton(onClick = { /* Delete */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.homeafter),
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

// Dummy data
data class Room(
    val no: Int,
    val nama: String,
    val kategori: String,
    val kapasitas: String,
    val fasilitas: String,
    val harga: String,
    val ukuran: String
)

val dummyRooms = List(15) {
    Room(
        no = it + 1,
        nama = "Jesica",
        kategori = "GKM Lantai 2",
        kapasitas = "50 Orang",
        fasilitas = "10 Kursi",
        harga = "Rp 50.000",
        ukuran = "4m x 2m"
    )
}


@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun ManajemenRuanganPreview() {
    ManajemenRuangan(
        onTambahRuanganClick = { /* Do nothing in preview */ }
    )
}
