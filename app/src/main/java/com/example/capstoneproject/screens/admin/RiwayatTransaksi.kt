package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.booking.Booking
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableBodyCell
import com.example.capstoneproject.ui.theme.TableHeaderCell
import com.example.capstoneproject.R

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

    var searchQuery by remember { mutableStateOf("") }
    val filteredTransaksiList = transaksiList.filter {
        it.customer_fullname.contains(searchQuery, ignoreCase = true) ||
                it.room_name.contains(searchQuery, ignoreCase = true)
    }

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(25.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lihat Riwayat Transaksi",
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF04A5D4)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Cari nama atau email...",
                                color = Color(0xFF94A3B8)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search Icon",
                                tint = Color(0xFF1E3A8A)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .width(300.dp)
                            .height(56.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF04A5D4),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(spacing))

                val horizontalScrollState = rememberScrollState()

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(horizontalScrollState),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .background(headerColor)
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableHeaderCell("No", 40.dp, textSize, headerTextColor, modifier = Modifier.padding(start = 10.dp))
                            TableHeaderCell("Kode Booking", 160.dp, textSize, headerTextColor, modifier = Modifier.padding(start = 30.dp))
                            TableHeaderCell("Peminjam", 190.dp, textSize, headerTextColor)
                            TableHeaderCell("Ruangan", 160.dp, textSize, headerTextColor)
                            TableHeaderCell("Tanggal", 150.dp, textSize, headerTextColor)
                            TableHeaderCell("Waktu", 170.dp, textSize, headerTextColor)
                            TableHeaderCell("Nominal", 150.dp, textSize, headerTextColor)
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
                                itemsIndexed(filteredTransaksiList) { index, booking ->
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
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableBodyCell(no.toString(), 40.dp, fontSize, modifier = Modifier.padding(start = 10.dp))
        TableBodyCell(booking.booking_code, 160.dp, fontSize, modifier = Modifier.padding(start = 30.dp))
        TableBodyCell(booking.customer_fullname, 190.dp, fontSize)
        TableBodyCell(booking.room_name, 160.dp, fontSize)
        TableBodyCell(booking.booking_date, 150.dp, fontSize)
        TableBodyCell("${booking.booking_start} - ${booking.booking_end}", 170.dp, fontSize)
        TableBodyCell(booking.booking_price.toString(), 150.dp, fontSize)
    }
}
