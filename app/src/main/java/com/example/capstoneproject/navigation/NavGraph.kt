package com.example.capstoneproject.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneproject.viewmodel.*
import com.example.capstoneproject.screens.admin.*
import com.example.capstoneproject.screens.dashboard.DashboardScreen
import com.example.capstoneproject.screens.login.AnimatedLoginPage
import com.example.capstoneproject.screens.root.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    ruanganViewModel: RuanganViewModel,
    bookingViewModel: BookingViewModel,
    customerViewModel: CustomerViewModel // ✅ Ditambahkan
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

        // 🧾 Riwayat Transaksi
        composable(Screen.RiwayatTransaksi.route) {
            LaunchedEffect(Unit) {
                bookingViewModel.fetchAllBookings()
            }

            RiwayatTransaksiPage(
                transaksiList = bookingViewModel.allBookings,
                onNavigate = { screen ->
                    navController.navigate(screen.route)
                },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // 👥 Daftar User
        composable(Screen.DaftarUser.route) {
            LaunchedEffect(Unit) {
                customerViewModel.fetchAllCustomers()
            }

            DaftarUserPage(
                customerList = customerViewModel.customerList,
                onUserSelected = { userId ->
                    navController.navigate("detail_user/$userId")
                },
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // 📄 Detail User (Turunan dari DaftarUser)
        composable("detail_user/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")

            LaunchedEffect(Unit) {
                if (customerViewModel.customerList.isEmpty()) {
                    customerViewModel.fetchAllCustomers()
                }
                if (bookingViewModel.allBookings.isEmpty()) {
                    bookingViewModel.fetchAllBookings()
                }
            }

            val customer = customerViewModel.customerList.find { it.customer_id == userId }
            val bookings = bookingViewModel.allBookings.filter { it.customer_id == userId }

            if (customer != null) {
                DetailUserPage(
                    customer = customer,
                    bookingList = bookings,
                    onBackClick = { navController.popBackStack() },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    onLogout = {
                        loginViewModel.clearLoginState()
                        mainViewModel.logout()
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    }
                )
            }
        }

        // 🛠️ Manajemen Admin
        composable(Screen.ManajemenAdmin.route) {
            val context = LocalContext.current

            // Jalankan hanya sekali saat composable pertama kali muncul
            LaunchedEffect(Unit) {
                adminViewModel.fetchAdmins()
            }

            ManajemenAdminPage(
                adminRequestList = adminViewModel.adminList.filter { it.admin_who == 1 },
                isLoading = adminViewModel.isLoading.value,
                errorMessage = adminViewModel.errorMessage.value,
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
                    navController.navigate(screen.route) { launchSingleTop = true }
                },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
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
                onEditRoom = { /* future */ },
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

        // ➕ Tambah Ruangan
        composable(Screen.TambahRuangan.route) {
            TambahRuanganPage(onBack = { navController.popBackStack() })
        }
    }
}