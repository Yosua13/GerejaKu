package com.example.projecttingkat2.ui.screen.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.firebase.BeritaAcaraRepository
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.BeritaAcaraViewModelFactory
import com.example.projecttingkat2.util.GerejaViewModelFactory
import com.example.projecttingkat2.viewmodel.BeritaAcaraDetailViewModel
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

const val KEY_ID_BERITA = "idBerita"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBeritaAcaraScreen(
    navHostController: NavHostController,
    id: String? = null,
) {
    val context = LocalContext.current
    val repository = BeritaAcaraRepository()
    val factory = BeritaAcaraViewModelFactory(repository)
    val viewModel: BeritaAcaraDetailViewModel = viewModel(factory = factory)

    val pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val pickedTime by remember { mutableStateOf(LocalTime.NOON) }

    var judul by rememberSaveable { mutableStateOf("") }
    var namaGereja by rememberSaveable { mutableStateOf("") }
    var pembicara by rememberSaveable { mutableStateOf("") }
    var jadwalIbadah by remember {
        mutableStateOf(DateTimeFormatter.ofPattern("MMM dd yyyy").format(pickedDate))
    }
    var jamIbadah by remember {
        mutableStateOf(DateTimeFormatter.ofPattern("HH:mm").format(pickedTime))
    }
    var deskripsi by rememberSaveable { mutableStateOf("") }
    var gambarBerita by rememberSaveable { mutableStateOf("") }

    var judulError by rememberSaveable { mutableStateOf(false) }
    var namaGerejaError by rememberSaveable { mutableStateOf(false) }
    var pembicaraError by rememberSaveable { mutableStateOf(false) }
    var jadwalIbadahError by rememberSaveable { mutableStateOf(false) }
    var jamIbadahError by rememberSaveable { mutableStateOf(false) }
    var deskripsiError by rememberSaveable { mutableStateOf(false) }
    var gambarBeritaError by rememberSaveable { mutableStateOf(false) }

    val isUploading = remember { mutableStateOf(false) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getBeritaAcaraById(id)
        data?.let {
            judul = it.judul
            namaGereja = it.namaGereja
            pembicara = it.pembicara
            jadwalIbadah = it.jadwalIbadah
            jamIbadah = it.jamIbadah
            deskripsi = it.deskripsi
            gambarBerita = it.gambarBerita
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = "Tambahkan Berita Acara")
                    else
                        Text(text = "Ubah Berita Acara")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        judulError = judul.isEmpty()
                        namaGerejaError = namaGereja.isEmpty()
                        pembicaraError = pembicara.isEmpty()
                        jadwalIbadahError = jadwalIbadah.isEmpty()
                        jamIbadahError = jamIbadah.isEmpty()
                        deskripsiError = deskripsi.isEmpty()
                        gambarBeritaError = gambarBerita.isEmpty()
                        if (judulError || namaGerejaError || jadwalIbadahError || jamIbadahError || deskripsiError || gambarBeritaError) {
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(judul, namaGereja, pembicara, jadwalIbadah, jamIbadah, deskripsi, gambarBerita)
                            Toast.makeText(context, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.update(
                                id.toString(),
                                judul,
                                namaGereja,
                                pembicara,
                                jadwalIbadah,
                                jamIbadah,
                                deskripsi,
                                gambarBerita
                            )
                            Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                        }

                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteBeritaAcara {
                            viewModel.delete(id.toString())
                            navHostController.popBackStack()
                        }
                    }
                }
            )
        },
    ) { contentPadding ->
        DetailBeritaAcaraCard(
            judul = judul,
            onJudulChange = { judul = it },
            namaGereja = namaGereja,
            onNamaGerejaChange = { namaGereja = it },
            pembicara = pembicara,
            onPembicaraChange = { pembicara = it },
            jadwalIbadah = jadwalIbadah,
            onJadwalIbadahChange = { jadwalIbadah = it },
            jam = jamIbadah,
            onJamChange = { jamIbadah = it },
            deskripsi = deskripsi,
            onDeskripsiChange = { deskripsi = it },
            gambarBerita = gambarBerita,
            onGambarBeritaChange = { gambarBerita = it },
            judulError = judulError,
            namaGerejaError = namaGerejaError,
            pembicaraError = pembicaraError,
            jadwalIbadahError = jadwalIbadahError,
            jamError = jamIbadahError,
            deskripsiError = deskripsiError,
            gambarBeritaError = gambarBeritaError,
            bitmap = bitmap,
            isUploading = isUploading,
            contentPadding = contentPadding,
            context = context
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailBeritaAcaraCard(
    judul: String,
    onJudulChange: (String) -> Unit,
    namaGereja: String,
    onNamaGerejaChange: (String) -> Unit,
    pembicara: String,
    onPembicaraChange: (String) -> Unit,
    jadwalIbadah: String,
    onJadwalIbadahChange: (String) -> Unit,
    jam: String,
    onJamChange: (String) -> Unit,
    deskripsi: String,
    onDeskripsiChange: (String) -> Unit,
    gambarBerita: String,
    onGambarBeritaChange: (String) -> Unit,
    judulError: Boolean,
    namaGerejaError: Boolean,
    pembicaraError: Boolean,
    jadwalIbadahError: Boolean,
    jamError: Boolean,
    deskripsiError: Boolean,
    gambarBeritaError: Boolean,
    bitmap: MutableState<Bitmap?>,
    isUploading: MutableState<Boolean>,
    contentPadding: PaddingValues,
    context: Context
) {
    val focusManager = LocalFocusManager.current
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.now()) }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            onJadwalIbadahChange(selectedDate)
        }, year, month, day
    )

    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            onJamChange(selectedTime)
        }, currentHour, currentMinute, true
    )
    var expanded by remember { mutableStateOf(false) }


    val repositoryGereja = GerejaRepository()
    val factoryGereja = GerejaViewModelFactory(repositoryGereja)
    val gerejaViewModel: GerejaDetailViewModel = viewModel(factory = factoryGereja)
    val gereja by gerejaViewModel.gerejaList.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = judul,
            onValueChange = { onJudulChange(it) },
            label = { Text(text = "Nama Acara") },
            singleLine = true,
            isError = judulError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (judulError) {
            Text(text = "Nama Acara tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = namaGereja,
            onValueChange = { onNamaGerejaChange(it) },
            label = { Text(text = "Nama Gereja") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Pilih Gereja")
                }
            },
            readOnly = true,
            singleLine = true,
            isError = namaGerejaError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (namaGerejaError) {
            Text(text = "Nama Gereja tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            gereja.forEach { gereja ->
                DropdownMenuItem(
                    text = { Text(text = gereja.judul) },
                    onClick = {
                        onNamaGerejaChange(gereja.judul)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

        OutlinedTextField(
            value = pembicara,
            onValueChange = { onPembicaraChange(it) },
            label = { Text(text = "Pembicara") },
            singleLine = true,
            isError = pembicaraError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (pembicaraError) {
            Text(text = "Pembicara tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = jadwalIbadah,
            onValueChange = { onJadwalIbadahChange(it) },
            readOnly = true,
            singleLine = true,
            isError = jadwalIbadahError,
            label = { Text("Jadwal Ibadah Gereja") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Pilih Tanggal")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (jadwalIbadahError) {
            Text(text = "Jadwal Ibadah Gereja tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        // Dialog untuk pemilih tanggal
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "OK") {
                    dateDialogState.hide()
                }
                negativeButton(text = "Batal") {
                    dateDialogState.hide()
                }
            }
        ) {
            datepicker(
                initialDate = pickedDate,
                onDateChange = { pickedDate = it }
            )
        }

        OutlinedTextField(
            value = jam,
            onValueChange = { onJamChange(it) },
            readOnly = true,
            singleLine = true,
            isError = jamError,
            label = { Text("Jam Ibadah Gereja") },
            trailingIcon = {
                IconButton(onClick = { timePickerDialog.show() }) {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Pilih Jam")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (jamError) {
            Text(text = "Jam Ibadah Gereja tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        // Dialog untuk pemilih jam
        MaterialDialog(
            dialogState = timeDialogState,
            buttons = {
                positiveButton(text = "OK") {
                    timeDialogState.hide()
                }
                negativeButton(text = "Batal") {
                    timeDialogState.hide()
                }
            }
        ) {
            timepicker(
                initialTime = pickedTime,
                onTimeChange = { pickedTime = it }
            )
        }

        OutlinedTextField(
            value = deskripsi,
            onValueChange = { onDeskripsiChange(it) },
            label = { Text(text = "Informasi Selengkapnya") },
            isError = deskripsiError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = false,
            maxLines = 4
        )
        if (deskripsiError) {
            Text(
                text = "Informasi Selengkapnya tidak boleh kosong",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image picker and upload section
        BeritaAcaraImagePicker(bitmap, isUploading) { imageUrl ->
            onGambarBeritaChange(imageUrl)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DeleteBeritaAcara(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Opsi lainnya",
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "Hapus Gereja")
                },
                onClick = {
                    expanded = false
                    delete()
                })
        }
    }
}

@Composable
fun BeritaAcaraImagePicker(
    bitmap: MutableState<Bitmap?>,
    isUploading: MutableState<Boolean>,
    onImageUploaded: (String) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream)
            bitmap.value = selectedBitmap
            BeritaAcaraUploadImageToFirebase(selectedBitmap, isUploading, onImageUploaded)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )
        } ?: Text("No Image Selected")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }
    }
}

fun BeritaAcaraUploadImageToFirebase(
    bitmap: Bitmap,
    isUploading: MutableState<Boolean>,
    onImageUploaded: (String) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("images/berita_acara_images/${UUID.randomUUID()}.jpg")

    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()

    isUploading.value = true
    val uploadTask = imagesRef.putBytes(data)
    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let { throw it }
        }
        imagesRef.downloadUrl
    }.addOnCompleteListener { task ->
        isUploading.value = false
        if (task.isSuccessful) {
            val downloadUri = task.result
            onImageUploaded(downloadUri.toString())
        } else {
            // Handle failures
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailBeritaAcaraScreenPreview() {
    ProjectTingkat2Theme {
        DetailBeritaAcaraScreen(rememberNavController())
    }
}