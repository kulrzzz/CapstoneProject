package com.example.capstoneproject.screens.superadmin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.model.room.Room
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableBodyCell
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun ManajemenRuanganPage(
    userRole: String?,
    roomList: List<Room>,
    isLoading: Boolean,
    onTambahRuanganClick: () -> Unit,
    onEditRoom: (Room) -> Unit,
    onDeleteRoom: (Room) -> Unit,
    onToggleAvailability: (Room, Boolean) -> Unit,
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
                text = "Manajemen Ruangan",
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4)
            )

            Spacer(modifier = Modifier.height(spacing))

            Button(
                onClick = onTambahRuanganClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1570EF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.tambahruangan),
                    contentDescription = "Tambah Ruangan",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tambah Ruangan", fontSize = textSize, color = Color.White)
            }

            Spacer(modifier = Modifier.height(spacing))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF04A5D4))
                        }
                    }

                    roomList.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada data ruangan ditemukan.")
                        }
                    }

                    else -> {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(headerColor)
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TableHeaderCell("No", 50.dp, textSize, headerTextColor, modifier = Modifier.padding(horizontal = 10.dp))
                                TableHeaderCell("Nama Ruangan", 200.dp, textSize, headerTextColor)
                                TableHeaderCell("Kategori", 155.dp, textSize, headerTextColor)
                                TableHeaderCell("Status",100.dp,textSize,headerTextColor)
                                TableHeaderCell("Actions",170.dp,textSize,headerTextColor)
                            }

                            Divider(color = Color.LightGray)

                            LazyColumn {
                                itemsIndexed(roomList) { index, room ->
                                    RoomRow(
                                        no = index + 1,
                                        room = room,
                                        textSize = textSize,
                                        onEdit = { onEditRoom(room) },
                                        onDelete = { onDeleteRoom(room) },
                                        onToggleAvailability = { isAvailable ->
                                            onToggleAvailability(
                                                room,
                                                isAvailable
                                            )
                                        }
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
fun RoomRow(
    no: Int,
    room: Room,
    textSize: TextUnit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleAvailability: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (no % 2 == 0) Color(0xFFF8FAFF) else Color.White)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableBodyCell(no.toString(), 50.dp, textSize, modifier = Modifier.padding(horizontal = 10.dp))
        TableBodyCell(room.room_name, 200.dp,textSize)
        TableBodyCell(room.room_kategori, 155.dp, textSize)
        Box(
            modifier = Modifier
                .width(95.dp)
                .padding(start = 10.dp)
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(
                        color = if (room.room_available == 1) Color.Green else Color.Red,
                        shape = RoundedCornerShape(percent = 50)
                    )
            )
        }
        Box(
            modifier = Modifier
                .width(167.dp)
                .padding(start = 13.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit",
                        tint = Color(0xFF1570EF),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash),
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}