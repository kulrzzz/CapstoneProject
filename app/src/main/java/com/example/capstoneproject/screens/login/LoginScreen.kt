package com.example.capstoneproject.screens.login

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.R
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        listOf(Color(0xFFB3E5FC), Color(0xFFFFF3E0))
    )

    val fontFamily = MaterialTheme.typography.bodyLarge.fontFamily // Ganti dengan FontFamily jika pakai custom font

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
            // Logo
            Image(
                painter = painterResource(id = R.drawable.reservin3),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(56.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    error = false
                },
                singleLine = true,
                placeholder = { Text("Email", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                },
                modifier = Modifier
                    .width(500.dp) // atau nilai yang sesuai selera
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    error = false
                },
                singleLine = true,
                placeholder = { Text("Password", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .width(500.dp) // atau nilai yang sesuai selera
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    val screen = viewModel.login(email, password)
                    if (screen != null) {
                        onLoginSuccess(screen)
                    } else {
                        error = true
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues() // penting agar isi tidak padded default
            ) {
                // Isi button dengan Box ber-gradient
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
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                }
            }

            // Error message
            if (error) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Email atau Password salah",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}