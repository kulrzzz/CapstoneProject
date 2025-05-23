package com.example.capstoneproject.screens.login

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneproject.R
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun AnimatedLoginPage(
    visible: Boolean,
    loginViewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (Screen) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(initialScale = 0.8f, animationSpec = tween(600, delayMillis = 200)) +
                fadeIn(animationSpec = tween(600, delayMillis = 200)),
        exit = scaleOut(targetScale = 1.1f, animationSpec = tween(400)) +
                fadeOut(animationSpec = tween(400))
    ) {
        LoginPage(loginViewModel, onLoginSuccess)
    }
}

@Composable
fun LoginPage(
    loginViewModel: LoginViewModel,
    onLoginSuccess: (Screen) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val gradient = Brush.verticalGradient(
        listOf(Color(0xFFB3E5FC), Color(0xFFFFF3E0))
    )

    val loginError by loginViewModel.loginError
    val isAuthenticating by loginViewModel.isAuthenticating

    // Clear error saat mengetik ulang
    LaunchedEffect(email, password) {
        loginViewModel.clearLoginState()
    }

    // ✅ Navigasi hanya saat token dan role valid
    LaunchedEffect(loginViewModel.token, loginViewModel.userRole) {
        val token = loginViewModel.token
        val role = loginViewModel.userRole
        if (!token.isNullOrBlank() && !role.isNullOrBlank()) {
            onLoginSuccess(Screen.Dashboard)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.reservin3),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(56.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                placeholder = { Text("Email", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                },
                modifier = Modifier
                    .width(500.dp)
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                placeholder = { Text("Password", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .width(500.dp)
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (email.isBlank() || password.isBlank()) {
                            loginViewModel.clearLoginState()
                        } else {
                            loginViewModel.login(email, password) { /* handled by effect */ }
                        }
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFF8008), Color(0xFFFF4D00))
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isAuthenticating) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                        )
                    }
                }
            }

            loginError?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
        }
    }
}