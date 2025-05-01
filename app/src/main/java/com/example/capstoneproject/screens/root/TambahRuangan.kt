package com.example.capstoneproject.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    singleLine: Boolean = true,
    minHeight: Dp = 64.dp
) {
    Column {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp // Label font size
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(fontSize = 16.sp), // Isi font size
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF0284C7),
                unfocusedBorderColor = Color.Gray,
                containerColor = Color.White
            ),
            singleLine = singleLine
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
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .exposedDropdownSize()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, fontSize = 16.sp) },
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
fun TambahRuangan() {
    val backgroundColor = colorResource(id = R.color.soft_indigo)

    var namaRuangan by remember { mutableStateOf("") }
    var kategoriRuangan by remember { mutableStateOf("") }
    var kapasitas by remember { mutableStateOf("") }
    var fasilitas by remember { mutableStateOf("") }
    var hargaSewa by remember { mutableStateOf("") }
    var ukuranRuangan by remember { mutableStateOf("") }
    var deskripsiRuangan by remember { mutableStateOf("") }

    val categories = listOf("Lantai 1", "Lantai 2", "Lantai 3")

    Column(
        modifier = Modifier
            .width(762.dp)
            .height(768.dp)
            .background(backgroundColor)
            .padding(24.dp)
    ) {
        Text(
            text = "Tambah Ruangan",
            color = Color(0xFF0284C7),
            fontSize = 24.sp // Judul font size
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CustomTextField(label = "Nama Ruangan", value = namaRuangan, onValueChange = { namaRuangan = it })
            CustomDropdownField(
                label = "Kategori Ruangan",
                selectedValue = kategoriRuangan,
                onValueSelected = { kategoriRuangan = it },
                items = categories,
            )
            CustomTextField(label = "Kapasitas", value = kapasitas, onValueChange = { kapasitas = it })
            CustomTextField(label = "Fasilitas", value = fasilitas, onValueChange = { fasilitas = it })
            CustomTextField(label = "Harga Sewa", value = hargaSewa, onValueChange = { hargaSewa = it })
            CustomTextField(label = "Ukuran Ruangan", value = ukuranRuangan, onValueChange = { ukuranRuangan = it })
            CustomTextField(
                label = "Deskripsi Ruangan",
                value = deskripsiRuangan,
                onValueChange = { deskripsiRuangan = it },
                singleLine = false,
                minHeight = 100.dp
            )

            Text(
                text = "Upload Foto Ruangan",
                color = Color.Gray,
                fontSize = 14.sp // Label font size
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📁 Pilih File", color = Color.Gray, fontSize = 14.sp)
            }

            Button(
                onClick = { /* TODO: Action */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("➕ Tambahkan", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 762, heightDp = 768)
@Composable
fun TambahRuanganPreview() {
    TambahRuangan()
}
