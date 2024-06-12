package com.example.projecttingkat2.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttingkat2.R
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.model.Gereja
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.GerejaViewModelFactory
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel
import com.example.projecttingkat2.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()

    val repository = GerejaRepository()
    val factory = GerejaViewModelFactory(repository)
    val viewModel: GerejaDetailViewModel = viewModel(factory = factory)
    val gerejaList by viewModel.gerejaList.collectAsState()

    var searchText by remember { mutableStateOf("") }

    currentUser?.let { user ->
        Scaffold(
            topBar = {
                TopApp(
                    searchText = searchText,
                    onSearchTextChanged = { searchText = it },
                    onSearch = { viewModel.searchGereja(it) })
            },
            bottomBar = { HomeBottomNavigation(navHostController) },
            floatingActionButton = {
                if (user.role == "Gereja") {
                    FloatingActionButton(
                        onClick = {
                            navHostController.navigate(Screen.GerejaFormBaru.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tambah Gereja",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (user.role == "Pengguna") {
                    FloatingActionButton(
                        onClick = {
                            navHostController.navigate(Screen.ChatAI.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ChatBubble,
                            contentDescription = "Chat AI",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = contentPadding
            ) {
                items(gerejaList) { gereja ->
                    GerejaCard(gereja = gereja) {
                        if (user.role == "Gereja") {
                            navHostController.navigate(Screen.GerejaFormUbah.gerejaId(gereja.id))
                        } else if (user.role == "Pengguna") {
                            navHostController.navigate(Screen.GerejaPengguna.penggunaId(gereja.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeBottomNavigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val selectedColor = Color(0xFFEA5DFF)
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    NavigationBar(
        modifier = modifier.shadow(elevation = 4.dp)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Church,
                    contentDescription = null,
                    tint = selectedColor
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.utama),
                    color = selectedColor
                )
            },
            selected = true,
            onClick = {
                navHostController.navigate(Screen.Home.route)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
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
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
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
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
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
            selected = false,
            onClick = {
                navHostController.navigate(Screen.Profil.route)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
    }
}



@Composable
fun GerejaCard(
    gereja: Gereja,
    onClick: () -> Unit,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(gereja.gambar)
        .crossfade(true)
        .error(R.drawable.ic_launcher_foreground)  // Add a placeholder for error
        .build()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 28.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = gereja.judul,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = gereja.judul,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Row (modifier = Modifier.padding(top = 10.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Pembicara"
                    )
                    Text(
                        text = gereja.aliran,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row (modifier = Modifier.padding(top = 10.dp)) {
                    Icon(
                        imageVector = Icons.Filled.PinDrop,
                        contentDescription = "Lokasi"
                    )
                    Text(
                        text = gereja.lokasi,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row (modifier = Modifier.padding(top = 10.dp)) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "Kalender"
                    )
                    Text(
                        text = gereja.jadwal,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun TopApp(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box {
            Image(
                painter = painterResource(R.drawable.merpati),
                contentDescription = "Andes Mountain",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(600.dp)
                    .height(100.dp)
                    .padding(start = 300.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 150.dp, top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Praise",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 24.sp
            )
            Text(
                text = "The Lord,",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 24.sp
            )
            Text(
                text = "Home",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
        ) {
            // Field pencarian dengan label dan ikon search
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 8.dp),
                label = { Text("Search", fontSize = 18.sp) },
                trailingIcon = {
                    IconButton(onClick = { onSearch(searchText) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Gray
                        )
                    }
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProjectTingkat2Theme {
        HomeScreen(rememberNavController())
    }
}