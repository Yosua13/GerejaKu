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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.firebase.BukuRepository
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.BukuViewModelFactory
import com.example.projecttingkat2.viewmodel.BukuDetailViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.UUID

const val KEY_ID_BUKU = "idBuku"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBukuScreen(
    navHostController: NavHostController,
    id: String? = null,
) {
    val context = LocalContext.current
    val repository = BukuRepository()
    val factory = BukuViewModelFactory(repository)
    val viewModel: BukuDetailViewModel = viewModel(factory = factory)

    var judul by rememberSaveable { mutableStateOf("") }
    var penulis by rememberSaveable { mutableStateOf("") }
    var sinopsis by rememberSaveable { mutableStateOf("") }
    var gambar by rememberSaveable { mutableStateOf("") }

    var judulError by rememberSaveable { mutableStateOf(false) }
    var penulisError by rememberSaveable { mutableStateOf(false) }
    var sinopsisError by rememberSaveable { mutableStateOf(false) }
    var gambarError by rememberSaveable { mutableStateOf(false) }

    val isUploading = remember { mutableStateOf(false) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getBukuById(id)
        data?.let {
            judul = it.judul
            penulis = it.penulis
            sinopsis = it.sinopsis
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
                        Text(text = "Tambahkan Buku")
                    else
                        Text(text = "Ubah Buku")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        judulError = judul.isEmpty()
                        penulisError = penulis.isEmpty()
                        sinopsisError = sinopsis.isEmpty()
                        gambarError = gambar.isEmpty()
                        if (judulError || penulisError || sinopsisError || gambarError) {
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(judul, penulis, sinopsis, gambar)
                            Toast.makeText(context, "Buku berhasil dimasukkan", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.update(
                                id.toString(),
                                judul,
                                penulis,
                                sinopsis,
                                gambar
                            )
                            Toast.makeText(context, "Buku berhasil diubah", Toast.LENGTH_SHORT).show()
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
                        DeleteBuku {
                            viewModel.delete(id.toString())
                            navHostController.popBackStack()
                        }
                    }
                }
            )
        },
    ) { contentPadding ->
        DetailBukuCard(
            judul = judul,
            onJudulChange = { judul = it },
            penulis = penulis,
            onPenulisChange = { penulis = it },
            sinopsis = sinopsis,
            onSinopsisChange = { sinopsis = it },
            gambar = gambar,
            onGambarChange = { gambar = it },
            judulError = judulError,
            penulisError = penulisError,
            sinopsisError = sinopsisError,
            gambarError = gambarError,
            bitmap = bitmap,
            isUploading = isUploading,
            contentPadding = contentPadding,
            context = context
        )
    }
}

@Composable
fun DetailBukuCard(
    judul: String,
    onJudulChange: (String) -> Unit,
    penulis: String,
    onPenulisChange: (String) -> Unit,
    sinopsis: String,
    onSinopsisChange: (String) -> Unit,
    gambar: String,
    onGambarChange: (String) -> Unit,
    judulError: Boolean,
    penulisError: Boolean,
    sinopsisError: Boolean,
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
            label = { Text(text = "Judul Buku") },
            singleLine = true,
            isError = judulError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (judulError) {
            Text(text = "Judul tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = penulis,
            onValueChange = { onPenulisChange(it) },
            label = { Text(text = "Nama Penulis") },
            singleLine = true,
            isError = penulisError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (penulisError) {
            Text(text = "Nama Penulis tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = sinopsis,
            onValueChange = { onSinopsisChange(it) },
            label = { Text(text = "Sinopsis Buku") },
            singleLine = false,
            isError = sinopsisError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        if (sinopsisError) {
            Text(text = "Sinopsis Buku tidak boleh kosong", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image picker and upload section
        BukuImagePicker(bitmap, isUploading) { imageUrl ->
            onGambarChange(imageUrl)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun DeleteBuku(delete: () -> Unit) {
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
                    Text(text = "Hapus Buku")
                },
                onClick = {
                    expanded = false
                    delete()
                })
        }
    }
}

@Composable
fun BukuImagePicker(
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
            BookUploadImageToFirebase(selectedBitmap, isUploading, onImageUploaded)
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

fun BookUploadImageToFirebase(
    bitmap: Bitmap,
    isUploading: MutableState<Boolean>,
    onImageUploaded: (String) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("images/buku_images/${UUID.randomUUID()}.jpg")

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
fun DetailBukuScreenPreview() {
    ProjectTingkat2Theme {
        DetailBukuScreen(rememberNavController())
    }
}