package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    minHeight: Dp = 64.dp
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        label = { Text(text = label, fontSize = 14.sp) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0284C7),
            unfocusedBorderColor = Color.Gray,
            containerColor = MaterialTheme.colorScheme.surface
        ),
        singleLine = singleLine,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun TambahAdminPage(onBack: () -> Unit = {}) {
    val backgroundColor = colorResource(id = R.color.soft_indigo)

    var adminId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var who by remember { mutableStateOf("") }
    var createdAt by remember { mutableStateOf("") }
    var updatedAt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Tombol Back
        Button(onClick = onBack) {
            Text("← Kembali")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tambah Admin",
            color = Color(0xFF0284C7),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CustomTextField(label = "ID Admin", value = adminId, onValueChange = { adminId = it })
            CustomTextField(label = "Nama Lengkap", value = fullName, onValueChange = { fullName = it })
            CustomTextField(label = "Email", value = email, onValueChange = { email = it })
            CustomTextField(label = "Password", value = password, onValueChange = { password = it }, isPassword = true)
            CustomTextField(label = "Admin Who", value = who, onValueChange = { who = it })
            CustomTextField(label = "Created At", value = createdAt, onValueChange = { createdAt = it })
            CustomTextField(label = "Updated At", value = updatedAt, onValueChange = { updatedAt = it })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val whoInt = who.toIntOrNull() ?: -1
                    if (whoInt == -1) {
                        // TODO: tampilkan error validasi jika perlu
                        return@Button
                    }

                    // TODO: Panggil ViewModel atau API untuk menambahkan admin
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("➕ Tambah Admin", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 762, heightDp = 768)
@Composable
fun TambahAdminPreview() {
    TambahAdminPage()
}