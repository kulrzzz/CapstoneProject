package com.example.capstoneproject.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneproject.viewmodel.AdminViewModel
import com.example.capstoneproject.viewmodel.LoginViewModel
import com.example.capstoneproject.viewmodel.MainViewModel
import com.example.capstoneproject.viewmodel.RuanganViewModel
import com.example.capstoneproject.screens.admin.DaftarUserPage
import com.example.capstoneproject.screens.admin.RiwayatTransaksiPage
import com.example.capstoneproject.screens.dashboard.DashboardScreen
import com.example.capstoneproject.screens.login.AnimatedLoginPage
import com.example.capstoneproject.screens.root.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    ruanganViewModel: RuanganViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 🔐 Login
        composable(Screen.Login.route) {
            AnimatedLoginPage(
                visible = true,
                loginViewModel = loginViewModel,
                onLoginSuccess = { screen ->
                    mainViewModel.setLoggedIn(true)
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🧭 Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                userRole = loginViewModel.userRole,
                onNavigate = { target ->
                    navController.navigate(target.route) {
                        popUpTo(Screen.Dashboard.route)
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // 🧾 Riwayat dan Daftar User
//        composable(Screen.RiwayatTransaksi.route) {
//            RiwayatTransaksiPage(onBack = { navController.popBackStack() })
//        }

//        composable(Screen.DaftarUser.route) {
//            DaftarUserPage(onBack = { navController.popBackStack() })
//        }

        // 🛠️ Manajemen Admin
        composable(Screen.ManajemenAdmin.route) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                adminViewModel.fetchAdmins()
            }

            ManajemenAdminPage(
                adminRequestList = adminViewModel.adminList.filter { it.admin_who == 1 },
                onTambahAdminClick = {
                    navController.navigate(Screen.TambahAdmin.route)
                },
                onEditAdmin = { admin ->
                    navController.navigate("edit_admin/${admin.admin_id}")
                },
                onDeleteAdmin = { admin ->
                    adminViewModel.deleteAdmin(admin.admin_id ?: "") { success ->
                        Toast.makeText(
                            context,
                            if (success) "Admin berhasil dihapus" else "Gagal menghapus admin",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ➕ Tambah Admin
        composable(Screen.TambahAdmin.route) {
            TambahAdminPage(onBack = { navController.popBackStack() })
        }

        // ✏️ Edit Admin
        composable("edit_admin/{adminId}") { backStackEntry ->
            val adminId = backStackEntry.arguments?.getString("adminId")
            val admin = adminViewModel.adminList.find { it.admin_id == adminId }

            if (admin != null) {
                EditAdminPage(
                    admin = admin,
                    onBack = { navController.popBackStack() },
                    adminViewModel = adminViewModel
                )
            }
        }

        // 🏢 Manajemen Ruangan
        composable(Screen.ManajemenRuangan.route) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                ruanganViewModel.fetchRooms()
            }

            ManajemenRuanganPage(
                onTambahRuanganClick = {
                    navController.navigate(Screen.TambahRuangan.route)
                },
                onNavigate = { screen ->
                    navController.navigate(screen.route) { launchSingleTop = true }
                },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                },
                roomList = ruanganViewModel.roomList,
                onEditRoom = { room ->
                    // Navigasi ke edit_room bisa ditambahkan di sini nanti
                },
                onDeleteRoom = { room ->
                    ruanganViewModel.deleteRoomById(room.room_id) { success ->
                        Toast.makeText(
                            context,
                            if (success) "Ruangan berhasil dihapus" else "Gagal menghapus ruangan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        composable(Screen.TambahRuangan.route) {
            TambahRuanganPage(onBack = { navController.popBackStack() })
        }
    }
}