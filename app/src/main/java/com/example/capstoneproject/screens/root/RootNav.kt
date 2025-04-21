package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(240.dp)
            .background(Color(0xFFF4F6FA)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Logo Section
            Text(
                text = "ReservIn",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A73E8),
                modifier = Modifier
                    .padding(16.dp)
            )

            val menuItems = listOf(
                "Dashboard",
                "Validasi Transaksi",
                "Lihat Riwayat Transaksi",
                "Transaksi Per User",
                "Transaksi Harian",
                "Lihat Daftar User"
            )

            menuItems.forEach { item ->
                val isSelected = item == selectedItem
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(item) }
                        .background(if (isSelected) Color(0xFFD6ECFF) else Color.Transparent)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = when (item) {
                            "Dashboard" -> Icons.Default.Dashboard
                            "Lihat Riwayat Transaksi" -> Icons.Default.List
                            "Lihat Daftar User" -> Icons.Default.People
                            else -> Icons.Default.List
                        },
                        contentDescription = null,
                        tint = if (isSelected) Color(0xFF1A73E8) else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFF1A73E8) else Color.Black
                    )
                }
            }
        }

        // Logout Button
        Button(
            onClick = { /* TODO: Handle Logout */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF6B00)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = Color.White)
        }
    }
}
