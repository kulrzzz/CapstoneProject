package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import java.text.NumberFormat
import java.util.*

@Composable
fun DetailUserPage(
    customer: Customer,
    bookingList: List<Booking>,
    userRole: String?,
    onBackClick: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val textSize = 14.sp
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
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color(0xFFFF9800)
                    )
                }
                Text(
                    text = "Detail User",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF04A5D4),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFFF9800)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
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
                    Column {
                        InfoText(label = "ID Pengguna", value = customer.customer_id)
                        InfoText(label = "Nama Lengkap", value = customer.customer_fullname)
                        InfoText(label = "Email", value = customer.customer_email)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0E0E0))
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TableHeader("Kode Booking", 100.dp, textSize, headerTextColor)
                        TableHeader("Nama Gedung", 120.dp, textSize, headerTextColor)
                        TableHeader("Tanggal", 100.dp, textSize, headerTextColor)
                        TableHeader("Waktu", 100.dp, textSize, headerTextColor)
                        TableHeader("Nominal", 100.dp, textSize, headerTextColor)
                    }

                    LazyColumn {
                        items(bookingList) { booking ->
                            TableRow(booking = booking, textSize = textSize)
                        }
                    }

                    if (bookingList.isEmpty()) {
                        Text(
                            text = "Belum ada riwayat peminjaman.",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoText(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label:", modifier = Modifier.width(140.dp), fontWeight = FontWeight.Medium)
        Text(text = value)
    }
}

@Composable
fun TableHeader(text: String, width: Dp, fontSize: androidx.compose.ui.unit.TextUnit, color: Color) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.width(width),
        color = color,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TableRow(booking: Booking, textSize: androidx.compose.ui.unit.TextUnit) {
    val rowBg = Color.White
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(booking.booking_code.toString(), 100.dp, textSize)
        TableCell(booking.room_id, 120.dp, textSize)
        TableCell(booking.booking_date, 100.dp, textSize)
        TableCell("${booking.booking_start} - ${booking.booking_end}", 100.dp, textSize)
        TableCell(formatCurrency(booking.booking_price), 100.dp, textSize)
    }
}

@Composable
fun TableCell(
    text: String,
    width: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        fontSize = fontSize,
        textAlign = textAlign,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 4.dp),
        maxLines = 1
    )
}

fun formatCurrency(amount: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(amount)
}