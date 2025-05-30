package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.model.booking.BookingDetail
import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableBodyCell
import com.example.capstoneproject.ui.theme.TableHeaderCell
import java.text.NumberFormat
import java.util.*

@Composable
fun DetailUserPage(
    customer: Customer,
    bookingList: List<BookingDetail>,
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
            Spacer(modifier = Modifier.height(25.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Kembali",
                        tint = Color(0xFFFF9800)
                    )
                }
                Text(
                    text = "Detail User",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF04A5D4),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            UserInfoCard(customer = customer)

            Spacer(modifier = Modifier.height(24.dp))

            BookingHistoryTable(
                bookingList = bookingList,
                textSize = textSize,
                headerTextColor = headerTextColor
            )
        }
    }
}

@Composable
fun UserInfoCard(customer: Customer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
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
                InfoText(label = "Nama  ", value = customer.customer_fullname)
                InfoText(label = "Email   ", value = customer.customer_email)
            }
        }
    }
}

@Composable
fun BookingHistoryTable(
    bookingList: List<BookingDetail>,
    textSize: androidx.compose.ui.unit.TextUnit,
    headerTextColor: Color
) {
    val scrollState = rememberScrollState()
    Card(
        modifier = Modifier.fillMaxSize(),
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
                    .padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                TableHeaderCell("No", 60.dp, textSize, headerTextColor, textAlign = TextAlign.Center)
                TableHeaderCell("Kode Booking", 180.dp, textSize, headerTextColor)
                TableHeaderCell("Nama Gedung", 180.dp, textSize, headerTextColor)
                TableHeaderCell("Tanggal", 180.dp, textSize, headerTextColor)
                TableHeaderCell("Waktu", 200.dp, textSize, headerTextColor)
                TableHeaderCell("Nominal", 150.dp, textSize, headerTextColor)
            }

            if (bookingList.isEmpty()) {
                Text(
                    text = "Belum ada riwayat peminjaman.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    itemsIndexed(bookingList) { index, booking ->
                        BookingRow(
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

@Composable
fun BookingRow(
    no: Int,
    booking: BookingDetail,
    textSize: androidx.compose.ui.unit.TextUnit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (no % 2 == 0) Color(0xFFF8FAFF) else Color.White)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableBodyCell(no.toString(), 60.dp, textSize)
        TableBodyCell(booking.booking_code, 180.dp, textSize)
        TableBodyCell(booking.room_name, 180.dp, textSize)
        TableBodyCell(booking.booking_date, 180.dp, textSize)
        TableBodyCell("${booking.booking_start} - ${booking.booking_end}", 200.dp, textSize)
        TableBodyCell(formatCurrency(booking.booking_price), 150.dp, textSize)
    }
}

@Composable
fun InfoText(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(text = "  $label:   ", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Medium)
        Text(text = value)
    }
}

fun formatCurrency(amount: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(amount)
}