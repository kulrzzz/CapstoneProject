package com.example.capstoneproject.screens.admin

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun RiwayatTransaksiPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleColor = Color(0xFF009DFF)
    val headerBg = Color(0xFFF5F7FF)
    val textSize = 12.sp

    Column(
        modifier = modifier
            .width(762.dp)
            .height(768.dp)
            .padding(16.dp)
    ) {
        // Tombol kembali (opsional)
        Button(onClick = onBack) {
            Text("← Kembali")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lihat Riwayat Transaksi",
            fontSize = 22.sp,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TransaksiHeaderRow(headerBg, textSize)
                LazyColumn {
                    items(dummyTransactions) { transaksi ->
                        TransaksiRow(transaksi, textSize)
                    }
                }
            }
        }
    }
}

@Composable
fun TransaksiHeaderRow(bgColor: Color, fontSize: TextUnit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableHeaderCell("Kode Booking", 100.dp, fontSize)
        TableHeaderCell("Nama Peminjam", 100.dp, fontSize)
        TableHeaderCell("Nama Gedung", 120.dp, fontSize)
        TableHeaderCell("Nominal Pembayaran", 130.dp, fontSize)
        TableHeaderCell("Status Pembayaran", 120.dp, fontSize)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TransaksiRow(transaksi: Transaksi, fontSize: TextUnit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = transaksi.kodeBooking,
            color = Color(0xFF007BFF),
            fontSize = fontSize,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.width(100.dp)
        )
        Text(transaksi.namaPeminjam, fontSize = fontSize, modifier = Modifier.width(100.dp))
        Text(transaksi.namaGedung, fontSize = fontSize, modifier = Modifier.width(120.dp))
        Text(transaksi.nominal, fontSize = fontSize, modifier = Modifier.width(130.dp))
        StatusBadge(transaksi.status, fontSize, Modifier.width(120.dp))
    }
}

@Composable
fun StatusBadge(status: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    val isLunas = status.equals("lunas", ignoreCase = true)
    val bgColor = if (isLunas) Color(0xFFE3FCEF) else Color(0xFFFFE6E6)
    val textColor = if (isLunas) Color(0xFF2ECC71) else Color(0xFFEA4335)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(bgColor, shape = RoundedCornerShape(16.dp))
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

// Dummy model & data
data class Transaksi(
    val kodeBooking: String,
    val namaPeminjam: String,
    val namaGedung: String,
    val nominal: String,
    val status: String
)

val dummyTransactions = listOf(
    Transaksi("12358G", "Jesicca", "GKM Lantai 2", "Rp 50.000", "Belum lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 1", "Rp 70.000", "Lunas"),
    Transaksi("12358G", "Jesicca", "GKM Lantai 3", "Rp 50.000", "Belum lunas")
)

@Preview(showBackground = true, widthDp = 762, heightDp = 768)
@Composable
fun RiwayatTransaksiPreview() {
    RiwayatTransaksiPage(onBack = {})
}