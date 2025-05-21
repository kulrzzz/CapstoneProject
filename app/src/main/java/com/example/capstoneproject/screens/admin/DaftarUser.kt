package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.Booking
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar

@Composable
fun DaftarUserPage(
    transaksiList: List<Booking>,
    onDetailClick: (Booking) -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FF))
    ) {
        SideBar(
            userRole = "root",
            onNavigate = onNavigate,
            onLogout = onLogout
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FF))
                .padding(24.dp)
        ) {
            Text(
                text = "Lihat Daftar User",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(transaksiList) { booking ->
                    DaftarUserCard(
                        customerName = booking.customer_id,
                        gedung = booking.room_id,
                        tanggal = booking.booking_date,
                        waktu = "${booking.booking_start} - ${booking.booking_end} WIB",
                        kodeBooking = booking.booking_code.toString(),
                        onDetailClick = { onDetailClick(booking) }
                    )
                }
            }
        }
    }
}

@Composable
fun DaftarUserCard(
    customerName: String,
    gedung: String,
    tanggal: String,
    waktu: String,
    kodeBooking: String,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFFF9800)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color(0xFF2196F3),
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Nama Peminjam          :     $customerName")
                Text("Nama Gedung              :     $gedung")
                Text("Tanggal Pinjam            :     $tanggal")
                Text("Waktu Pinjam               :     $waktu")
                Text("Kode Booking               :     $kodeBooking")
            }

            Button(
                onClick = onDetailClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Lihat Detail", color = Color(0xFF90A4AE))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun DaftarUserPagePreview() {
    val dummyList = listOf(
        Booking(
            booking_id = "B001",
            customer_id = "Jessica",
            room_id = "GKM Lantai 1",
            booking_code = 12356,
            booking_date = "08 Desember 2025",
            booking_start = "08.00",
            booking_end = "12.00",
            booking_desc = "Keperluan presentasi",
            booking_price = 50000,
            booking_status = 1,
            created_at = null,
            updated_at = null
        ),
        Booking(
            booking_id = "B002",
            customer_id = "Fajar",
            room_id = "GKB 2 Ruang 203",
            booking_code = 78901,
            booking_date = "09 Desember 2025",
            booking_start = "13.00",
            booking_end = "15.00",
            booking_desc = "Meeting divisi",
            booking_price = 65000,
            booking_status = 1,
            created_at = null,
            updated_at = null
        )
    )

    DaftarUserPage(transaksiList = dummyList)
}
