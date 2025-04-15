package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.navigation.Screen

@Composable
fun AdminDashboard(onNavigate: (Screen) -> Unit, onLogout: () -> Unit) {
    val menuItems = listOf(
        "Validasi Transaksi" to Screen.ValidasiTransaksi,
        "Riwayat Transaksi" to Screen.RiwayatTransaksi,
        "Transaksi Per User" to Screen.TransaksiPerUser,
        "Transaksi Harian" to Screen.TransaksiHarian,
        "Lihat Daftar User" to Screen.DaftarUser,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))

        menuItems.forEach { (label, screen) ->
            Button(
                onClick = { onNavigate(screen) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = label, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text("Logout", color = Color.White)
        }
    }
}