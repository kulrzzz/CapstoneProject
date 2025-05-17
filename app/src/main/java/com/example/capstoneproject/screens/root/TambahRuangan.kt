package com.example.capstoneproject.screens.root

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minHeight: Dp = 64.dp
) {
    Column {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight),
            shape = RoundedCornerShape(24.dp),
            textStyle = TextStyle(fontSize = 16.sp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF0284C7),
                unfocusedBorderColor = Color.Gray,
                containerColor = Color.White
            ),
            singleLine = singleLine,
            visualTransformation = VisualTransformation.None
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    label: String,
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    items: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                readOnly = true,
                textStyle = TextStyle(fontSize = 16.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0284C7),
                    unfocusedBorderColor = Color.Gray,
                    containerColor = Color.White
                ),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontSize = 16.sp) },
                        onClick = {
                            onValueSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TambahRuanganPage(
    onBack: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    var namaRuangan by remember { mutableStateOf("") }
    var kategoriRuangan by remember { mutableStateOf("") }
    var kapasitas by remember { mutableStateOf("") }
    var fasilitas by remember { mutableStateOf("") }
    var hargaSewa by remember { mutableStateOf("") }
    var ukuranRuangan by remember { mutableStateOf("") }
    var deskripsiRuangan by remember { mutableStateOf("") }

    val categories = listOf("Lantai 1", "Lantai 2", "Lantai 3")

    val spacing = 24.dp
    val textSize = 14.sp
    val titleSize = 30.sp

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FF))
    ) {
        SideBar(
            userRole = "root",
            onNavigate = onNavigate,
            onLogout = onLogout
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(spacing)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Tambah Ruangan",
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
                    CustomTextField(
                        label = "Nama Ruangan",
                        value = namaRuangan,
                        onValueChange = { namaRuangan = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CustomDropdownField(
                        label = "Kategori Ruangan",
                        selectedValue = kategoriRuangan,
                        onValueSelected = { kategoriRuangan = it },
                        items = categories
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(label = "Kapasitas", value = kapasitas, onValueChange = { kapasitas = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(label = "Fasilitas", value = fasilitas, onValueChange = { fasilitas = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(label = "Harga Sewa", value = hargaSewa, onValueChange = { hargaSewa = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(label = "Ukuran Ruangan", value = ukuranRuangan, onValueChange = { ukuranRuangan = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(
                        label = "Deskripsi Ruangan",
                        value = deskripsiRuangan,
                        onValueChange = { deskripsiRuangan = it },
                        singleLine = false,
                        minHeight = 100.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Upload Foto Ruangan", fontSize = textSize, color = Color.Gray)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📁 Pilih File", color = Color.Gray, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "Data ruangan berhasil disubmit", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1570EF))
                    ) {
                        Text("Tambah Ruangan", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
fun TambahRuanganPreview() {
    TambahRuanganPage()
}
