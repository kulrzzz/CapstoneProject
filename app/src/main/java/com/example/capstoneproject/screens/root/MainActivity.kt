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
                onTambahRuanganClick = { selectedMenu = MenuItem.TAMBAH_RUANGAN }
            )
            MenuItem.TAMBAH_RUANGAN -> TambahRuangan()
            MenuItem.DASHBOARD -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ini Dashboard", style = MaterialTheme.typography.headlineSmall)
            }
            MenuItem.RIWAYAT -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ini Riwayat", style = MaterialTheme.typography.headlineSmall)
            }
            MenuItem.DAFTAR_USER -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ini Daftar User", style = MaterialTheme.typography.headlineSmall)
            }
            MenuItem.MANAJEMEN_ADMIN -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ini Manajemen Admin", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
