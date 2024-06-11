package com.example.projecttingkat2.ui.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
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
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginRegisterScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.top_background),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Selamat Datang di GerejaKu",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color(android.graphics.Color.parseColor("#7d32a8"))
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        bottomBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Image(
                    painter = painterResource(R.drawable.bottom_background),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    ) { contentPadding ->
        Card(
            modifier = Modifier
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                val pagerState = rememberPagerState(pageCount = { 2 })
                val coroutineScope = rememberCoroutineScope()

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            height = 2.dp
                        )
                    }
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        text = {
                            Text(text = stringResource(R.string.login))
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    )
                    Tab(
                        selected = pagerState.currentPage == 1,
                        text = {
                            Text(text = stringResource(R.string.daftar))
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                }
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(start = 28.dp, end = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        when (page) {
                            0 -> LoginSr(navHostController, viewModel())
                            1 -> RegisterSr(viewModel(), onRegisterSuccess = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginSr(navHostController: NavHostController, viewModel: UserViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val loginError by viewModel.loginError.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            Toast.makeText(context, "Berhasil Masuk", Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home.route)
        }
    }

    LaunchedEffect(loginError) {
        loginError?.let {
            Toast.makeText(context, "Gagal Masuk: $it", Toast.LENGTH_SHORT).show()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape
            )
            if (emailError) {
                Text(text = "Email tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Kata Sandi") },
                singleLine = true,
                isError = passwordError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                shape = CircleShape,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            if (passwordError) {
                Text(text = "Password tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    emailError = email.isEmpty()
                    passwordError = password.isEmpty()

                    if (!emailError && !passwordError) {
                        viewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(top = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#7d32a8"))),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Masuk")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterSr(viewModel: UserViewModel, onRegisterSuccess: () -> Unit) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var noHP by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    val options = listOf("Pengguna", "Gereja")
    var role by remember { mutableStateOf(options[0]) }
    var expanded by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var noHPError by remember { mutableStateOf(false) }
    var tanggalError by remember { mutableStateOf(false) }
    var roleError by remember { mutableStateOf(false) }

    val user = User(
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
        nomorHp = noHP,
        tanggal = tanggal,
        role = role
    )

    val registrationSuccess by viewModel.registrationSuccess.collectAsState()
    val registrationError by viewModel.registrationError.collectAsState()

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
            // Navigate to another screen if needed
        }
    }

    LaunchedEffect(registrationError) {
        registrationError?.let {
            Toast.makeText(context, "Registration Failed: $it", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier
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
                Text(
                    text = "Nama Depan tidak boleh kosong atau lebih dari 25 karakter",
                    color = MaterialTheme.colorScheme.error
                )
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
                Text(
                    text = "Nama Belakang tidak boleh kosong atau lebih dari 10 karakter",
                    color = MaterialTheme.colorScheme.error
                )
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
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = CircleShape
            )
            if (emailError) {
                Text(
                    text = "Alamat Surel tidak boleh kosong, menggunakan simbol kecuali @, atau lebih dari 30 karakter",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Kata Sandi") },
                singleLine = true,
                isError = passwordError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                shape = CircleShape,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            if (passwordError) {
                Text(
                    text = "Kata Sandi harus antara 6 dan 15 karakter",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Konfirmasi Kata Sandi") },
                singleLine = true,
                isError = confirmPasswordError,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                shape = CircleShape,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )
            if (confirmPasswordError) {
                Text(
                    text = "Konfirmasi Kata Sandi tidak boleh kosong",
                    color = MaterialTheme.colorScheme.error
                )
            } else if (password != confirmPassword) {
                Text(
                    text = "Konfirmasi Kata Sandi Berbeda",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = noHP,
                onValueChange = { noHP = it },
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
                Text(
                    text = "Nomor HP harus angka dan tidak lebih dari 13 karakter",
                    color = MaterialTheme.colorScheme.error
                )
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
                Text(
                    text = "Tanggal Lahir tidak boleh kosong", color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = role,
                    onValueChange = { role = it },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = roleError,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .imePadding(),
                    shape = CircleShape,
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                role = options[index]
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            if (roleError) {
                Text(text = "tidak boleh kosong", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    firstNameError = firstName.isEmpty() || firstName.length > 25 || firstName.length <= 3
                    lastNameError = lastName.isEmpty() || lastName.length > 10 || lastName.length <= 3
                    emailError = email.isEmpty() || email.length > 30 || email.any { !it.isLetterOrDigit() && it != '@' && it == '_' && it == '.' }
                    passwordError = password.isEmpty() || password.length < 6 || password.length > 15
                    confirmPasswordError = confirmPassword.isEmpty() || confirmPassword != password
                    noHPError = noHP.isEmpty() || noHP.length > 13 || noHP.any { !it.isDigit() }
                    tanggalError = tanggal.isEmpty()
                    roleError = role.isEmpty()

                    if (password != confirmPassword) {
                        confirmPasswordError = true
                        Toast.makeText(context, "Password tidak sama", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    if (!firstNameError && !lastNameError && !emailError && !passwordError && !confirmPasswordError && !noHPError && !tanggalError && !roleError) {
                        viewModel.registerUser(user)
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(top = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#7d32a8"))),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Daftar")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun LoginRegisterScreenPreview() {
    ProjectTingkat2Theme {
        LoginRegisterScreen(rememberNavController())
    }
}
