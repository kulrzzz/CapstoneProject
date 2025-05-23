package com.example.capstoneproject.screens.root

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneproject.model.admin.AdminCreateRequest
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.viewmodel.AdminViewModel

@Composable
fun TambahAdminPage(
    userRole: String?,
    onBack: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {},
    adminViewModel: AdminViewModel = viewModel(),
    token: String? = null
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val titleSize = 30.sp

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FF))
    ) {
        SideBar(
            userRole = userRole,
            onNavigate = onNavigate,
            onLogout = onLogout
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Tambah Admin",
                fontSize = titleSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF04A5D4)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    AdminInputField("Nama Lengkap", fullName, onValueChange = { fullName = it })
                    Spacer(modifier = Modifier.height(16.dp))

                    AdminInputField("Email", email, onValueChange = { email = it })
                    Spacer(modifier = Modifier.height(16.dp))

                    AdminInputField("Password", password, onValueChange = { password = it }, isPassword = true)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Kembali", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(300.dp))

                        Button(
                            onClick = {
                                if (token.isNullOrEmpty()) {
                                    Toast.makeText(context, "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val request = AdminCreateRequest(
                                    access_token = token,
                                    admin_fullname = fullName,
                                    admin_email = email,
                                    admin_pass = password,
                                    admin_who = 1
                                )

                                isLoading = true
                                adminViewModel.createAdmin(request) { success ->
                                    isLoading = false
                                    if (success) {
                                        Toast.makeText(context, "Admin berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                        onBack()
                                    } else {
                                        Toast.makeText(context, "Gagal menambahkan admin", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1570EF)),
                            modifier = Modifier.weight(1f).height(50.dp),
                            enabled = !isLoading
                        ) {
                            Text(
                                if (isLoading) "Menambahkan..." else "Tambah Admin",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        shape = RoundedCornerShape(24.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray,
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}