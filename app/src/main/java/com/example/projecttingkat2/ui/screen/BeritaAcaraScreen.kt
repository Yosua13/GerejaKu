package com.example.projecttingkat2.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.example.projecttingkat2.firebase.BeritaAcaraRepository
import com.example.projecttingkat2.model.BeritaAcara
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.BeritaAcaraViewModelFactory
import com.example.projecttingkat2.viewmodel.BeritaAcaraDetailViewModel
import com.example.projecttingkat2.viewmodel.UserViewModel

@Composable
fun BeritaAcaraScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()

    val repository = BeritaAcaraRepository()
    val factory = BeritaAcaraViewModelFactory(repository)
    val viewModel: BeritaAcaraDetailViewModel = viewModel(factory = factory)
    val beritaAcaraList by viewModel.beritaList.collectAsState()


    currentUser?.let { user ->
        Scaffold(
            bottomBar = { BeritaBottomNavigation(navHostController) },
            floatingActionButton = {
                if (user.role == "Gereja") {
                    FloatingActionButton(
                        onClick = {
                            navHostController.navigate(Screen.BeritaFormBaru.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tambah Berita Acara",
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
                items(beritaAcaraList) { berita ->
                    BeritaAcaraCard(beritaAcara = berita) {
                        if (user.role == "Gereja") {
                            navHostController.navigate(Screen.BeritaFormUbah.beritaId(berita.id))
                        } else if (user.role == "Pengguna") {
                            navHostController.navigate(Screen.BeritaPengguna.beritaPenggunaId(berita.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BeritaBottomNavigation(
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
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.utama)
                )
            },
            selected = false,
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
            selected = true,
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
fun BeritaAcaraCard(
    beritaAcara: BeritaAcara,
    onClick: () -> Unit,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(beritaAcara.gambarBerita)
        .crossfade(true)
        .error(R.drawable.ic_launcher_foreground)  // Add a placeholder for error
        .build()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 28.dp, top = 28.dp)
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
                contentDescription = beritaAcara.judul,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            )
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
            ) {
                Column {
                    Text(
                        text = beritaAcara.judul,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = beritaAcara.pembicara,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeritaAcaraScreenPreview() {
    ProjectTingkat2Theme {
        BeritaAcaraScreen(rememberNavController())
    }
}