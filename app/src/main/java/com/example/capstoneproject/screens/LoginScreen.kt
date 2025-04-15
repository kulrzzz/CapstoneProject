package com.example.capstoneproject.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.navigation.Screen

@Composable
fun AnimatedLoginPage(
    visible: Boolean,
    viewModel: MainViewModel,
    onLoginSuccess: (Screen) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = 0.8f,
            animationSpec = tween(durationMillis = 600, delayMillis = 200)
        ) + fadeIn(animationSpec = tween(600, delayMillis = 200)),
        exit = scaleOut(
            targetScale = 1.1f,
            animationSpec = tween(durationMillis = 400)
        ) + fadeOut(animationSpec = tween(400))
    ) {
        LoginPage(viewModel, onLoginSuccess)
    }
}

@Composable
fun LoginPage(viewModel: MainViewModel, onLoginSuccess: (Screen) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                error = false
            },
            label = { Text("Username") },
            isError = error
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = false
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = error
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val screen = viewModel.login(username, password)
            if (screen != null) {
                onLoginSuccess(screen)
            } else {
                error = true
            }
        }) {
            Text("Login")
        }

        if (error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Username atau Password salah", color = MaterialTheme.colorScheme.error)
        }
    }
}