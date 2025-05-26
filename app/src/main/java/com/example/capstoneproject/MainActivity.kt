package com.example.capstoneproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.capstoneproject.navigation.AppNavGraph
import com.example.capstoneproject.ui.theme.CapstoneProjectTheme
import com.example.capstoneproject.viewmodel.AdminViewModel
import com.example.capstoneproject.viewmodel.BookingViewModel
import com.example.capstoneproject.viewmodel.LoginViewModel
import com.example.capstoneproject.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil MainViewModel menggunakan ViewModel factory milik Activity
        val mainViewModel: MainViewModel by viewModels()

        setContent {
            // Buat NavController
            val navController = rememberNavController()

            // ViewModel lain diakses dari scope Compose
            val loginViewModel: LoginViewModel = viewModel()
            val adminViewModel: AdminViewModel = viewModel()
            val bookingViewModel: BookingViewModel = viewModel()

            // Atur tema dan panggil navigator utama
            CapstoneProjectTheme {
                AppNavGraph(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    loginViewModel = loginViewModel,
                    adminViewModel = adminViewModel,
                    bookingViewModel = bookingViewModel
                )
            }
        }
    }
}