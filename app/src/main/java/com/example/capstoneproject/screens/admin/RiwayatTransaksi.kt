package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.Booking
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun RiwayatTransaksiPage(
    userRole: String?,
    transaksiList: List<Booking>,
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val spacing = 24.dp
    val textSize = 14.sp
    val titleSize = 30.sp
    val headerColor = Color(0xFFF0F4FF)
    val headerTextColor = Color(0xFF1A237E)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FF))
    ) {
        SideBar(
            userRole = userRole,
            onNavigate = onNavigate,
            onLogout = onLogout
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(spacing)
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Lihat Riwayat Transaksi",
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4)
            )

            Spacer(modifier = Modifier.height(spacing))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerColor)
                            .padding(vertical = 12.dp, horizontal = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableHeaderCell("No", 40.dp, textSize, headerTextColor)
                        TableHeaderCell("Kode", 100.dp, textSize, headerTextColor)
                        TableHeaderCell("Peminjam", 120.dp, textSize, headerTextColor)
                        TableHeaderCell("Gedung", 120.dp, textSize, headerTextColor)
                        TableHeaderCell("Nominal", 100.dp, textSize, headerTextColor)
                        TableHeaderCell("Status", 100.dp, textSize, headerTextColor)
                    }

                    Divider(color = Color.LightGray)

                    LazyColumn {
                        itemsIndexed(transaksiList) { index, booking ->
                            TransaksiRow(
                                no = index + 1,
                                booking = booking,
                                fontSize = textSize
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransaksiRow(
    no: Int,
    booking: Booking,
    fontSize: TextUnit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (no % 2 == 0) Color(0xFFF8FAFF) else Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = no.toString(),
            fontSize = fontSize,
//            textAlign = TextAlign.Center,
            modifier = Modifier.width(36.dp)
        )

        Text(
            text = booking.booking_id,
            fontSize = fontSize,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = booking.customer_id,
            fontSize = fontSize,
            modifier = Modifier.width(150.dp)
        )

        Text(
            text = booking.room_id,
            fontSize = fontSize,
            modifier = Modifier.width(150.dp)
        )

        Text(
            text = "Rp${booking.booking_price}",
            fontSize = fontSize,
            modifier = Modifier.width(120.dp)
        )

        StatusBadge(
            status = if (booking.booking_status == 1) "Lunas" else "Belum lunas",
            fontSize = fontSize,
            modifier = Modifier.width(120.dp)
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val isLunas = status.equals("Lunas", ignoreCase = true)
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