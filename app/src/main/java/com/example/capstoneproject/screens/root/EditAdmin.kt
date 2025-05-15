package com.example.capstoneproject.screens.root

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneproject.model.Admin
import com.example.capstoneproject.viewmodel.AdminViewModel

@Composable
fun EditAdminPage(
    admin: Admin,
    onBack: () -> Unit,
    adminViewModel: AdminViewModel = viewModel()
) {
    val context = LocalContext.current

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFFF5F7FF)),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Edit Admin", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Nama Lengkap", fontSize = 14.sp, color = Color.Gray)
        TextField(
            value = admin.admin_fullname ?: "",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Email", fontSize = 14.sp, color = Color.Gray)
        TextField(
            value = admin.admin_email ?: "",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Password Baru", fontSize = 14.sp, color = Color.Gray)
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Konfirmasi Password", fontSize = 14.sp, color = Color.Gray)
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
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
                    if (newPassword.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    adminViewModel.updateAdminPassword(admin.admin_id!!, newPassword) { success ->
                        isLoading = false
                        if (success) {
                            Toast.makeText(context, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            onBack()
                        } else {
                            Toast.makeText(context, "Gagal memperbarui password", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Menyimpan..." else "Simpan")
            }
        }
    }
}