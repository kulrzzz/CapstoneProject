package com.example.capstoneproject.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar

@Composable
fun DashboardScreen(
    userRole: String?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {

        // ✅ Sidebar dinamis untuk admin dan root
        SideBar(
            userRole = userRole,
            onNavigate = onNavigate,
            onLogout = onLogout
        )

        // ✅ Konten dashboard utama
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp),
            color = Color(0xFFF9FBFF)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Main Dashboard",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricCard(title = "Total Peminjam", value = "45")
                    MetricCard(title = "Total Pembayaran", value = "Rp. 200,000")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Riwayat Peminjaman",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(sampleRiwayat) { index, item ->
                        RiwayatItemRow(index + 1, item)
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(text = title, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RiwayatItemRow(index: Int, data: RiwayatData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$index", Modifier.weight(0.2f))
        Text(data.nama, Modifier.weight(1f))
        Text(data.ruangan, Modifier.weight(1f))
        Text(data.agenda, Modifier.weight(1f))
        Text(data.jam, Modifier.weight(1f))
        Text(data.kode, Modifier.weight(1f))
    }
}

data class RiwayatData(
    val nama: String,
    val ruangan: String,
    val agenda: String,
    val jam: String,
    val kode: String
)

val sampleRiwayat = listOf(
    RiwayatData("Mithilesh Kumar Singh", "GKM Lantai 1", "Kunjungan Studi", "07.00 - 13.00", "12358G"),
    RiwayatData("Suron Maharjan", "GKM Lantai 1", "Gladi Bersih", "08.00 - 15.50", "86523B"),
    RiwayatData("Sandesh Bajracharya", "GKM Lantai 2", "Kunjungan Studi", "16.00 - 20.00", "78365D"),
    RiwayatData("Subin Sedhai", "GKM Lantai 4.4", "Gladi Bersih", "12.00 - 20.00", "863265F"),
    RiwayatData("Vonjola Joshi", "GKM Lantai 3.2", "Upgrading 2", "12.00 - 20.00", "459872E")
)