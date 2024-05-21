package com.example.projecttingkat2.ui.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.R
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfilScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.profiledoa),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(280.dp)
                        .padding(bottom = 8.dp)
                )
            }
        },
        bottomBar = {
            ProfilBottomNavigation(navHostController)
        },
    ) { contentPadding ->
        ProfilCard(navHostController, modifier = Modifier.padding(contentPadding))
    }
}

@Composable
private fun ProfilBottomNavigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.shadow(elevation = 4.dp)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Church,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.utama))
            },
            selected = false,
            onClick = {
                navHostController.navigate(Screen.Home.route)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.buku))
            },
            selected = false,
            onClick = {
                navHostController.navigate(Screen.Book.route)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Newspaper,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.berita_acara))
            },
            selected = false,
            onClick = {
                navHostController.navigate(Screen.BeritaAcara.route)
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.data_diri))
            },
            selected = true,
            onClick = {
                navHostController.navigate(Screen.Profil.route)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfilCard(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var nama by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var noHp by rememberSaveable { mutableStateOf("") }
    var namaError by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var noHpError by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Nama")
        OutlinedTextField(
            value = nama,
            onValueChange = { newNama ->
                nama = newNama
                namaError = newNama.isNullOrEmpty() == null
            },
            label = {
                Text(text = stringResource(R.string.name))
            },
            isError = namaError,
            trailingIcon = { IconPicker(namaError) },
            supportingText = { ErrorHint(namaError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Text(text = "Email")
        OutlinedTextField(
            value = email,
            onValueChange = { newEmail ->
                email = newEmail
                emailError = newEmail.isNullOrEmpty() == null
            },
            label = {
                Text(text = stringResource(R.string.email_address))
            },
            isError = emailError,
            trailingIcon = { IconPicker(emailError) },
            supportingText = { ErrorHint(emailError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Text(text = "No HP")
        OutlinedTextField(
            value = noHp,
            onValueChange = { newNomorHp ->
                noHp = newNomorHp
                noHpError = newNomorHp.isNullOrEmpty() == null
            },
            label = {
                Text(text = stringResource(R.string.no_hp))
            },
            isError = noHpError,
            trailingIcon = { IconPicker(noHpError) },
            supportingText = { ErrorHint(noHpError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )

        // Tanggal Lahir
        Text(text = "Tanggal Lahir")
        CalenderField()

        Text(text = "Kata Sandi")
        OutlinedTextField(
            value = password,
            onValueChange = { newPassword ->
                password = newPassword
                passwordError = newPassword.isNullOrEmpty() == null
            },
            label = {
                Text(text = stringResource(R.string.password))
            },
            isError = passwordError,
            trailingIcon = { IconPicker(passwordError) },
            supportingText = { ErrorHint(passwordError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = {
                emailError = email.isEmpty()
                passwordError = password.isEmpty()
                namaError = nama.isEmpty()
                noHpError = noHp.isEmpty()
                if (emailError || passwordError || namaError || noHpError) {
                    return@Button
                }
                val message = "Hi, Selamat Datang di Gereja Ku"
                navHostController.navigate(Screen.Home.route)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.width(300.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF4F1964)),
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 18.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalenderField() {
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tanggal") },
            trailingIcon = {
                IconButton(onClick = { dateDialogState.show() }) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Pilih Tanggal")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Dialog untuk pemilih tanggal
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "OK") {
                    // Simpan tanggal terpilih dan perbarui tampilan
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
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun BookScreenPreview() {
    ProjectTingkat2Theme {
        ProfilScreen(rememberNavController())
    }
}