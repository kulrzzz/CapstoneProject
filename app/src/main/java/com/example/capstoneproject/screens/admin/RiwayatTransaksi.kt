package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun RiwayatTransaksiPage(
    userRole: String?,
    transaksiList: List<Booking>,
    isLoading: Boolean,
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val spacing = 20.dp
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

            // Scroll horizontal hanya untuk tabel
            val horizontalScrollState = rememberScrollState()

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    // Header Tabel
                    Row(
                        modifier = Modifier
                            .background(headerColor)
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableHeaderCell("No", 70.dp, textSize, headerTextColor)
                        TableHeaderCell("Kode Booking", 170.dp, textSize, headerTextColor)
                        TableHeaderCell("Peminjam", 170.dp, textSize, headerTextColor)
                        TableHeaderCell("Ruangan", 150.dp, textSize, headerTextColor)
                        TableHeaderCell("Tanggal", 120.dp, textSize, headerTextColor)
                        TableHeaderCell("x", 220.dp, textSize, headerTextColor, textAlign = TextAlign.Center)
                    }

                    Divider(color = Color.LightGray)

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF04A5D4),
                                strokeWidth = 4.dp
                            )
                        }
                    } else {
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
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = no.toString(),
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(70.dp)
        )

        Text(
            text = booking.booking_code,
            fontSize = fontSize,
            modifier = Modifier.width(170.dp)
        )

        Text(
            text = booking.customer_fullname,
            fontSize = fontSize,
            modifier = Modifier.width(170.dp)
        )

        Text(
            text = booking.room_name,
            fontSize = fontSize,
            modifier = Modifier.width(150.dp)
        )

        Text(
            text = booking.booking_date,
            fontSize = fontSize,
            modifier = Modifier.width(100.dp)
        )

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.width(240.dp)
                .padding(start = 30.dp)
        ) {
            Text("Lihat Detail", fontSize = fontSize)
        }
    }
}
