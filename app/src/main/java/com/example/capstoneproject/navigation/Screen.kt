package com.example.capstoneproject.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object AdminDashboard : Screen("admin_dashboard")
    object RootDashboard : Screen("root_dashboard")

    // Admin / Kasir
    object ValidasiTransaksi : Screen("validasi_transaksi")
    object RiwayatTransaksi : Screen("riwayat_transaksi")
    object TransaksiPerUser : Screen("transaksi_per_user")
    object TransaksiHarian : Screen("transaksi_harian")
    object DaftarUser : Screen("daftar_user")

    // Root
    object CrudAdmin : Screen("crud_admin")
    object CrudRuangan : Screen("crud_ruangan")
}