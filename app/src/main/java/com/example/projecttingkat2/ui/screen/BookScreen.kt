package com.example.projecttingkat2.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.projecttingkat2.R
import com.example.projecttingkat2.firebase.BukuRepository
import com.example.projecttingkat2.model.Buku
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.BukuViewModelFactory
import com.example.projecttingkat2.viewmodel.BukuDetailViewModel

@Composable
fun BookScreen(navHostController: NavHostController) {

    val repository = BukuRepository()
    val factory = BukuViewModelFactory(repository)
    val viewModel: BukuDetailViewModel = viewModel(factory = factory)
    val bukuList by viewModel.bukuList.collectAsState()

    Scaffold(
        bottomBar = { BookBottomNavigation(navHostController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Screen.BukuFormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Tambah Buku",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = contentPadding,
        ) {
            items(bukuList) { buku ->
                BookCard(
                    buku = buku
                ) {
                    navHostController.navigate(Screen.BukuFormUbah.bukuId(buku.id))
                }
            }
        }
    }
}

@Composable
private fun BookBottomNavigation(
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
            selected = true,
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
fun BookCard(
    buku: Buku,
    onClick: () -> Unit,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(buku.gambar)
        .crossfade(true)
        .error(R.drawable.ic_launcher_foreground)  // Add a placeholder for error
        .build()

    Log.d("BookCard", "Loading image from URL: ${buku.gambar}")
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(175.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF))
    ) {
        Column {
            AsyncImage(
                model = imageRequest,
                contentDescription = buku.judul,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.small)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            )
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp)
                    .width(245.dp)
            ) {
                Text(
                    text = buku.judul,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = buku.penulis,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjectTingkat2Theme {
        BookScreen(rememberNavController())
    }
}