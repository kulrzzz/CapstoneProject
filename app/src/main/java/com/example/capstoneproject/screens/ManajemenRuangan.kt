package com.example.capstoneproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R

@Composable
fun ManajemenRuangan(
    onTambahRuanganClick: () -> Unit
) {
    // Styling variables
    val titleColor = Color(0xFF009DFF)
    val buttonColor = Color(0xFF007BFF)
    val spacingLarge = 24.dp
    val spacingMedium = 16.dp
    val textSizeTitle = 22.sp
    val textSizeNormal = 10.sp

    // Variabel yang kamu atur manual
    val columnWidthNo = 25.dp   // Lebar kolom No
    val verticalSpacing = 0.dp  // Jarak antar baris
    val iconSpacing = 4.dp      // Jarak antar ikon

    Column(
        modifier = Modifier
            .width(762.dp)
            .height(768.dp)
            .padding(spacingMedium)
    ) {
        Text(
            text = "Manajemen Ruangan",
            fontSize = textSizeTitle,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(spacingLarge))

        Button(
            onClick = onTambahRuanganClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.White
            )
        ) {
            Text(text = "Tambah Ruangan", fontSize = textSizeNormal)
        }

        Spacer(modifier = Modifier.height(spacingMedium))

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F7FF))
                        .padding(vertical = spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableHeaderCell("No", columnWidthNo, textSizeNormal)
                    TableHeaderCell("Nama Ruangan", 90.dp, textSizeNormal)
                    TableHeaderCell("Kategori Ruangan", 110.dp, textSizeNormal)
                    TableHeaderCell("Kapasitas", 75.dp, textSizeNormal)
                    TableHeaderCell("Fasilitas", 75.dp, textSizeNormal)
                    TableHeaderCell("Harga Sewa", 87.dp, textSizeNormal)
                    TableHeaderCell("Ukuran", 90.dp, textSizeNormal)
                    TableHeaderCell("Foto", 95.dp, textSizeNormal)
                    TableHeaderCell("Actions", 80.dp, textSizeNormal)
                    Spacer(modifier = Modifier.weight(1f))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(dummyRooms) { room ->
                        RoomItem(
                            room = room,
                            textSize = textSizeNormal,
                            verticalSpacing = verticalSpacing,
                            columnWidthNo = columnWidthNo,
                            iconSpacing = iconSpacing
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TableHeaderCell(text: String, width: Dp, textSize: TextUnit) {
    Text(
        text = text,
        fontSize = textSize,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 4.dp)
    )
}

@Composable
fun RoomItem(
    room: Room,
    textSize: TextUnit,
    verticalSpacing: Dp,
    columnWidthNo: Dp,
    iconSpacing: Dp
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp), // padding horizontal tetap rapi
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = room.no.toString(), fontSize = textSize, modifier = Modifier.width(columnWidthNo))
            Text(text = room.nama, fontSize = textSize, modifier = Modifier.width(80.dp))
            Text(text = room.kategori, fontSize = textSize, modifier = Modifier.width(103.dp))
            Text(text = room.kapasitas, fontSize = textSize, modifier = Modifier.width(75.dp))
            Text(text = room.fasilitas, fontSize = textSize, modifier = Modifier.width(80.dp))
            Text(text = room.harga, fontSize = textSize, modifier = Modifier.width(80.dp))
            Text(text = room.ukuran, fontSize = textSize, modifier = Modifier.width(80.dp))
            Text(
                text = "Lihat Disini",
                color = Color(0xFF007BFF),
                fontSize = textSize,
                modifier = Modifier.width(80.dp)
            )
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(iconSpacing)
            ) {
                IconButton(onClick = { /* Edit */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit",
                        tint = Color(0xFF007BFF)
                    )
                }
                IconButton(onClick = { /* Delete */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash),
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }

        // Spacer antar baris
        Spacer(modifier = Modifier.height(verticalSpacing))
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

@Preview(showBackground = true, widthDp = 762, heightDp = 768)
@Composable
fun ManajemenRuanganPreview() {
    ManajemenRuangan(
        onTambahRuanganClick = { /* Do nothing in preview */ }
    )
}
