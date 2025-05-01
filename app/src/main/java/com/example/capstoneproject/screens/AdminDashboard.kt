package com.example.capstoneproject.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.navigation.Screen

@Composable
fun AdminDashboard(
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val selectedMenu = remember { mutableStateOf<Screen>(Screen.AdminDashboard) }
    val riwayatExpanded = remember { mutableStateOf(true) }

    val menuItems = listOf(
        Triple("Dashboard", Screen.AdminDashboard, Icons.Default.Home),
        Triple("Lihat Riwayat Transaksi", Screen.RiwayatTransaksi, Icons.Default.BarChart),
        Triple("Lihat Daftar User", Screen.DaftarUser, Icons.Default.Badge)
    )

    Row(modifier = Modifier.fillMaxSize()) {

        // Sidebar
        Column(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .background(Color(0xFFF8FAFC))
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ✅ LOGO DI POSISI TENGAH
                Image(
                    painter = painterResource(id = R.drawable.reservin3),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(56.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 28.dp)
                )

                menuItems.forEach { (label, screen, icon) ->
                    val isSelected = selectedMenu.value::class == screen::class

                    if ((label == "Transaksi Per User" || label == "Transaksi Harian") && !riwayatExpanded.value) return@forEach

                    if (label == "Lihat Riwayat Transaksi") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { riwayatExpanded.value = !riwayatExpanded.value }
                                .padding(10.dp)
                                .background(
                                    if (isSelected) Color(0xFFE0F2FE) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color(0xFF0072FF) else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                color = if (isSelected) Color(0xFF0072FF) else Color.Black,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = if (riwayatExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand",
                                tint = Color.Gray
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedMenu.value = screen
                                    onNavigate(screen)
                                }
                                .padding(10.dp)
                                .background(
                                    if (isSelected) Color(0xFFE0F2FE) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color(0xFF0072FF) else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                color = if (isSelected) Color(0xFF0072FF) else Color.Black,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                // ✅ LOGOUT BUTTON GRADIENT & LEBIH KE ATAS
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onLogout,
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFFFF8008), Color(0xFFFF4D00))
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Main Content Tetap (Dashboard Kanan)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp)
        ) {
            Text("Main Dashboard", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard("Total Peminjam", "45", Modifier.weight(1f))
                DashboardCard("Total Pembayaran", "Rp 200.000", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Riwayat Peminjaman", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(dummyRiwayat) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nama: ${item.nama}")
                            Text("Ruangan: ${item.ruangan}")
                            Text("Waktu: ${item.waktu}")
                            Text("Kode: ${item.kode}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

data class RiwayatItem(val nama: String, val ruangan: String, val waktu: String, val kode: String)

val dummyRiwayat = listOf(
    RiwayatItem("Mithlesh Kumar Singh", "GKM 1", "07:00 - 13:00", "123456"),
    RiwayatItem("Suron Maharjan", "GKM 2", "08:00 - 15:00", "865328"),
    RiwayatItem("Sandesh Bajracharya", "GKM 1.2", "16:00 - 20:00", "783650"),
    RiwayatItem("Subin Sedhai", "GKM 1.3", "12:00 - 14:00", "863256"),
    RiwayatItem("Worjula Joshi", "GKM 1.3", "12:00 - 20:00", "4598728")
)

@Preview(
    showBackground = true,
    device = "spec:width=1024dp,height=768dp,dpi=240"
)
@Composable
fun AdminDashboardTabletPreview() {
    MaterialTheme {
        AdminDashboard(
            onNavigate = {},
            onLogout = {}
        )
    }
}
