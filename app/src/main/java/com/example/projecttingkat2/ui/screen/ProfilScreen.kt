package com.example.projecttingkat2.ui.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.R
import com.example.projecttingkat2.model.User
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfilScreen(navHostController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
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
                                .padding(bottom = 8.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        userViewModel.logout()
                        navHostController.navigate(Screen.LoginRegister.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
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
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
        },
        bottomBar = {
            ProfilBottomNavigation(navHostController)
        },
    ) { contentPadding ->
        ProfilCard(viewModel(), user = User(), modifier = Modifier.padding(contentPadding))
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfilCard(
    viewModel: UserViewModel,
    user: User,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var email by remember { mutableStateOf(user.email) }
    var newPassword  by remember { mutableStateOf(user.password) }
    var nomorHP by remember { mutableStateOf(user.nomorHp) }
    var tanggal by remember { mutableStateOf(user.tanggal) }
    var passwordVisible by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf(user.role ?: "") }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var newPasswordError  by remember { mutableStateOf(false) }
    var noHPError by remember { mutableStateOf(false) }
    var tanggalError by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val currentUser by viewModel.currentUser.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()
    val updateError by viewModel.updateError.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            firstName = it.firstName
            lastName = it.lastName
            email = it.email
            nomorHP = it.nomorHp
            tanggal = it.tanggal
            role = it.role
        }
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show()
            Log.d("UpdateUserScreen", "User profile updated successfully")
        }
    }

    LaunchedEffect(updateError) {
        updateError?.let {
            Toast.makeText(context, "Update Failed: $it", Toast.LENGTH_SHORT).show()
            Log.d("UpdateUserScreen", "Error updating user profile: $it")
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "Nama Depan") },
                singleLine = true,
                isError = firstNameError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape
            )
            if (firstNameError) {
                Text(text = "Nama Depan tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Nama Belakang") },
                singleLine = true,
                isError = lastNameError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape
            )
            if (lastNameError) {
                Text(text = "Nama Belakang tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Alamat Surel") },
                singleLine = true,
                isError = emailError,
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape
            )
            if (emailError) {
                Text(text = "Alamat Surel tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Kata Sandi Baru") },
                singleLine = true,
                isError = newPasswordError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                shape = CircleShape,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                }
            )
            if (newPasswordError) {
                Text(
                    text = "Konfirmasi Kata Sandi tidak boleh kosong",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nomorHP,
                onValueChange = { nomorHP = it },
                label = { Text(text = "Nomor HP") },
                singleLine = true,
                isError = noHPError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape
            )
            if (noHPError) {
                Text(text = "Nomor HP tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tanggal,
                onValueChange = { tanggal = it },
                label = { Text(text = "Tanggal Lahir") },
                singleLine = true,
                isError = tanggalError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape,
            )
            if (tanggalError) {
                Text(text = "Tanggal Lahir tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Peran") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    firstNameError = firstName.isEmpty()
                    lastNameError = lastName.isEmpty()
                    emailError = email.isEmpty()
                    newPasswordError = newPassword.isEmpty()
                    noHPError = nomorHP.isEmpty()
                    tanggalError = tanggal.isEmpty()


                    // If all fields are valid, proceed with update
                    if (!newPassword.isBlank()) {
                        viewModel.updateProfile(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            newPassword = if (newPassword.isBlank()) null else newPassword,
                            nomorHP = nomorHP,
                            tanggal = tanggal,
                            role = role,
                            onSuccess = {
                                Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show()
                                Log.d("UpdateUserScreen", "User profile updated successfully")
                            },
                            onFailure = {
                                    updateError?.let {
                                    Toast.makeText(context, "Update Failed: $it", Toast.LENGTH_SHORT).show()
                                    Log.d("UpdateUserScreen", "Error updating user profile: $it")
                                    }
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProjectTingkat2Theme {
        ProfilScreen(rememberNavController())
    }
}