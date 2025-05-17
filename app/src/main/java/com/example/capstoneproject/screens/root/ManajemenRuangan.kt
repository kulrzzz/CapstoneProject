package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.model.Room
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun ManajemenRuanganPage(
    roomList: List<Room>,
    onTambahRuanganClick: () -> Unit,
    onEditRoom: (Room) -> Unit,
    onDeleteRoom: (Room) -> Unit,
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val spacing = 24.dp
    val textSize = 14.sp
    val titleSize = 30.sp
    val headerColor = Color(0xFFF0F4FF)
    val headerTextColor = Color(0xFF1A237E)

    Row(modifier = Modifier
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
                Text("Tambah Ruangan", fontSize = textSize, color = Color.White)
            }

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
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableHeaderCell("No", 75.dp, textSize, headerTextColor)
                        TableHeaderCell("Nama Ruangan", 240.dp, textSize, headerTextColor)
                        TableHeaderCell("Kategori", 240.dp, textSize, headerTextColor)
                        TableHeaderCell("Actions", 120.dp, textSize, headerTextColor)
                    }

                    Divider(color = Color.LightGray)

                    LazyColumn {
                        itemsIndexed(roomList) { index, room ->
                            RoomRow(
                                no = index + 1,
                                room = room,
                                textSize = textSize,
                                onEdit = { onEditRoom(room) },
                                onDelete = { onDeleteRoom(room) }
                            )
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
    onDelete: () -> Unit
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
            fontSize = textSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(36.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Text(
            text = room.room_name,
            fontSize = textSize,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .width(240.dp)
                .padding(start = 48.dp)
        )

        Text(
            text = room.room_kategori.orEmpty(),
            fontSize = textSize,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .width(240.dp)
                .padding(start = 48.dp)
        )

        Row(
            modifier = Modifier
                .width(120.dp)
                .padding(start = 26.dp)
        ) {
            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit",
                    tint = Color (0xFF1570EF),
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

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun ManajemenRuanganPreview() {
    val dummyRooms = List(5) {
        com.example.capstoneproject.model.Room(
            room_id = "$it",
            room_name = "Ruang Meeting ${'A' + it}",
            room_desc = "Deskripsi ruang meeting",
            room_kategori = "Gedung GKM Lt. ${1 + (it % 3)}",
            room_capacity = 20 + it,
            room_price = 50000L,
            room_available = 1,
            room_start = "08:00",
            room_end = "17:00",
            created_at = "2024-01-01",
            updated_at = "2024-01-02"
        )
    }

    ManajemenRuanganPage(
        roomList = dummyRooms,
        onTambahRuanganClick = {},
        onEditRoom = {},
        onDeleteRoom = {},
        onNavigate = {},
        onLogout = {}
    )
}