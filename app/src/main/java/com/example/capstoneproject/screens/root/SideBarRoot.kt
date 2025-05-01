package com.example.capstoneproject.screens.root

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capstoneproject.R

enum class MenuItem {
    DASHBOARD, RIWAYAT, DAFTAR_USER, MANAJEMEN_ADMIN, MANAJEMEN_RUANGAN, TAMBAH_RUANGAN
}

@Composable
fun SideBarRoot(
    selectedMenu: MenuItem,
    onMenuSelected: (MenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .width(262.dp)
            .fillMaxHeight()
            .background(Color(0xFFF8FAFC))
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp)) // Biar logo turun dikit

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoreservin),
                    contentDescription = "Logo",
                    modifier = Modifier.height(30.dp)
                )
                Spacer(modifier = Modifier.width(4.dp)) // Atur jarak logo + text
                Image(
                    painter = painterResource(id = R.drawable.reservin3),
                    contentDescription = "Logo Text",
                    modifier = Modifier.height(20.dp)
                )
            }

            Divider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.fillMaxWidth())

            SidebarItem(R.drawable.homebefore, "Dashboard", selectedMenu == MenuItem.DASHBOARD) {
                onMenuSelected(MenuItem.DASHBOARD)
            }
            SidebarItem(R.drawable.riwayat, "Lihat Riwayat Transaksi", selectedMenu == MenuItem.RIWAYAT) {
                onMenuSelected(MenuItem.RIWAYAT)
            }
            SidebarItem(R.drawable.daftaruser, "Lihat Daftar User", selectedMenu == MenuItem.DAFTAR_USER) {
                onMenuSelected(MenuItem.DAFTAR_USER)
            }

            Divider(color = Color(0xFFE2E8F0), thickness = 1.dp, modifier = Modifier.fillMaxWidth())

            SidebarItem(R.drawable.adminbefore, "Manajemen Admin", selectedMenu == MenuItem.MANAJEMEN_ADMIN) {
                onMenuSelected(MenuItem.MANAJEMEN_ADMIN)
            }
            SidebarItem(R.drawable.ruanganbefore, "Manajemen Ruangan", selectedMenu == MenuItem.MANAJEMEN_RUANGAN) {
                onMenuSelected(MenuItem.MANAJEMEN_RUANGAN)
            }
        }

        LogoutButton()
    }
}

@Composable
fun SidebarItem(iconRes: Int, text: String, isSelected: Boolean, onClick: () -> Unit) {
    val background = if (isSelected) Color(0xFFD8ECFF) else Color.Transparent
    val textColor = if (isSelected) Color(0xFF2563EB) else Color(0xFF718096)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
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
                    colors = listOf(Color(0xFFFF5F6D), Color(0xFFFFC371))
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

@Preview(showBackground = true, widthDp = 262, heightDp = 768)
@Composable
fun SideBarRootPreview() {
    var selectedMenu by remember { mutableStateOf(MenuItem.DASHBOARD) }

    SideBarRoot(
        selectedMenu = selectedMenu,
        onMenuSelected = { selectedMenu = it }
    )
}
