package com.example.capstoneproject.navigation

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.viewmodel.*
import com.example.capstoneproject.screens.admin.*
import com.example.capstoneproject.screens.dashboard.DashboardScreen
import com.example.capstoneproject.screens.login.AnimatedLoginPage
import com.example.capstoneproject.screens.superadmin.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    adminViewModel: AdminViewModel,
    bookingViewModel: BookingViewModel
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            AnimatedLoginPage(
                visible = true,
                loginViewModel = loginViewModel,
                onLoginSuccess = { screen ->
                    val role = loginViewModel.userRole
                    println("ROLE AT NAVIGATION: $role")

                    if (
                        loginViewModel.token != null &&
                        (role == "superadmin" || role == "admin") &&
                        loginViewModel.admin != null
                    ) {
                        mainViewModel.setLoggedIn(true)
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        println("🚨 Navigasi ditunda karena role belum sesuai")
                    }
                }
            )
        }

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
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable(Screen.RiwayatTransaksi.route) {
            LaunchedEffect(Unit) {
                bookingViewModel.fetchAllBookings()
            }
            val transaksiList = bookingViewModel.allBookings ?: emptyList()

            RiwayatTransaksiPage(
                transaksiList = transaksiList,
                userRole = loginViewModel.userRole,
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable(Screen.DaftarUser.route) {
            val customerViewModel: CustomerViewModel = hiltViewModel()

            DaftarUserPage(
                viewModel = customerViewModel,
                onUserSelected = { userId ->
                    navController.navigate("detail_user/$userId")
                },
                userRole = loginViewModel.userRole,
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                },
            )
        }

        composable("detail_user/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val customerViewModel: CustomerViewModel = hiltViewModel()

            if (userId != null) {
                LaunchedEffect(Unit) {
                    if (customerViewModel.customerList.isEmpty()) {
                        customerViewModel.fetchCustomers(BuildConfig.API_ACCESS_TOKEN)
                    }
                    if (bookingViewModel.allBookings.isEmpty()) bookingViewModel.fetchAllBookings()
                }

                val customer = customerViewModel.customerList.find { it.customer_id == userId }
                val bookings = bookingViewModel.getBookingsByCustomerId(userId)

                if (customer != null) {
                    DetailUserPage(
                        customer = customer,
                        bookingList = bookings,
                        onBackClick = { navController.popBackStack() },
                        userRole = loginViewModel.userRole,
                        onNavigate = { navController.navigate(it.route) },
                        onLogout = {
                            loginViewModel.clearLoginState()
                            mainViewModel.logout()
                            navController.navigate(Screen.Login.route) { popUpTo(0) }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                        Toast.makeText(context, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.DaftarUser.route) {
                        popUpTo(Screen.DaftarUser.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.ManajemenAdmin.route) {
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

        composable(Screen.TambahAdmin.route) {
            TambahAdminPage(
                token = loginViewModel.token,
                onBack = { navController.popBackStack() },
                userRole = loginViewModel.userRole,
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    loginViewModel.clearLoginState()
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable("edit_admin/{adminId}") { backStackEntry ->
            val adminId = backStackEntry.arguments?.getString("adminId")
            if (adminId != null) {
                val admin = adminViewModel.adminList.find { it.admin_id == adminId }

                if (admin != null) {
                    EditAdminPage(
                        admin = admin,
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
                } else {
                    Toast.makeText(context, "Data admin tidak ditemukan", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            } else {
                navController.navigate(Screen.ManajemenAdmin.route) {
                    popUpTo(Screen.ManajemenAdmin.route) { inclusive = true }
                }
            }
        }

        composable(Screen.ManajemenRuangan.route) {
            val token = loginViewModel.token ?: ""
            val roomViewModel: RoomViewModel = viewModel(factory = RoomViewModelFactory(token))

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

        composable(Screen.TambahRuangan.route) {
            val token = loginViewModel.token ?: ""
            val roomViewModel: RoomViewModel = viewModel(factory = RoomViewModelFactory(token))

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
        }
    }
}