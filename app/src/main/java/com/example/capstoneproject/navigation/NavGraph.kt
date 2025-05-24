package com.example.capstoneproject.navigation

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneproject.viewmodel.*
import com.example.capstoneproject.screens.admin.*
import com.example.capstoneproject.screens.dashboard.DashboardScreen
import com.example.capstoneproject.screens.login.AnimatedLoginPage
import com.example.capstoneproject.screens.root.*
import com.example.capstoneproject.viewmodel.RoomViewModelFactory

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    bookingViewModel: BookingViewModel,
    customerViewModel: CustomerViewModel
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
                    if (
                        loginViewModel.token != null &&
                        loginViewModel.userRole != null &&
                        loginViewModel.admin != null
                    ) {
                        mainViewModel.setLoggedIn(true)
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
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
                onNavigate = { navController.navigate(it.route) },
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
                onUserSelected = { userId -> navController.navigate("detail_user/$userId") },
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // 📄 Detail User
        composable("detail_user/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")

            LaunchedEffect(Unit) {
                if (customerViewModel.customerList.isEmpty()) customerViewModel.fetchAllCustomers()
                if (bookingViewModel.allBookings.isEmpty()) bookingViewModel.fetchAllBookings()
            }

            val customer = customerViewModel.customerList.find { it.customer_id == userId }
            val bookings = bookingViewModel.allBookings.filter { it.customer_id == userId }

            customer?.let {
                DetailUserPage(
                    customer = it,
                    bookingList = bookings,
                    onBackClick = { navController.popBackStack() },
                    onNavigate = { navController.navigate(it.route) },
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

            LaunchedEffect(loginViewModel.token) {
                loginViewModel.token?.let { token ->
                    adminViewModel.fetchAdmins(token)
                }
            }

            ManajemenAdminPage(
                adminRequestList = adminViewModel.adminList.filter { it.admin_who == 1 },
                isLoading = adminViewModel.isLoading.value,
                errorMessage = adminViewModel.errorMessage.value,
                onTambahAdminClick = { navController.navigate(Screen.TambahAdmin.route) },
                onEditAdmin = { admin -> navController.navigate("edit_admin/${admin.admin_id}") },
                onDeleteAdmin = { admin ->
                    loginViewModel.token?.let { token ->
                        adminViewModel.deleteAdmin(admin.admin_id ?: "", token) { success ->
                            Toast.makeText(
                                context,
                                if (success) "Admin berhasil dihapus" else "Gagal menghapus admin",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                userRole = loginViewModel.userRole,
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // ➕ Tambah Admin
        composable(Screen.TambahAdmin.route) {
            TambahAdminPage(
                userRole = loginViewModel.userRole,
                token = loginViewModel.token,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // ✏️ Edit Admin
        composable("edit_admin/{adminId}") { backStackEntry ->
            val adminId = backStackEntry.arguments?.getString("adminId")
            val admin = adminViewModel.adminList.find { it.admin_id == adminId }

            admin?.let {
                EditAdminPage(
                    admin = it,
                    token = loginViewModel.token,
                    onBack = { navController.popBackStack() },
                    adminViewModel = adminViewModel,
                    userRole = loginViewModel.userRole,
                    onNavigate = { navController.navigate(it.route) },
                    onLogout = {
                        loginViewModel.clearLoginState()
                        mainViewModel.logout()
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    }
                )
            }
        }

        // 🏢 Manajemen Ruangan
        composable(Screen.ManajemenRuangan.route) {
            val context = LocalContext.current

            val roomViewModel: RoomViewModel = viewModel(
                factory = RoomViewModelFactory(loginViewModel.token ?: "")
            )

            LaunchedEffect(Unit) {
                roomViewModel.fetchRooms()
            }

            ManajemenRuanganPage(
                roomList = roomViewModel.roomList,
                onTambahRuanganClick = { navController.navigate(Screen.TambahRuangan.route) },
                onEditRoom = { room ->
                    Toast.makeText(context, "Fitur edit belum tersedia", Toast.LENGTH_SHORT).show()
                },
                onDeleteRoom = { room ->
                    roomViewModel.deleteRoomById(room.room_id) { success ->
                        Toast.makeText(
                            context,
                            if (success) "Ruangan berhasil dihapus" else "Gagal menghapus ruangan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onToggleAvailability = { room, isAvailable ->
                    roomViewModel.toggleRoomAvailability(room, isAvailable) { success ->
                        Toast.makeText(
                            context,
                            if (success) "Status ruangan diperbarui" else "Gagal mengubah status ruangan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                userRole = loginViewModel.userRole,
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // ➕ Tambah Ruangan
        composable(Screen.TambahRuangan.route) {
            val token = loginViewModel.token
            if (token != null) {
                val roomViewModel: RoomViewModel = viewModel(
                    factory = RoomViewModelFactory(token)
                )

                TambahRuanganPage(
                    userRole = loginViewModel.userRole,
                    viewModel = roomViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    onLogout = {
                        loginViewModel.clearLoginState()
                        mainViewModel.logout()
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            }
        }
    }
}