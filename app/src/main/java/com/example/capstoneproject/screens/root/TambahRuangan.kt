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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.capstoneproject.model.facility.FacilityCreateRequest
import com.example.capstoneproject.model.room.Room
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.viewmodel.RoomViewModel
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
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
            shape = RoundedCornerShape(24.dp),
            textStyle = TextStyle(fontSize = 16.sp),
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
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                modifier = Modifier.menuAnchor().fillMaxWidth(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahRuanganPage(
    userRole: String?,
    onBack: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: RoomViewModel
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

    val calendar = Calendar.getInstance()
    val timePickerDialogStart = TimePickerDialog(
        context,
        { _, hour, minute -> startTime = "%02d:%02d".format(hour, minute) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    val timePickerDialogEnd = TimePickerDialog(
        context,
        { _, hour, minute -> endTime = "%02d:%02d".format(hour, minute) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    val kategoriList = listOf("Meeting", "Auditorium", "Kelas", "Lab")

    Row(Modifier.fillMaxSize().background(Color(0xFFF5F7FF))) {
        SideBar(userRole = userRole, onNavigate = onNavigate, onLogout = onLogout)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            Text("Tambah Ruangan", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF04A5D4))
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).verticalScroll(scrollState)
                ) {
                    CustomTextField("Nama Ruangan", namaRuangan, { namaRuangan = it })
                    Spacer(Modifier.height(16.dp))
                    CustomTextField("Deskripsi Ruangan", deskripsiRuangan, { deskripsiRuangan = it }, false, 100.dp)
                    Spacer(Modifier.height(16.dp))
                    CustomDropdownField("Kategori Ruangan", kategoriRuangan, { kategoriRuangan = it }, kategoriList)
                    Spacer(Modifier.height(16.dp))
                    CustomTextField("Kapasitas", kapasitas, { kapasitas = it })
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Jam Mulai: ${startTime.ifEmpty { "Belum dipilih" }}")
                        Spacer(Modifier.width(16.dp))
                        Button(onClick = { timePickerDialogStart.show() }) { Text("Pilih Jam Mulai") }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Jam Selesai: ${endTime.ifEmpty { "Belum dipilih" }}")
                        Spacer(Modifier.width(16.dp))
                        Button(onClick = { timePickerDialogEnd.show() }) { Text("Pilih Jam Selesai") }
                    }
                    Spacer(Modifier.height(16.dp))
                    CustomTextField("Harga Sewa", hargaSewa, { hargaSewa = it })
                    Spacer(Modifier.height(24.dp))

                    Text("Upload Gambar", fontSize = 14.sp, color = Color.Gray)
                    Button(onClick = { launcher.launch("image/*") }) { Text("Pilih Gambar") }
                    imageUri.value?.let {
                        Spacer(Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Text("Fasilitas", fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = fasilitasInput,
                            onValueChange = { fasilitasInput = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Masukkan Fasilitas") },
                            singleLine = true
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            if (fasilitasInput.isNotBlank()) {
                                fasilitasList.add(fasilitasInput.trim())
                                fasilitasInput = ""
                            }
                        }) { Text("Tambahkan") }
                    }
                    Spacer(Modifier.height(8.dp))
                    fasilitasList.forEach { Text("- $it", fontSize = 14.sp, color = Color.DarkGray) }
                    Spacer(Modifier.height(16.dp))

                    viewModel.errorMessage.value?.let {
                        Text(it, color = Color.Red, fontSize = 14.sp)
                    }
                    viewModel.successMessage.value?.let {
                        Text(it, color = Color(0xFF16A34A), fontSize = 14.sp)
                    }
                    if (viewModel.isLoading.value) {
                        Spacer(Modifier.height(8.dp))
                        CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                    }

                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.clearMessages()
                            if (!validateForm(context, namaRuangan, deskripsiRuangan, kategoriRuangan, kapasitas, hargaSewa, startTime, endTime, fasilitasList, imageUri.value))
                                return@Button

                            val room = Room(
                                room_id = "", room_name = namaRuangan, room_desc = deskripsiRuangan,
                                room_kategori = kategoriRuangan, room_capacity = kapasitas.toIntOrNull() ?: 0,
                                room_price = hargaSewa.toLongOrNull() ?: 0L, room_available = 1,
                                room_start = startTime, room_end = endTime, created_at = "", updated_at = ""
                            )

                            viewModel.addRoomWithImageAndFacilities(
                                room = room,
                                imageUri = imageUri.value,
                                context = context,
                                fasilitasList = fasilitasList
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "Ruangan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                    namaRuangan = ""; deskripsiRuangan = ""; kategoriRuangan = ""; kapasitas = ""
                                    hargaSewa = ""; startTime = ""; endTime = ""; fasilitasInput = ""
                                    fasilitasList.clear(); imageUri.value = null
                                } else {
                                    Toast.makeText(context, "Gagal menambahkan ruangan", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1570EF)),
                        enabled = !viewModel.isLoading.value
                    ) {
                        Text("Tambah Ruangan", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

fun validateForm(
    context: android.content.Context,
    nama: String,
    deskripsi: String,
    kategori: String,
    kapasitas: String,
    harga: String,
    mulai: String,
    selesai: String,
    fasilitas: List<String>,
    image: Uri?
): Boolean {
    if (nama.isBlank() || deskripsi.isBlank() || kategori.isBlank() ||
        kapasitas.isBlank() || harga.isBlank() || mulai.isBlank() || selesai.isBlank()
    ) {
        Toast.makeText(context, "Harap lengkapi semua kolom", Toast.LENGTH_SHORT).show()
        return false
    }
    if (image == null) {
        Toast.makeText(context, "Harap pilih gambar", Toast.LENGTH_SHORT).show()
        return false
    }
    if (fasilitas.isEmpty()) {
        Toast.makeText(context, "Tambahkan setidaknya 1 fasilitas", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}