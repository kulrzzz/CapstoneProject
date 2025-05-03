package com.example.capstoneproject.screens.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.R
import com.example.capstoneproject.navigation.Screen
import androidx.compose.material3.*

@Composable
fun SideBar(
    viewModel: MainViewModel = viewModel(),
    userRole: String?, // ⬅️ TAMBAHKAN INI
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val commonMenu = listOf(
        MenuItem("Dashboard", R.drawable.homebefore, R.drawable.homeafter, Screen.Dashboard),
        MenuItem("Lihat Riwayat Transaksi", R.drawable.riwayat, R.drawable.riwayatafter, Screen.RiwayatTransaksi),
        MenuItem("Lihat Daftar User", R.drawable.user, R.drawable.useruser, Screen.DaftarUser)
    )

    val rootExtraMenu = listOf(
        MenuItem("Manajemen Admin", R.drawable.adminbefore, R.drawable.adminafter, Screen.ManajemenAdmin),
        MenuItem("Manajemen Ruangan", R.drawable.ruanganbefore, R.drawable.ruanganafter, Screen.ManajemenRuangan)
    )

    val menuItems = if (userRole == "root") commonMenu + rootExtraMenu else commonMenu

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.reservin3),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .height(48.dp)
            )

            menuItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { onNavigate(item.screen) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = item.iconBefore),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = item.title, fontSize = 14.sp, color = Color.Black)
                }
            }
        }

        Button(
            onClick = onLogout,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5A00))
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

data class MenuItem(
    val title: String,
    val iconBefore: Int,
    val iconAfter: Int,
    val screen: Screen
)