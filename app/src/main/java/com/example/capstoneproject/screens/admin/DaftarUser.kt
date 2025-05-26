package com.example.capstoneproject.screens.admin

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.viewmodel.CustomerViewModel

@Composable
fun DaftarUserPage(
    userRole: String?,
    onUserSelected: (String) -> Unit,
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: CustomerViewModel = hiltViewModel()
) {
    val customerList by remember { derivedStateOf { viewModel.customerList } }
    val isLoading by remember { derivedStateOf { viewModel.isLoading } }
    val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }

    val context = LocalContext.current

    // Coba muat data di dalam try-catch, hanya jika list kosong
    LaunchedEffect(Unit) {
        try {
            if (customerList.isEmpty()) {
                viewModel.fetchAllCustomers()
            }
        } catch (e: Exception) {
            viewModel.setError("Gagal memuat data: ${e.localizedMessage ?: "Unknown Error"}")
        }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Lihat Daftar User",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            when {
                isLoading -> {
                    CircularProgressIndicator(color = Color(0xFF04A5D4))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Terjadi kesalahan.",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
                customerList.isEmpty() -> {
                    Text(
                        text = "Belum ada data customer tersedia.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(customerList) { customer ->
                            DaftarUserCard(
                                nama = customer.customer_fullname,
                                email = customer.customer_email,
                                onClick = {
                                    try {
                                        onUserSelected(customer.customer_id)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Navigasi gagal", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DaftarUserCard(
    nama: String,
    email: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Nama   : $nama")
                Text("Email  : $email")
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Lihat Detail", color = Color(0xFF90A4AE))
            }
        }
    }
}