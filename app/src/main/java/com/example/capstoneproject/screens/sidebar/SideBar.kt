package com.example.capstoneproject.screens.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.navigation.Screen


//enum class MenuItem {
//    DASHBOARD, RIWAYAT, DAFTAR_USER, MANAJEMEN_ADMIN, MANAJEMEN_RUANGAN, TAMBAH_RUANGAN
//}

@Composable
fun SideBar(
    userRole: String?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val menuItems = remember(userRole) {
        buildList {
            add(SidebarItemModel("Dashboard", R.drawable.homebefore, Screen.Dashboard))
            add(SidebarItemModel("Lihat Riwayat Transaksi", R.drawable.riwayat, Screen.RiwayatTransaksi))
            add(SidebarItemModel("Lihat Daftar User", R.drawable.daftaruser, Screen.DaftarUser))

            if (userRole == "root") {
                add(SidebarItemModel("DIVIDER", -1, Screen.Dashboard)) // Marker for Divider
                add(SidebarItemModel("Manajemen Admin", R.drawable.adminbefore, Screen.ManajemenAdmin))
                add(SidebarItemModel("Manajemen Ruangan", R.drawable.ruanganbefore, Screen.ManajemenRuangan))
            }
        }
    }

    Column(
        modifier = Modifier
            .width(262.dp)
            .fillMaxHeight()
            .background(Color(0xFFF8FAFC))
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

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
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.reservin3),
                    contentDescription = "Logo Text",
                    modifier = Modifier.height(20.dp)
                )
            }

            Divider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            // Render Menu Items
            menuItems.forEach { item ->
                if (item.icon == -1) {
                    Divider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                } else {
                    SidebarItem(iconRes = item.icon, text = item.title) {
                        onNavigate(item.screen)
                    }
                }
            }
        }

        LogoutButton(onLogout)
    }
}

@Composable
fun SidebarItem(iconRes: Int, text: String, onClick: () -> Unit) {
//    val background = if (isSelected) Color(0xFFD8ECFF) else Color.Transparent
//    val textColor = if (isSelected) Color(0xFF2563EB) else Color(0xFF718096)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent, RoundedCornerShape(8.dp))
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
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(48.dp)
            .clickable { onLogout() }
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFFFF8008), Color(0xFFFF4D00))
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Logout",
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
        )
    }
}

// Data class untuk menu item
data class SidebarItemModel(
    val title: String,
    val icon: Int,
    val screen: Screen
)

@Preview(showBackground = true, widthDp = 262, heightDp = 768)
@Composable
fun SideBarPreview() {
    SideBar(
        userRole = "root",
        onNavigate = {},
        onLogout = {}
    )
}