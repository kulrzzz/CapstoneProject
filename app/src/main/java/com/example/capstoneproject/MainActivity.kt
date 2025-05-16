package com.example.capstoneproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.capstoneproject.navigation.AppNavGraph
import com.example.capstoneproject.ui.theme.CapstoneProjectTheme
import com.example.capstoneproject.viewmodel.AdminViewModel
import com.example.capstoneproject.viewmodel.LoginViewModel
import com.example.capstoneproject.viewmodel.MainViewModel
import com.example.capstoneproject.viewmodel.RuanganViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android ViewModel scoped to Activity lifecycle (recommended over manual instantiation)
        val mainViewModel: MainViewModel by viewModels()

        setContent {
            val navController = rememberNavController()

            // Compose-aware ViewModel instantiation
            val loginViewModel: LoginViewModel = viewModel()
            val adminViewModel: AdminViewModel = viewModel()
            val ruanganViewModel: RuanganViewModel = viewModel()

            CapstoneProjectTheme {
                AppNavGraph(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    loginViewModel = loginViewModel,
                    adminViewModel = adminViewModel,
                    ruanganViewModel = ruanganViewModel
                )
            }
        }
    }
}