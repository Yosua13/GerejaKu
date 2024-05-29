package com.example.projecttingkat2.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.projecttingkat2.components.DividerTextComponent
import com.example.projecttingkat2.components.MyTextFieldComponent
import com.example.projecttingkat2.components.PasswordMyTextFieldComponent
import com.example.projecttingkat2.components.UnderLineTextComponent
import com.example.projecttingkat2.data.LoginUIEvent
import com.example.projecttingkat2.data.LoginViewModel
import com.example.projecttingkat2.data.RegisterViewModel
import com.example.projecttingkat2.data.RegisterUIEvent
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
        LoginForm(viewModel(), navHostController)
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
fun LoginForm(loginViewModel: LoginViewModel = viewModel(), navHostController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource(id = R.drawable.message),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError
                )

                PasswordMyTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource(id = R.drawable.ic_lock),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.passwordError
                )

                Spacer(modifier = Modifier.height(40.dp))
//                UnderLineTextComponent(value = stringResource(id = R.string.forgot_password))

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    isEnabled = loginViewModel.allValidationsPassed.value,
                    navHostController
                )

                Spacer(modifier = Modifier.height(20.dp))

//                DividerTextComponent()

            }
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun RegisterForm(
    navHostController: NavHostController,
    registerViewModel: RegisterViewModel = viewModel(),
) {
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
                    registerViewModel.onEvent(RegisterUIEvent.FirstNameChanged(it))
                },
                errorStatus = registerViewModel.registrationUIState.value.firstNameError
            )

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.last_name),
                painterResource = painterResource(id = R.drawable.profile),
                onTextSelected = {
                    registerViewModel.onEvent(RegisterUIEvent.LastNameChanged(it))
                },
                errorStatus = registerViewModel.registrationUIState.value.lastNameError
            )

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource = painterResource(id = R.drawable.message),
                onTextSelected = {
                    registerViewModel.onEvent(RegisterUIEvent.EmailChanged(it))
                },
                errorStatus = registerViewModel.registrationUIState.value.emailError
            )

            PasswordMyTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource = painterResource(id = R.drawable.ic_lock),
                onTextSelected = {
                    registerViewModel.onEvent(RegisterUIEvent.PasswordChanged(it))
                },
                errorStatus = registerViewModel.registrationUIState.value.passwordError
            )

            Spacer(modifier = Modifier.height(40.dp))

            ButtonComponent(
                value = stringResource(id = R.string.register),
                onButtonClicked = { registerViewModel.onEvent(RegisterUIEvent.RegisterButtonClicked) },
                isEnabled = registerViewModel.allValidationsPassed.value,
                navHostController
            )

            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

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