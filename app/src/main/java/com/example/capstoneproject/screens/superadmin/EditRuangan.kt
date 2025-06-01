package com.example.capstoneproject.screens.superadmin

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.capstoneproject.model.facility.FacilityDeleteRequest
import com.example.capstoneproject.model.room.Room
import com.example.capstoneproject.navigation.Screen
import com.example.capstoneproject.screens.sidebar.SideBar
import com.example.capstoneproject.viewmodel.RoomViewModel
import kotlinx.coroutines.launch

@Composable
fun EditRuanganPage(
    roomId: String,
    navController: NavController,
    roomViewModel: RoomViewModel,
    userRole: String? = null,
    onNavigate: (Screen) -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val roomDetail = roomViewModel.roomDetail.value
    val isLoading = roomViewModel.isLoading.value

    var namaRuangan by remember { mutableStateOf("") }
    var deskripsiRuangan by remember { mutableStateOf("") }
    var kategoriRuangan by remember { mutableStateOf("") }
    var kapasitas by remember { mutableStateOf("") }
    var hargaSewa by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var fasilitasInput by remember { mutableStateOf("") }
    val fasilitasList = remember { mutableStateListOf<String>() }
    val fasilitasToDelete = remember { mutableStateListOf<String>() }
    val imageUris = remember { mutableStateListOf<Uri>() }
    val imageToDelete = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }
    var isAvailable by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUris.add(it) }
    }

    val timePickerDialogStart = TimePickerDialog(context, { _, hour, minute ->
        startTime = String.format("%02d:%02d", hour, minute)
    }, 12, 0, true)

    val timePickerDialogEnd = TimePickerDialog(context, { _, hour, minute ->
        endTime = String.format("%02d:%02d", hour, minute)
    }, 12, 0, true)

    val kategoriList = listOf("Meeting", "Lab", "Auditorium", "Class", "Workspace")

    LaunchedEffect(roomId) {
        roomViewModel.fetchRoomDetailById(roomId)
    }

    LaunchedEffect(roomDetail) {
        roomDetail?.let {
            namaRuangan = it.room_name
            deskripsiRuangan = it.room_desc
            kategoriRuangan = it.room_kategori
            kapasitas = it.room_capacity.toString()
            hargaSewa = it.room_price.toString()
            startTime = it.room_start.take(5)
            endTime = it.room_end.take(5)
            isAvailable = it.room_available == 1
            fasilitasList.clear()
            fasilitasList.addAll(it.facility.map { f -> f.facility_name })
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) { Text("OK") }
            },
            title = { Text("Berhasil") },
            text = { Text("Data ruangan berhasil diperbarui.") }
        )
    }

    Row(Modifier.fillMaxSize().background(Color(0xFFF5F7FF))) {
        SideBar(userRole = userRole, onNavigate = onNavigate, onLogout = onLogout)

        Column(Modifier.weight(1f).padding(24.dp).verticalScroll(scrollState)) {
            Text("Edit Ruangan", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF04A5D4))
            Spacer(Modifier.height(16.dp))

            CustomTextField("Nama Ruangan", namaRuangan, { namaRuangan = it })
            CustomTextField("Deskripsi Ruangan", deskripsiRuangan, { deskripsiRuangan = it }, false)
            CustomDropdownField("Kategori Ruangan", kategoriRuangan, { kategoriRuangan = it }, kategoriList)
            CustomTextField("Kapasitas", kapasitas, { kapasitas = it })
            CustomTextField("Harga Sewa", hargaSewa, { hargaSewa = it })

            Spacer(Modifier.height(8.dp))
            Text("Status Ketersediaan")
            Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })

            Spacer(Modifier.height(16.dp))
            Text("Fasilitas", fontSize = 14.sp, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = fasilitasInput,
                    onValueChange = { fasilitasInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Masukkan Fasilitas") },
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

            fasilitasList.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("- $item", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(Modifier.width(4.dp))
                    IconButton(onClick = {
                        fasilitasToDelete.add(roomDetail?.facility?.getOrNull(index)?.facility_id ?: "")
                        fasilitasList.removeAt(index)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Gambar Lama", fontSize = 14.sp, color = Color.Gray)
            roomDetail?.images?.forEach { img ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(img.ri_image),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { imageToDelete.add(img.ri_id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Upload Gambar Baru")
            Button(onClick = { launcher.launch("image/*") }) { Text("Pilih Gambar") }
            imageUris.forEach {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))
            Text("Jam Mulai: $startTime")
            Button(onClick = { timePickerDialogStart.show() }) { Text("Pilih Jam Mulai") }
            Text("Jam Selesai: $endTime")
            Button(onClick = { timePickerDialogEnd.show() }) { Text("Pilih Jam Selesai") }

            Spacer(Modifier.height(24.dp))
            if (isLoading) CircularProgressIndicator()

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Kembali")
                }
                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    if (!validateEditForm(
                            context,
                            namaRuangan,
                            deskripsiRuangan,
                            kategoriRuangan,
                            kapasitas,
                            hargaSewa,
                            startTime,
                            endTime,
                            fasilitasList
                        )) return@Button

                    val room = Room(
                        room_id = roomId,
                        room_name = namaRuangan,
                        room_desc = deskripsiRuangan,
                        room_kategori = kategoriRuangan,
                        room_capacity = kapasitas.toInt(),
                        room_price = hargaSewa.toLong(),
                        room_available = if (isAvailable) 1 else 0,
                        room_start = startTime,
                        room_end = endTime,
                        created_at = "",
                        updated_at = ""
                    )

                    scope.launch {
                        fasilitasToDelete.forEach {
                            if (it.isNotBlank()) roomViewModel.deleteFacilityFromRoom(
                                FacilityDeleteRequest(roomViewModel.accessToken, it)
                            ) {}
                        }
                        imageToDelete.forEach {
                            if (it.isNotBlank()) roomViewModel.deleteRoomImage(it) {}
                        }

                        roomViewModel.updateRoom(room) { success ->
                            if (success) {
                                fasilitasList.forEach {
                                    roomViewModel.addFacilityToRoom(
                                        com.example.capstoneproject.model.facility.FacilityCreateRequest(
                                            room_id = roomId,
                                            access_token = roomViewModel.accessToken,
                                            facility_name = it
                                        )
                                    ) {}
                                }
                                imageUris.forEach {
                                    roomViewModel.addRoomImage(uri = it, roomId = roomId, context = context) {}
                                }
                                showDialog = true
                            }
                        }
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text("Simpan Perubahan")
                }
            }
        }
    }
}

fun validateEditForm(
    context: android.content.Context,
    nama: String,
    deskripsi: String,
    kategori: String,
    kapasitas: String,
    harga: String,
    mulai: String,
    selesai: String,
    fasilitas: List<String>
): Boolean {
    if (nama.isBlank() || deskripsi.isBlank() || kategori.isBlank() ||
        kapasitas.isBlank() || harga.isBlank() || mulai.isBlank() || selesai.isBlank()
    ) {
        Toast.makeText(context, "Harap lengkapi semua kolom", Toast.LENGTH_SHORT).show()
        return false
    }
    if (fasilitas.isEmpty()) {
        Toast.makeText(context, "Tambahkan setidaknya 1 fasilitas", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}