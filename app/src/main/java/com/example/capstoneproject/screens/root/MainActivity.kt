package com.example.capstoneproject.screens.root

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    var selectedMenu by remember { mutableStateOf(MenuItem.DASHBOARD) }

    Row(Modifier.fillMaxSize()) {
        SideBarRoot(
            selectedMenu = selectedMenu,
            onMenuSelected = { selectedMenu = it }
        )

        when (selectedMenu) {
            MenuItem.MANAJEMEN_RUANGAN -> ManajemenRuangan(
                onTambahRuanganClick = { /* navigasi ke TambahRuanganScreen */ }
            )

            MenuItem.MANAJEMEN_RUANGAN -> TambahRuangan()
            else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Silahkan pilih menu lain", style = MaterialTheme.typography.headlineSmall)
            }
        }
//        when (selectedMenu) {
////            MenuItem.DASHBOARD -> DashboardScreen()
//            MenuItem.MANAJEMEN_RUANGAN -> ManajemenRuangan(
//                onTambahRuanganClick = { /* navigasi ke TambahRuanganScreen */ }
//            )
//            // lainnya
//            MenuItem.DASHBOARD -> TODO()
//            MenuItem.RIWAYAT -> TODO()
//            MenuItem.DAFTAR_USER -> TODO()
//            MenuItem.MANAJEMEN_ADMIN -> TODO()
//        }
    }
}

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
