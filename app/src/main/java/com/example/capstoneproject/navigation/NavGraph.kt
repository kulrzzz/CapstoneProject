package com.example.capstoneproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.screens.admin.DaftarUserPage
import com.example.capstoneproject.screens.admin.RiwayatTransaksiPage
import com.example.capstoneproject.screens.dashboard.DashboardScreen
import com.example.capstoneproject.screens.login.AnimatedLoginPage
import com.example.capstoneproject.screens.root.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 🔐 Login Page
        composable(Screen.Login.route) {
            AnimatedLoginPage(
                visible = true,
                viewModel = viewModel,
                onLoginSuccess = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🧭 Shared Dashboard Page (Admin + Root)
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                userRole = viewModel.userRole.value,
                onNavigate = { target ->
                    navController.navigate(target.route) {
                        popUpTo(Screen.Dashboard.route)
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // 🧾 Admin + Root Pages
        composable(Screen.RiwayatTransaksi.route) {
            RiwayatTransaksiPage(onBack = { navController.popBackStack() })
        }
        composable(Screen.DaftarUser.route) {
            DaftarUserPage(onBack = { navController.popBackStack() })
        }

        // 🛠️ Root-only Pages
        composable(Screen.ManajemenAdmin.route) {
            ManajemenAdminPage(
                onTambahAdminClick = {
                    navController.navigate(Screen.TambahAdmin.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.TambahAdmin.route) {
            TambahAdminPage(onBack = { navController.popBackStack() })
        }

        composable(Screen.ManajemenRuangan.route) {
            ManajemenRuanganPage(
                onTambahRuanganClick = {
                    navController.navigate(Screen.TambahRuangan.route)
                }
            )
        }

        composable(Screen.TambahRuangan.route) {
            TambahRuanganPage(onBack = { navController.popBackStack() })
        }
    }
}