package com.example.projecttingkat2.ui.screen.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.R
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.GerejaViewModelFactory
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.math.sin

const val KEY_ID_GEREJA = "idGereja"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailGerejaScreen(
    navHostController: NavHostController,
    id: String? = null,
) {
    val context = LocalContext.current
    val repository = GerejaRepository()
    val factory = GerejaViewModelFactory(repository)
    val viewModel: GerejaDetailViewModel = viewModel(factory = factory)

    var judul by rememberSaveable { mutableStateOf("") }
    var aliran by rememberSaveable { mutableStateOf("") }
    var lokasi by rememberSaveable { mutableStateOf("") }
    var link by rememberSaveable { mutableStateOf("") }
    var jadwal by rememberSaveable { mutableStateOf("") }
    var deskripsi by rememberSaveable { mutableStateOf("") }
    var gambar by rememberSaveable { mutableStateOf("") }

    var judulError by rememberSaveable { mutableStateOf(false) }
    var aliranError by rememberSaveable { mutableStateOf(false) }
    var lokasiError by rememberSaveable { mutableStateOf(false) }
    var linkError by rememberSaveable { mutableStateOf(false) }
    var jadwalError by rememberSaveable { mutableStateOf(false) }
    var deskripsiError by rememberSaveable { mutableStateOf(false) }
    var gambarError by rememberSaveable { mutableStateOf(false) }

    val isUploading = remember { mutableStateOf(false) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getGerejaById(id)
        data?.let {
            judul = it.judul
            aliran = it.aliran
            lokasi = it.lokasi
            link = it.link
            jadwal = it.jadwal
            deskripsi = it.deskripsi
            gambar = it.gambar
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
                        Text(text = "Tambahkan Gereja")
                    else
                        Text(text = "Ubah Gereja")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        judulError = judul.isEmpty()
                        aliranError = aliran.isEmpty()
                        lokasiError = lokasi.isEmpty()
                        linkError = link.isEmpty()
                        jadwalError = jadwal.isEmpty()
                        deskripsiError = deskripsi.isEmpty()
                        gambarError = gambar.isEmpty()
                        if (judulError || aliranError || lokasiError || linkError || jadwalError || deskripsiError || gambarError) {
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(judul, aliran, lokasi, link, jadwal, deskripsi, gambar)
                            Toast.makeText(context, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.update(
                                id.toString(),
                                judul,
                                aliran,
                                lokasi,
                                link,
                                jadwal,
                                deskripsi,
                                gambar
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
                        DeleteAction {
                            viewModel.delete(id.toString())
                            navHostController.popBackStack()
                        }
                    }
                }
            )
        },
    ) { contentPadding ->
        DetailGerejaCard(
            judul = judul,
            onJudulChange = { judul = it },
            aliran = aliran,
            onAliranChange = { aliran = it },
            lokasi = lokasi,
            onLokasiChange = { lokasi = it },
            link = link,
            onLinkChange = { link = it },
            jadwal = jadwal,
            onJadwalChange = { jadwal = it },
            deskripsi = deskripsi,
            onDeskripsiChange = { deskripsi = it },
            gambar = gambar,
            onGambarChange = { gambar = it },
            judulError = judulError,
            aliranError = aliranError,
            lokasiError = lokasiError,
            linkError = linkError,
            jadwalError = jadwalError,
            deskripsiError = deskripsiError,
            gambarError = gambarError,
            bitmap = bitmap,
            isUploading = isUploading,
            contentPadding = contentPadding,
            context = context
        )
    }
}


@Composable
fun DetailGerejaCard(
    judul: String,
    onJudulChange: (String) -> Unit,
    aliran: String,
    onAliranChange: (String) -> Unit,
    lokasi: String,
    onLokasiChange: (String) -> Unit,
    link: String,
    onLinkChange: (String) -> Unit,
    jadwal: String,
    onJadwalChange: (String) -> Unit,
    deskripsi: String,
    onDeskripsiChange: (String) -> Unit,
    gambar: String,
    onGambarChange: (String) -> Unit,
    judulError: Boolean,
    aliranError: Boolean,
    lokasiError: Boolean,
    linkError: Boolean,
    jadwalError: Boolean,
    deskripsiError: Boolean,
    gambarError: Boolean,
    bitmap: MutableState<Bitmap?>,
    isUploading: MutableState<Boolean>,
    contentPadding: PaddingValues,
    context: Context
) {
    val focusManager = LocalFocusManager.current

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
            label = { Text(text = "Nama Gereja") },
            singleLine = true,
            isError = judulError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (judulError) {
            Text(text = "Nama Gereja tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = aliran,
            onValueChange = { onAliranChange(it) },
            label = { Text(text = "Aliran Gereja") },
            singleLine = true,
            isError = aliranError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (aliranError) {
            Text(text = "Aliran Gereja tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = lokasi,
            onValueChange = { onLokasiChange(it) },
            label = { Text(text = "Lokasi Gereja") },
            singleLine = true,
            isError = lokasiError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (lokasiError) {
            Text(text = "Lokasi Gereja tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = link,
            onValueChange = { onLinkChange(it) },
            label = { Text(text = "Link GMaps Gereja") },
            singleLine = true,
            isError = linkError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (linkError) {
            Text(text = "Link GMaps tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = jadwal,
            onValueChange = { onJadwalChange(it) },
            label = { Text(text = "Jadwal Ibadah") },
            singleLine = true,
            isError = jadwalError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (jadwalError) {
            Text(text = "Jadwal Ibadah tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = deskripsi,
            onValueChange = { onDeskripsiChange(it) },
            label = { Text(text = "Latar Belakang Gereja") },
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
                text = "Latar Belakang tidak boleh kosong",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image picker and upload section
        GerejaImagePicker(bitmap, isUploading) { imageUrl ->
            onGambarChange(imageUrl)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun DeleteAction(delete: () -> Unit) {
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
fun GerejaImagePicker(
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
            GerejaUploadImageToFirebase(selectedBitmap, isUploading, onImageUploaded)
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

fun GerejaUploadImageToFirebase(
    bitmap: Bitmap,
    isUploading: MutableState<Boolean>,
    onImageUploaded: (String) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("images/gereja_images/${UUID.randomUUID()}.jpg")

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
fun DetailGerejaScreenPreview() {
    ProjectTingkat2Theme {
        DetailGerejaScreen(rememberNavController())
    }
}