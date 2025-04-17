package com.example.capstoneproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.screens.admin.*
import com.example.capstoneproject.screens.login.AnimatedLoginPage
import com.example.capstoneproject.screens.root.*

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // Login
        composable(Screen.Login.route) {
            AnimatedLoginPage(
                visible = true,
                viewModel = viewModel,
                onLoginSuccess = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // prevents back to login
                    }
                }
            )
        }

        // Dashboards
        composable(Screen.AdminDashboard.route) {
            AdminDashboard(
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) // clear backstack
                    }
                }
            )
        }

        composable(Screen.RootDashboard.route) {
            RootDashboard(
                onNavigate = { navController.navigate(it.route) },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // Admin Pages
        composable(Screen.RiwayatTransaksi.route) {
            RiwayatTransaksiPage(onBack = { navController.popBackStack() })
        }
        composable(Screen.DaftarUser.route) {
            DaftarUserPage(onBack = { navController.popBackStack() })
        }

        // Root Pages
        composable(Screen.CrudAdmin.route) {
            CrudAdminPage(onBack = { navController.popBackStack() })
        }
        composable(Screen.CrudRuangan.route) {
            CrudRuanganPage(onBack = { navController.popBackStack() })
        }
    }
}