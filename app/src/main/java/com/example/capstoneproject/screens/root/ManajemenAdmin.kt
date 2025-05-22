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
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableHeaderCell

/**
 * Halaman utama Manajemen Admin yang menampilkan daftar admin.
 * Menyediakan aksi: tambah, edit, hapus.
 * Meng-handle state loading, error, dan tampilan kosong.
 */
@Composable
fun ManajemenAdminPage(
    adminRequestList: List<Admin>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onTambahAdminClick: () -> Unit,
    onEditAdmin: (Admin) -> Unit,
    onDeleteAdmin: (Admin) -> Unit,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
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
        // Sidebar navigasi kiri
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
                text = "Manajemen Admin",
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4)
            )

            Spacer(modifier = Modifier.height(spacing))

            // Tombol tambah admin
            Button(
                onClick = onTambahAdminClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1570EF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.tambahadmin),
                    contentDescription = "Tambah Admin",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tambah Admin", fontSize = 14.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(spacing))

            // Container card berisi konten utama (tabel)
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                // ================================
                // 📦 STATE HANDLING: Loading / Error / Kosong / Konten
                // ================================
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 16.sp
                            )
                        }
                    }

                    adminRequestList.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada admin yang ditemukan.")
                        }
                    }

                    else -> {
                        Column {
                            // Header Tabel
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(headerColor)
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TableHeaderCell("No", 75.dp, textSize, headerTextColor)
                                TableHeaderCell("Fullname", 240.dp, textSize, headerTextColor)
                                TableHeaderCell("Email", 240.dp, textSize, headerTextColor)
                                TableHeaderCell("Actions", 120.dp, textSize, headerTextColor)
                            }

                            Divider(color = Color.LightGray)

                            // Konten Tabel
                            LazyColumn {
                                itemsIndexed(adminRequestList) { index, admin ->
                                    AdminRow(
                                        no = index + 1,
                                        admin = admin,
                                        textSize = textSize,
                                        onEdit = { onEditAdmin(admin) },
                                        onDelete = { onDeleteAdmin(admin) }
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

/**
 * Baris individual dalam daftar admin.
 */
@Composable
fun AdminRow(
    no: Int,
    admin: Admin,
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
            text = admin.admin_fullname,
            fontSize = textSize,
            modifier = Modifier
                .width(240.dp)
                .padding(start = 48.dp)
        )

        Text(
            text = admin.admin_email,
            fontSize = textSize,
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