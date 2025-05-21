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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.Booking
import com.example.capstoneproject.model.Customer
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableHeaderCell
import java.text.NumberFormat
import java.util.*

@Composable
fun DetailUserPage(
    customer: Customer,
    bookingList: List<Booking>,
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
            // Header
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

            // Informasi Pengguna
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoText(label = "ID Pengguna        :", value = customer.customer_id)
                    InfoText(label = "Nama Lengkap    :", value = customer.customer_fullname)
                    InfoText(label = "Email                      :", value = customer.customer_email)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tabel Riwayat Peminjaman
            Card (
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0E0E0))
                            .padding(vertical = 12.dp, horizontal = 10.dp)
                    ) {
                        TableHeaderCell(
                            "No",
                            40.dp,
                            textSize,
                            headerTextColor,
                            textAlign = TextAlign.Center
                        )
                        TableHeaderCell("Kode Booking", 100.dp, textSize, headerTextColor)
                        TableHeaderCell("Nama Gedung", 120.dp, textSize, headerTextColor)
                        TableHeaderCell("Tanggal Peminjaman", 120.dp, textSize, headerTextColor)
                        TableHeaderCell("Waktu Peminjaman", 120.dp, textSize, headerTextColor)
                        TableHeaderCell("Nominal Pembayaran", 140.dp, textSize, headerTextColor)
                    }

                    LazyColumn {
                        itemsIndexed(bookingList) { index, booking ->
                            DetailRow(
                                no = index + 1,
                                booking = booking,
                                textSize = textSize
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoText(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label ", modifier = Modifier.width(140.dp), fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}

@Composable
fun DetailRow(
    no: Int,
    booking: Booking,
    textSize: androidx.compose.ui.unit.TextUnit
) {
    val rowBg = if (no % 2 == 0) Color(0xFFF8FAFF) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(no.toString(), 45.dp, textSize, TextAlign.Center)
        TableCell(booking.booking_code.toString(), 100.dp, textSize)
        TableCell(booking.room_id, 120.dp, textSize)
        TableCell(booking.booking_date, 120.dp, textSize)
        TableCell("${booking.booking_start} - ${booking.booking_end}", 120.dp, textSize)
        TableCell(formatCurrency(booking.booking_price), 140.dp, textSize)
    }
}

@Composable
fun TableCell(
    text: String,
    width: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    textAlign: TextAlign = TextAlign.Start
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

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun DetailUserPagePreview() {
    val dummyCustomer = Customer(
        customer_id = "02102",
        customer_fullname = "Jessica",
        customer_email = "jessica@gmail.com",
        customer_pass = "password123",
        created_at = null,
        updated_at = null
    )

    val dummyBookings = List(8) {
        Booking(
            booking_id = "B${it + 1}",
            customer_id = "02102",
            room_id = "GKM Lantai 2",
            booking_code = 12358,
            booking_date = "12-09-24",
            booking_start = "07.00",
            booking_end = "13.00",
            booking_desc = "Untuk presentasi",
            booking_price = 50000,
            booking_status = 1,
            created_at = null,
            updated_at = null
        )
    }

    DetailUserPage(
        customer = dummyCustomer,
        bookingList = dummyBookings,
        onBackClick = {}
    )
}
