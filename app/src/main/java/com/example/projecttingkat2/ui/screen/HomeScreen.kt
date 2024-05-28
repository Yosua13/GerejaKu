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
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.model.Gereja
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.GerejaViewModelFactory
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val repository = GerejaRepository()
    val factory = GerejaViewModelFactory(repository)
    val viewModel: GerejaDetailViewModel = viewModel(factory = factory)
    val gerejaList by viewModel.gerejaList.collectAsState()

    Scaffold(
        bottomBar = { HomeBottomNavigation(navHostController) },
        floatingActionButton = {
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
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            items(gerejaList) { gereja ->
                GerejaCard(gereja = gereja) {
                    navHostController.navigate(Screen.GerejaFormUbah.gerejaId(gereja.id))
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
            selected = true,
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
            selected = false,
            onClick = {
                navHostController.navigate(Screen.Profil.route)
            }
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
                contentDescription = gereja.judul,
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
                        text = gereja.judul,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = gereja.aliran,
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
fun HomeScreenPreview() {
    ProjectTingkat2Theme {
        HomeScreen(rememberNavController())
    }
}