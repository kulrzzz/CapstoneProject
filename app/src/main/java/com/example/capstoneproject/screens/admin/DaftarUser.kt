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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.R
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
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val deleteSuccess by viewModel.deleteSuccess
    val context = LocalContext.current

    // Fetch customer list on load
    LaunchedEffect(Unit) {
        if (customerList.isEmpty() && !isLoading) {
            viewModel.fetchCustomers(BuildConfig.API_ACCESS_TOKEN)
        }
    }

    // Show toast on delete success
    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess == true) {
            Toast.makeText(context, "User dihapus", Toast.LENGTH_SHORT).show()
            viewModel.resetDeleteState()
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Daftar User",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF04A5D4),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                when {
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Terjadi kesalahan.",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }

                    customerList.isEmpty() && !isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AssistChip(
                                onClick = {},
                                label = { Text("Data user tidak ditemukan") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.daftaruser),
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }

                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(customerList) { customer ->
                                DaftarUserCard(
                                    nama = customer.customer_fullname,
                                    email = customer.customer_email,
                                    onClick = {
                                        // Navigasi hanya jika customer ID tidak kosong
                                        if (customer.customer_id.isNotBlank()) {
                                            onUserSelected(customer.customer_id)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "ID user tidak ditemukan.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    onDelete = {
                                        viewModel.deleteCustomer(
                                            accessToken = BuildConfig.API_ACCESS_TOKEN,
                                            customerId = customer.customer_id
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFF04A5D4),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun DaftarUserCard(
    nama: String,
    email: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray),
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

            Column(modifier = Modifier.weight(1f)) {
                Text("Nama  : $nama")
                Text("Email   : $email")
            }

            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Lihat Detail",
                    tint = Color(0xFF1570EF),
                    modifier = Modifier.size(25.dp)
                )
            }

            IconButton(
//                onClick = onDelete
                onClick = {/*todo*/}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.trash),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(23.dp)
                )
            }
        }
    }
}