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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun RiwayatTransaksiScreen() {
    val titleColor = Color(0xFF009DFF)
    val headerBg = Color(0xFFF5F7FF)
    val textSizeTitle = 22.sp
    val textSizeNormal = 12.sp
    val spacingLarge = 24.dp
    val spacingMedium = 16.dp

    Column(
        modifier = Modifier
            .width(762.dp)
            .height(768.dp)
            .padding(spacingMedium)
    ) {
        Text(
            text = "Lihat Riwayat Transaksi",
            fontSize = textSizeTitle,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(spacingLarge))

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
                        .background(headerBg)
                        .padding(vertical = spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableHeaderCell("Kode Booking", 100.dp, textSizeNormal)
                    TableHeaderCell("Nama Peminjam", 100.dp, textSizeNormal)
                    TableHeaderCell("Nama Gedung", 120.dp, textSizeNormal)
                    TableHeaderCell("Nominal Pembayaran", 130.dp, textSizeNormal)
                    TableHeaderCell("Status Pembayaran", 120.dp, textSizeNormal)
                    Spacer(modifier = Modifier.weight(1f))
                }

                LazyColumn {
                    items(dummyTransactions) { transaksi ->
                        TransactionRow(transaksi, textSizeNormal)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionRow(
    transaksi: Transaksi,
    textSize: TextUnit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Kode Booking (underline, biru)
        Text(
            text = transaksi.kodeBooking,
            color = Color(0xFF007BFF),
            fontSize = textSize,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = transaksi.namaPeminjam,
            fontSize = textSize,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = transaksi.namaGedung,
            fontSize = textSize,
            modifier = Modifier.width(120.dp)
        )

        Text(
            text = transaksi.nominal,
            fontSize = textSize,
            modifier = Modifier.width(130.dp)
        )

        StatusBadge(
            status = transaksi.status,
            fontSize = textSize,
            modifier = Modifier.width(120.dp)
        )
    }
}

@Composable
fun StatusBadge(status: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    val isLunas = status.lowercase() == "lunas"
    val backgroundColor = if (isLunas) Color(0xFFE3FCEF) else Color(0xFFFFE6E6)
    val textColor = if (isLunas) Color(0xFF2ECC71) else Color(0xFFEA4335)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}

// Dummy Data Class
data class Transaksi(
    val kodeBooking: String,
    val namaPeminjam: String,
    val namaGedung: String,
    val nominal: String,
    val status: String
)

// Dummy Data List
val dummyTransactions = listOf(
    Transaksi("12358G", "Jesicca", "GKM Lantai 2", "Rp 50.000", "Belum lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 3", "Rp 50.000", "Belum lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 3", "Rp 50.000", "Belum lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 3", "Rp 50.000", "Belum lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
)

@Preview(showBackground = true, widthDp = 762, heightDp = 768)
@Composable
fun RiwayatTransaksiScreenPreview() {
    RiwayatTransaksiScreen()
}