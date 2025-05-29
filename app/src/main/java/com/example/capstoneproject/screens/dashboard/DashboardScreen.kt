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
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.capstoneproject.R


@Composable
fun DashboardScreen(
    userRole: String?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    if (userRole == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FF))
    ) {
        SideBar(userRole = userRole, onNavigate = onNavigate, onLogout = onLogout)

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Main Dashboard",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    title = "Total Peminjam",
                    value = "45",
                    iconRes = R.drawable.user
                )
                MetricCard(
                    title = "Total Pembayaran",
                    value = "Rp. 200.000",
                    iconRes = R.drawable.total
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Riwayat Peminjaman",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            BookingHistoryTable(sampleRiwayat)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, iconRes: Int) {
    Card(
        modifier = Modifier
//            .weight(1f)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(text = title, fontSize = 14.sp, color = Color.Gray)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BookingHistoryTable(data: List<RiwayatData>) {
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0))
                    .padding(vertical = 12.dp, horizontal = 15.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                TableHeader("No", 60.dp)
                TableHeader("Nama", 200.dp)
                TableHeader("Ruangan", 200.dp)
                TableHeader("Agenda", 180.dp)
                TableHeader("Jam", 180.dp)
                TableHeader("Kode", 120.dp)
            }

            LazyColumn {
                itemsIndexed(data) { index, item ->
                    BookingRow(index + 1, item)
                }
            }
        }
    }
}

@Composable
fun TableHeader(text: String, width: Dp) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1A237E),
        modifier = Modifier.width(width),
        textAlign = TextAlign.Start
    )
}

@Composable
fun BookingRow(index: Int, data: RiwayatData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (index % 2 == 0) Color(0xFFF8FAFF) else Color.White)
            .padding(horizontal = 15.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(index.toString(), 60.dp)
        TableCell(data.nama, 200.dp)
        TableCell(data.ruangan, 200.dp)
        TableCell(data.agenda, 180.dp)
        TableCell(data.jam, 180.dp)
        TableCell(data.kode, 120.dp)
    }
}

@Composable
fun TableCell(text: String, width: Dp) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier.width(width),
        textAlign = TextAlign.Start
    )
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
