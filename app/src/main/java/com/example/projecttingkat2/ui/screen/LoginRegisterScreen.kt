package com.example.projecttingkat2.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.R
import com.example.projecttingkat2.components.ButtonComponent
import com.example.projecttingkat2.components.CheckboxComponent
import com.example.projecttingkat2.components.DividerTextComponent
import com.example.projecttingkat2.components.MyTextFieldComponent
import com.example.projecttingkat2.components.PasswordMyTextFieldComponent
import com.example.projecttingkat2.components.UnderLineTextComponent
import com.example.projecttingkat2.data.LoginViewModel
import com.example.projecttingkat2.data.UIEvent
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import kotlinx.coroutines.launch

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
                    painter = painterResource(R.drawable.logogerejaku),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(150.dp)
                        .clip(RoundedCornerShape(40.dp))
                )
            }
        }
    ) { contentPadding ->
        Card(
            modifier = Modifier
                .padding(start = 28.dp, end = 28.dp, bottom = 80.dp)
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
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        when (page) {
                            0 -> LoginPageContent(navHostController)
                            1 -> RegisterPageContent(navHostController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginPageContent(navHostController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginForm(navHostController)
    }
}

@Composable
fun RegisterPageContent(navHostController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterForm(navHostController)
    }
}


@Composable
fun LoginForm(navHostController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource = painterResource(id = R.drawable.message),
                onTextSelected = {

                }
            )
            PasswordMyTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource = painterResource(id = R.drawable.ic_lock),
                onTextSelected = {

                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            UnderLineTextComponent(stringResource(id = R.string.forgot_password))
            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(value = stringResource(id = R.string.login), onButtonClicked = {  }, isEnabled = false, navHostController)
            Spacer(modifier = Modifier.height(20.dp))
            DividerTextComponent()
        }
    }
}
//@Composable
//fun LoginForm(
//    navHostController: NavHostController,
//    modifier: Modifier = Modifier,
//) {
//    var email by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var emailError by rememberSaveable { mutableStateOf(false) }
//    var passwordError by rememberSaveable { mutableStateOf(false) }
//    val context = LocalContext.current
//
//    Column(
//        modifier = modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        OutlinedTextField(
//            value = email,
//            onValueChange = { newEmail ->
//                email = newEmail
//                emailError = newEmail.isNullOrEmpty() == null
//            },
//            label = {
//                Text(text = stringResource(R.string.email_address))
//            },
//            isError = emailError,
//            trailingIcon = { IconPicker(emailError) },
//            supportingText = { ErrorHint(emailError) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp)
//                .fillMaxWidth()
//        )
//        OutlinedTextField(
//            value = password,
//            onValueChange = { newPassword ->
//                password = newPassword
//                passwordError = newPassword.isNullOrEmpty() == null
//            },
//            label = {
//                Text(text = stringResource(R.string.password))
//            },
//            isError = passwordError,
//            trailingIcon = { IconPicker(passwordError) },
//            supportingText = { ErrorHint(passwordError) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Next
//            ),
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp)
//                .fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.padding(16.dp))
//        Button(
//            onClick = {
//                emailError = email.isEmpty()
//                passwordError = password.isEmpty()
//                if (emailError || passwordError) {
//                    return@Button
//                }
//                val message = "Hi, Selamat Datang di Gereja Ku"
//                navHostController.navigate(Screen.Home.route)
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//            },
//            modifier = Modifier.width(300.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xFF4F1964)),
//        ) {
//            Text(
//                text = stringResource(R.string.login),
//                fontSize = 18.sp,
//                modifier = Modifier.padding(4.dp)
//            )
//        }
//        Spacer(modifier = Modifier.padding(16.dp))
//    }
//}

