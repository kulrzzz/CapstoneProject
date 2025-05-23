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
import com.example.capstoneproject.viewmodel.CustomerViewModel
import com.example.capstoneproject.viewmodel.LoginViewModel
import com.example.capstoneproject.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()

        setContent {
            val navController = rememberNavController()

            val loginViewModel: LoginViewModel = viewModel()
            val adminViewModel: AdminViewModel = viewModel()
            val bookingViewModel: BookingViewModel = viewModel()
            val customerViewModel: CustomerViewModel = viewModel()

            // ❌ JANGAN buat RoomViewModel di sini!
            // ✅ RoomViewModel akan dibuat dengan Factory di NavGraph saat butuh saja

            CapstoneProjectTheme {
                AppNavGraph(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    loginViewModel = loginViewModel,
                    adminViewModel = adminViewModel,
                    bookingViewModel = bookingViewModel,
                    customerViewModel = customerViewModel
                )
            }
        }
    }
}