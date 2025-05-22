package com.example.capstoneproject.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.Customer
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar

@Composable
fun DaftarUserPage(
    customerList: List<Customer>,
    onUserSelected: (String) -> Unit, // ✅ Kirim ID ke navigator
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
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

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(customerList) { customer ->
                    DaftarUserCard(
                        id = customer.customer_id,
                        nama = customer.customer_fullname,
                        email = customer.customer_email,
                        onClick = {
                            onUserSelected(customer.customer_id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DaftarUserCard(
    id: String,
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
                Text("ID Customer       :      $id")
                Text("Nama                   :      $nama")
                Text("Email                    :      $email")
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

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun DaftarUserPagePreview() {
    val dummyCustomerList = listOf(
        Customer(
            customer_id = "CUS001",
            customer_fullname = "Jessica",
            customer_email = "jessica@example.com",
            customer_pass = "secret",
            created_at = null,
            updated_at = null
        ),
        Customer(
            customer_id = "CUS002",
            customer_fullname = "Fajar",
            customer_email = "fajar@example.com",
            customer_pass = "secret",
            created_at = null,
            updated_at = null
        )
    )
}
