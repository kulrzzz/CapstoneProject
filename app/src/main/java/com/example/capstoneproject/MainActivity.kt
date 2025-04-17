package com.example.capstoneproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.capstoneproject.navigation.AppNavGraph
import com.example.capstoneproject.ui.theme.CapstoneProjectTheme // ✅ Tambahkan import ini

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel()

        setContent {
            val navController = rememberNavController()
            CapstoneProjectTheme { // ✅ Gunakan theme custom-mu
                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
