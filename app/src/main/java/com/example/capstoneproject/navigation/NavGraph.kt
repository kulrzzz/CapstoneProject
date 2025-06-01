package com.example.capstoneproject.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.CircularProgressIndicator

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
            val token = loginViewModel.token
            val transaksiList = bookingViewModel.allBookings
            val isLoading = bookingViewModel.isLoading

            LaunchedEffect(token) {
                if (!token.isNullOrBlank()) {
                    bookingViewModel.fetchRiwayatTransaksi(token)
                } else {
                    bookingViewModel.setError("Token tidak tersedia. Silakan login ulang.")
                }
            }

            RiwayatTransaksiPage(
                transaksiList = transaksiList,
                isLoading = isLoading,
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
            val context = LocalContext.current

            val customerViewModel: CustomerViewModel = hiltViewModel()
            val selectedCustomer by customerViewModel.selectedCustomer
            val isCustomerLoading by customerViewModel.isLoading
            val customerError by customerViewModel.errorMessage

            val bookingList by bookingViewModel.userBookings
            val isBookingLoading by remember { derivedStateOf { bookingViewModel.isLoading } }
            val bookingError by remember { derivedStateOf { bookingViewModel.errorMessage } }

//            var isDataLoaded by remember { mutableStateOf(false) }

            // Fetch data once
            LaunchedEffect(userId) {
                if (!userId.isNullOrEmpty()) {
                    println("📡 Memulai fetch data untuk userId: $userId")
                    customerViewModel.fetchCustomerById(userId, BuildConfig.API_ACCESS_TOKEN)
                    bookingViewModel.fetchBookingsByCustomerId(userId, BuildConfig.API_ACCESS_TOKEN)
                }
            }

            // Redirect jika userId kosong
            if (userId.isNullOrEmpty()) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.DaftarUser.route) {
                        popUpTo(Screen.DaftarUser.route) { inclusive = true }
                    }
                }
                return@composable
            }

            // Loading state
            if (isCustomerLoading || isBookingLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@composable
            }

            // Error handling yang aman (tidak crash jika pesan kosong/null)
            val finalErrorMessage = customerError?.takeIf { it.isNotBlank() }
                ?: bookingError?.takeIf { it.isNotBlank() }

            if (finalErrorMessage != null) {
                LaunchedEffect("error-toast") {
                    Toast.makeText(
                        context,
                        finalErrorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
                return@composable
            }

            // Menampilkan halaman detail user
            selectedCustomer?.let { customer ->
                DetailUserPage(
                    customer = customer,
                    bookingList = bookingList,
                    onBackClick = { navController.popBackStack() },
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
                isLoading = roomViewModel.isLoading.value,
                onTambahRuanganClick = { navController.navigate(Screen.TambahRuangan.route) },
                onEditRoom = { room ->
                    navController.navigate("edit_room/${room.room_id}")
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

        composable("edit_room/{roomId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: return@composable
            val token = loginViewModel.token ?: ""
            val roomViewModel: RoomViewModel = viewModel(factory = RoomViewModelFactory(token))

            EditRuanganPage(
                roomId = roomId,
                roomViewModel = roomViewModel,
                navController = navController,
                userRole = loginViewModel.userRole,
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