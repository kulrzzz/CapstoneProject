package com.example.capstoneproject.screens.root

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneproject.model.admin.Admin
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.viewmodel.AdminViewModel

@Composable
fun EditAdminPage(
    userRole: String?,
    admin: Admin,
    token: String?,
    onBack: () -> Unit,
    adminViewModel: AdminViewModel = viewModel(),
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F7FF))
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
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Edit Admin",
                fontSize = 30.sp,
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
                    AdminInputField(
                        label = "Nama Lengkap",
                        value = admin.admin_fullname ?: "",
                        onValueChange = {},
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminInputField(
                        label = "Email",
                        value = admin.admin_email ?: "",
                        onValueChange = {},
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminInputField(
                        label = "Password Baru",
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AdminInputField(
                        label = "Konfirmasi Password",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Kembali")
                        }

                        Button(
                            onClick = {
                                if (token.isNullOrEmpty()) {
                                    Toast.makeText(context, "Token tidak tersedia", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                when {
                                    newPassword.isBlank() || confirmPassword.isBlank() -> {
                                        Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                    }
                                    newPassword != confirmPassword -> {
                                        Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        isLoading = true
                                        adminViewModel.updateAdminPassword(
                                            adminId = admin.admin_id ?: "",
                                            newPassword = newPassword,
                                            token = token
                                        ) { success ->
                                            isLoading = false
                                            if (success) {
                                                Toast.makeText(context, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                                onBack()
                                            } else {
                                                Toast.makeText(context, "Gagal memperbarui password", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1570EF))
                        ) {
                            Text(if (isLoading) "Menyimpan..." else "Simpan", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    val containerColor = if (enabled) Color.White else Color(0xFFF3F4F6)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Gray,
            disabledLabelColor = Color.Gray,
            disabledBorderColor = Color.LightGray
        )
    )
}