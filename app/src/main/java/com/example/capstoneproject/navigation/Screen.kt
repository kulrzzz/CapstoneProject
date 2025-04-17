package com.example.capstoneproject.navigation

sealed class Screen(val route: String) {

    // General / Auth
    object Login : Screen("login")

    // Dashboard
    object AdminDashboard : Screen("admin_dashboard")
    object RootDashboard : Screen("root_dashboard")

    // Admin Screens
    object RiwayatTransaksi : Screen("riwayat_transaksi")
    object DaftarUser : Screen("daftar_user")

    // Root Screens
    object CrudAdmin : Screen("crud_admin")
    object CrudRuangan : Screen("crud_ruangan")
}