package com.example.capstoneproject.screens.admin


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.capstoneproject.R

@Composable
fun SideBarAdmin(
    logoSpacing: Dp = 25.dp
) {
    Column(
        modifier = Modifier
            .width(262.dp)
            .height(768.dp)
            .background(Color(0xFFF8FAFC))
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Logo
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(logoSpacing),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoreservin),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.reservin3),
                    contentDescription = "Logo Text",
                    modifier = Modifier.height(20.dp)
                )
            }

            Divider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            SidebarItem(iconRes = R.drawable.homebefore, text = "Dashboard")
            SidebarItem(iconRes = R.drawable.riwayat, text = "Lihat Riwayat Transaksi")
            SidebarItem(iconRes = R.drawable.daftaruser, text = "Lihat Daftar User")
        }

        LogoutButton()
    }
}

@Composable
fun SidebarItem(iconRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF718096)
        )
    }
}

@Composable
fun LogoutButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFF4781A), Color(0xFFE53C08))
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Logout",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SideBarPreview() {
    MaterialTheme {
        SideBarAdmin()
    }
}
