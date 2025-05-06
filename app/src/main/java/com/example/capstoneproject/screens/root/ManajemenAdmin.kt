package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.ui.theme.TableHeaderCell

@Composable
fun ManajemenAdminPage(
    adminList: List<Admin> = dummyAdmins,
    onTambahAdminClick: () -> Unit,
    onBack: () -> Unit
) {
    val titleColor = Color(0xFF009DFF)
    val headerBg = Color(0xFFF5F7FF)
    val spacingLarge = 24.dp
    val spacingMedium = 16.dp
    val textSizeTitle = 22.sp
    val textSizeNormal = 12.sp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium)
    ) {
        Text(
            text = "Manajemen Admin",
            fontSize = textSizeTitle,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(spacingLarge))

        // Tombol Tambah Admin
        Button(
            onClick = onTambahAdminClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007BFF),
                contentColor = Color.White
            )
        ) {
            Text("Tambah Admin", fontSize = textSizeNormal)
        }

        Spacer(modifier = Modifier.height(spacingMedium))

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerBg)
                        .padding(vertical = spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableHeaderCell("ID", 80.dp, textSizeNormal)
                    TableHeaderCell("Nama Lengkap", 120.dp, textSizeNormal)
                    TableHeaderCell("Email", 140.dp, textSizeNormal)
                    TableHeaderCell("Password", 100.dp, textSizeNormal)
                    TableHeaderCell("Who", 60.dp, textSizeNormal)
                    TableHeaderCell("Created At", 100.dp, textSizeNormal)
                    TableHeaderCell("Updated At", 100.dp, textSizeNormal)
                }

                LazyColumn {
                    items(adminList) { admin ->
                        AdminRow(admin, textSizeNormal)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRow(admin: Admin, textSize: androidx.compose.ui.unit.TextUnit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(admin.admin_id, fontSize = textSize, modifier = Modifier.width(80.dp))
        Text(admin.admin_fullname, fontSize = textSize, modifier = Modifier.width(120.dp))
        Text(admin.admin_email, fontSize = textSize, modifier = Modifier.width(140.dp))
        Text(admin.admin_pass, fontSize = textSize, modifier = Modifier.width(100.dp))
        Text(admin.admin_who.toString(), fontSize = textSize, modifier = Modifier.width(60.dp))
        Text(admin.created_at, fontSize = textSize, modifier = Modifier.width(100.dp))
        Text(admin.updated_at, fontSize = textSize, modifier = Modifier.width(100.dp))
    }
}

// Dummy data
val dummyAdmins = listOf(
    Admin("ADM01", "Siti Aisyah", "aisyah@email.com", "pass123", 1, "2024-01-01", "2024-02-01"),
    Admin("ADM02", "Budi Santoso", "budi@email.com", "adminBudi", 2, "2024-01-15", "2024-03-05"),
    Admin("ADM03", "Rina Dewi", "rina@email.com", "rinaPass", 1, "2024-02-10", "2024-04-01")
)

@Preview(showBackground = true, widthDp = 762, heightDp = 768)
@Composable
fun ManajemenAdminPreview() {
    ManajemenAdminPage(
        onTambahAdminClick = {},
        onBack = {}
    )
}