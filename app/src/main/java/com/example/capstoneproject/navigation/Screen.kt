package com.example.capstoneproject.navigation

sealed class Screen(val route: String) {

    // General Screens (dapat diakses oleh admin & root)
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object SideBar : Screen("sidebar")

    // Admin-only Screens
    object RiwayatTransaksi : Screen("riwayat_transaksi")
    object DaftarUser : Screen("daftar_user")
    object DetailUser : Screen("detail_user") // Diperlukan karena ada di folder admin

    // Root-only Screens (akses penuh termasuk fitur admin)
    object ManajemenAdmin : Screen("manajemen_admin")
    object TambahAdmin : Screen("tambah_admin")
    object ManajemenRuangan : Screen("manajemen_ruangan")
    object TambahRuangan : Screen("tambah_ruangan")
    object EditAdmin : Screen("edit_admin") // Tambahan berdasarkan file EditAdmin.kt
}