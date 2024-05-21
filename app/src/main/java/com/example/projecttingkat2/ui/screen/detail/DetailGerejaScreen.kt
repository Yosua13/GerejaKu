package com.example.projecttingkat2.ui.screen.detail

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.R
import com.example.projecttingkat2.database.GerejaKuDb
import com.example.projecttingkat2.ui.screen.ErrorHint
import com.example.projecttingkat2.ui.screen.IconPicker
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.GerejaViewModelFactory
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel

const val KEY_ID_GEREJA = "idGereja"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailGerejaScreen(
    navHostController: NavHostController,
    id: Long? = null
) {
    val context = LocalContext.current
    val db = GerejaKuDb.getInstance(context)
    val factory = GerejaViewModelFactory(db.gerejaDao)
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

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getGereja(id) ?: return@LaunchedEffect
        judul = data.judul
        aliran = data.aliran
        lokasi = data.lokasi
        link = data.link
        jadwal = data.jadwal
        deskripsi = data.deskripsi
        gambar = data.gambar
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
                        if (judulError ||aliranError || lokasiError || linkError || jadwalError || deskripsiError || gambarError) {
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(judul, aliran, lokasi, link, jadwal, deskripsi, gambar)
                        } else {
                            viewModel.update(id, judul, aliran, lokasi, link, jadwal, deskripsi, gambar)
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
                            viewModel.delete(id)
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
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nama Gereja")
        OutlinedTextField(
            value = judul,
            onValueChange = { newJudul ->
                onJudulChange(newJudul)
            },
            label = {
                Text(text = stringResource(R.string.name))
            },
            isError = judulError,
            trailingIcon = { IconPicker(judulError) },
            supportingText = { ErrorHint(judulError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Text(text = "Aliran Gereja")
        OutlinedTextField(
            value = aliran,
            onValueChange = { newAliran ->
                onAliranChange(newAliran)
            },
            label = {
                Text(text = stringResource(R.string.email_address))
            },
            isError = aliranError,
            trailingIcon = { IconPicker(aliranError) },
            supportingText = { ErrorHint(aliranError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Text(text = "Lokasi Gereja")
        OutlinedTextField(
            value = lokasi,
            onValueChange = { newLokasi ->
                onLokasiChange(newLokasi)
            },
            label = {
                Text(text = stringResource(R.string.email_address))
            },
            isError = lokasiError,
            trailingIcon = { IconPicker(lokasiError) },
            supportingText = { ErrorHint(lokasiError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Text(text = "Link GMaps Gereja")
        OutlinedTextField(
            value = link,
            onValueChange = { newLink ->
                onLinkChange(newLink)
            },
            label = {
                Text(text = "Link Gmaps Gereja")
            },
            isError = linkError,
            trailingIcon = { IconPicker(linkError) },
            supportingText = { ErrorHint(linkError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Text(text = "Jadwal Ibadah")
        OutlinedTextField(
            value = jadwal,
            onValueChange = { newJadwal ->
                onJadwalChange(newJadwal)
            },
            label = {
                Text(text = "08.00, 10.30, 13.30, 16.30")
            },
            isError = jadwalError,
            trailingIcon = { IconPicker(jadwalError) },
            supportingText = { ErrorHint(jadwalError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )

        Text(text = "Latar Belakang Gereja")
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { newDesc ->
                onDeskripsiChange(newDesc)
            },
            label = {
                Text(text = "Gereja ini didirikan pada.....")
            },
            isError = deskripsiError,
            trailingIcon = { IconPicker(deskripsiError) },
            supportingText = { ErrorHint(deskripsiError) },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )

        Text(text = "Gambar Gereja (.png)")
        if (gambar.isNotEmpty()) {
            Image(
                bitmap = ImageBitmap.imageResource(context.resources, context.resources.getIdentifier(gambar, "drawable", context.packageName)),
                contentDescription = "Gambar Gereja",
                modifier = Modifier.size(128.dp).padding(16.dp)
            )
        }
        Button(onClick = {
            // Handle image picker intent
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            (context as Activity).startActivityForResult(intent, 100)
        }) {
            Text(text = "Pilih Gambar")
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gambar,
            onValueChange = { onGambarChange(it) },
            label = { Text(text = "Path Gambar") },
            isError = gambarError,
            trailingIcon = { IconPicker(gambarError) },
            supportingText = { ErrorHint(gambarError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DetailGerejaScreenPreview() {
    ProjectTingkat2Theme {
        DetailGerejaScreen(rememberNavController())
    }
}