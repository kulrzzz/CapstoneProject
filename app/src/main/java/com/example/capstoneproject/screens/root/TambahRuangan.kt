package com.example.capstoneproject.screens.root

import android.app.TimePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.capstoneproject.model.Facility
import com.example.capstoneproject.model.Room
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.viewmodel.RuanganViewModel
import java.io.File
import java.util.*

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
    onLogout: () -> Unit = {},
    viewModel: RuanganViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var namaRuangan by remember { mutableStateOf("") }
    var deskripsiRuangan by remember { mutableStateOf("") }
    var kategoriRuangan by remember { mutableStateOf("") }
    var kapasitas by remember { mutableStateOf("") }
    var hargaSewa by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var fasilitasInput by remember { mutableStateOf("") }
    val fasilitasList = remember { mutableStateListOf<String>() }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value = uri
    }

    val categories = listOf("Meeting", "Auditorium", "Kelas", "Lab")
    val calendar = Calendar.getInstance()

    val timePickerDialogStart = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int -> startTime = String.format("%02d:%02d", hour, minute) },
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    val timePickerDialogEnd = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int -> endTime = String.format("%02d:%02d", hour, minute) },
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FF))) {
        SideBar(userRole = "root", onNavigate = onNavigate, onLogout = onLogout)

        Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(24.dp)) {
            Text("Tambah Ruangan", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF04A5D4))
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp).verticalScroll(scrollState)) {
                    CustomTextField(
                        label = "Nama Ruangan",
                        value = namaRuangan,
                        onValueChange = { namaRuangan = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(
                        label = "Deskripsi Ruangan",
                        value = deskripsiRuangan,
                        onValueChange = { deskripsiRuangan = it },
                        singleLine = false,
                        minHeight = 100.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomDropdownField(
                        label = "Kategori Ruangan",
                        selectedValue = kategoriRuangan,
                        onValueSelected = { kategoriRuangan = it },
                        items = categories
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(
                        label = "Kapasitas",
                        value = kapasitas,
                        onValueChange = { kapasitas = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Jam Mulai: ${if (startTime.isNotEmpty()) startTime else "Belum dipilih"}")
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { timePickerDialogStart.show() }) { Text("Pilih Jam Mulai") }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Jam Selesai: ${if (endTime.isNotEmpty()) endTime else "Belum dipilih"}")
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { timePickerDialogEnd.show() }) { Text("Pilih Jam Selesai") }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(
                        label = "Harga Sewa",
                        value = hargaSewa,
                        onValueChange = { hargaSewa = it }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Upload Gambar", fontSize = 14.sp, color = Color.Gray)
                    Button(onClick = { launcher.launch("image/*") }) { Text("Pilih Gambar") }
                    imageUri.value?.let { uri ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Fasilitas", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = fasilitasInput,
                            onValueChange = { fasilitasInput = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Masukkan Fasilitas") },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            if (fasilitasInput.isNotBlank()) {
                                fasilitasList.add(fasilitasInput)
                                fasilitasInput = ""
                            }
                        }) { Text("Tambahkan") }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    fasilitasList.forEach { fasilitas -> Text("- $fasilitas", fontSize = 14.sp, color = Color.DarkGray) }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val uri = imageUri.value
                            if (uri == null) {
                                Toast.makeText(context, "Harap pilih gambar", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                            tempFile.outputStream().use { output -> inputStream?.copyTo(output) }

                            val room = Room(
                                room_id = "",
                                room_name = namaRuangan,
                                room_desc = deskripsiRuangan,
                                room_kategori = kategoriRuangan,
                                room_capacity = kapasitas.toIntOrNull() ?: 0,
                                room_price = hargaSewa.toLongOrNull() ?: 0L,
                                room_available = 1,
                                room_start = startTime,
                                room_end = endTime,
                                created_at = null,
                                updated_at = null
                            )

                            viewModel.addFullRoom(room, tempFile, fasilitasList) { success ->
                                if (success) {
                                    Toast.makeText(context, "Ruangan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Gagal menambahkan ruangan", Toast.LENGTH_SHORT).show()
                                }
                            }
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