@Composable
fun RegisterForm(navHostController: NavHostController, loginViewModel: LoginViewModel = viewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.first_name),
                painterResource = painterResource(id = R.drawable.profile),
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.FirstNameChanged(it))
                },
                errorStatus = loginViewModel.registrationUIState.value.firstNameError
            )

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.last_name),
                painterResource = painterResource(id = R.drawable.profile),
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.LastNameChanged(it))
                },
                errorStatus = loginViewModel.registrationUIState.value.lastNameError
            )

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource = painterResource(id = R.drawable.message),
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.EmailChanged(it))
                },
                errorStatus = loginViewModel.registrationUIState.value.emailError
            )

            PasswordMyTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource = painterResource(id = R.drawable.ic_lock),
                onTextSelected = {
                    loginViewModel.onEvent(UIEvent.PasswordChanged(it))
                },
                errorStatus = loginViewModel.registrationUIState.value.passwordError
            )

            CheckboxComponent(
                value = stringResource(id = R.string.term_and_conditions),
                onTextSelected = {
//                    PostOfficeAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
                },
                onCheckedChange = {
                    loginViewModel.onEvent(UIEvent.PrivacyPolicyCheckBoxClicked(it))
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            ButtonComponent(
                value = stringResource(id = R.string.register),
                onButtonClicked = { loginViewModel.onEvent(UIEvent.RegisterButtonClicked) },
                isEnabled = loginViewModel.allValidationsPassed.value,
                navHostController
            )

            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

//@Composable
//fun RegisterForm(
//    modifier: Modifier = Modifier,
//) {
//    var name by rememberSaveable { mutableStateOf("") }
//    var email by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var nameError by rememberSaveable { mutableStateOf(false) }
//    var emailError by rememberSaveable { mutableStateOf(false) }
//    var passwordError by rememberSaveable { mutableStateOf(false) }
//    val context = LocalContext.current
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .verticalScroll(state = rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Spacer(modifier = Modifier.padding(8.dp))
//        OutlinedTextField(
//            value = name,
//            onValueChange = { newName ->
//                name = newName
//                nameError = newName.isNullOrEmpty() == null
//            },
//            label = {
//                Text(text = stringResource(R.string.name))
//            },
//            isError = nameError,
//            trailingIcon = { IconPicker(nameError) },
//            supportingText = { ErrorHint(nameError) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp)
//                .fillMaxWidth()
//        )
//        OutlinedTextField(
//            value = email,
//            onValueChange = { newEmail ->
//                email = newEmail
//                emailError = newEmail.isNullOrEmpty() == null
//            },
//            label = {
//                Text(text = stringResource(R.string.email_address))
//            },
//            isError = emailError,
//            trailingIcon = { IconPicker(emailError) },
//            supportingText = { ErrorHint(emailError) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp)
//                .fillMaxWidth()
//        )
//        OutlinedTextField(
//            value = password,
//            onValueChange = { newPassword ->
//                password = newPassword
//                passwordError = newPassword.isNullOrEmpty() == null
//            },
//            label = {
//                Text(text = stringResource(R.string.password))
//            },
//            isError = passwordError,
//            trailingIcon = { IconPicker(passwordError) },
//            supportingText = { ErrorHint(passwordError) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Next
//            ),
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier
//                .padding(start = 16.dp, end = 16.dp)
//                .fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.padding(16.dp))
//        Button(
//            onClick = {
//                nameError = name.isEmpty()
//                emailError = email.isEmpty()
//                passwordError = password.isEmpty()
//                if (emailError || passwordError || nameError) return@Button
//            },
//            modifier = Modifier.width(300.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xFF4F1964)),
//        ) {
//            Text(
//                text = stringResource(R.string.daftar),
//                fontSize = 18.sp,
//                modifier = Modifier.padding(4.dp)
//            )
//        }
//        Spacer(modifier = Modifier.padding(16.dp))
//        Text(text = stringResource(R.string.or))
//        Spacer(modifier = Modifier.padding(8.dp))
//        Image(
//            painter = painterResource(R.drawable.google),
//            contentDescription = null,
//            modifier = Modifier
//                .size(80.dp)
//                .clip(CircleShape),
//        )
//    }
//}

@Composable
fun IconPicker(isError: Boolean) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginRegisterScreenPreview() {
    ProjectTingkat2Theme {
        LoginRegisterScreen(rememberNavController())
    }
}