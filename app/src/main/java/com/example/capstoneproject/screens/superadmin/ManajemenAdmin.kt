package com.example.capstoneproject.screens.superadmin

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.model.admin.Admin
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.ui.theme.TableBodyCell
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun ManajemenAdminPage(
    userRole: String?,
    adminRequestList: List<Admin>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onTambahAdminClick: () -> Unit,
    onEditAdmin: (Admin) -> Unit,
    onDeleteAdmin: (Admin) -> Unit,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
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
        // Sidebar navigasi kiri
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
                        val scrollState = rememberScrollState()

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .background(headerColor)
                                        .padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TableHeaderCell("No", 50.dp, textSize, headerTextColor, modifier = Modifier.padding(start = 20.dp))
                                    TableHeaderCell("Fullname", 320.dp, textSize, headerTextColor)
                                    TableHeaderCell("Email", 340.dp, textSize, headerTextColor)
                                    TableHeaderCell("Actions", 170.dp, textSize, headerTextColor)
                                }

                                Divider(color = Color.LightGray)

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
}

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
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableBodyCell(no.toString(), 50.dp, textSize, modifier = Modifier.padding(start = 20.dp))
        TableBodyCell(admin.admin_fullname, 320.dp, textSize)
        TableBodyCell(admin.admin_email, 340.dp, textSize)
        Box(
            modifier = Modifier
                .width(170.dp)
                .padding(start = 10.dp)
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
