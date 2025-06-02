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
            .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))

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
            .height(90.dp),
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
                Text(text = title, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
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
                TableHeader("Kode", 180.dp)
                TableHeader("Peminjam", 190.dp)
                TableHeader("Ruangan", 160.dp)
                TableHeader("Tanggal", 150.dp)
                TableHeader("Waktu", 170.dp)
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
        textAlign = TextAlign.Center
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
        TableCell(data.nama, 180.dp)
        TableCell(data.ruangan, 190.dp)
        TableCell(data.agenda, 160.dp)
        TableCell(data.jam, 150.dp)
        TableCell(data.kode, 170.dp)
    }
}

@Composable
fun TableCell(text: String, width: Dp) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier.width(width),
        textAlign = TextAlign.Center
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
    RiwayatData("6327517145", "RG", "Jepang", "2025-06-05", "07:00:00 - 10:00:00"),
    RiwayatData("6327517145", "RG", "Lavender", "2025-06-05", "08:00:00 - 16:00:00"),
    RiwayatData("3325287042", "regga ananda", "Lavender", "2025-06-05", "14:00:00 - 16:00:00"),
    RiwayatData("8900950976", "aqiljaved", "Indonesia", "2025-06-07", "07:00:00 - 09:00:00"),
)
