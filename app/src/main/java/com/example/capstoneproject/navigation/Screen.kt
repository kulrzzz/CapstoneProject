package com.example.capstoneproject.navigation

sealed class Screen(val route: String) {

    // General
    object Login : Screen("login")
    object Dashboard : Screen("dashboard") // digunakan bersama untuk admin & root

    // Admin-only Screens
    object RiwayatTransaksi : Screen("riwayat_transaksi")
    object DaftarUser : Screen("daftar_user")

    // Root-only Screens (5 fitur termasuk admin)
    object ManajemenAdmin : Screen("manajemen_admin")
    object TambahAdmin : Screen("tambah_admin")
    object ManajemenRuangan : Screen("manajemen_ruangan")
    object TambahRuangan : Screen("tambah_ruangan")
}
