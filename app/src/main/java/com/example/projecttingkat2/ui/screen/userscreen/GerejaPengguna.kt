package com.example.projecttingkat2.ui.screen.userscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.projecttingkat2.R
import com.example.projecttingkat2.firebase.GerejaRepository
import com.example.projecttingkat2.model.Gereja
import com.example.projecttingkat2.ui.screen.HomeScreen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme
import com.example.projecttingkat2.util.GerejaViewModelFactory
import com.example.projecttingkat2.viewmodel.GerejaDetailViewModel
import com.example.projecttingkat2.viewmodel.UserViewModel

const val KEY_ID_GEREJA_PENGGUNA = "idPengguna"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerejaPengguna(
    navHostController: NavHostController,
    gereja: Gereja,
    id: String? = null,
) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()

    val repository = GerejaRepository()
    val factory = GerejaViewModelFactory(repository)
    val viewModel: GerejaDetailViewModel = viewModel(factory = factory)
    val gerejaList by viewModel.gerejaList.collectAsState()

    val gerejaViewModel: GerejaDetailViewModel = viewModel()

    LaunchedEffect(Unit) {
        if (id != null) {
            gerejaViewModel.getGerejaById(id)
        }
    }

    // Ambil data gereja dari ViewModel
    val pengguna by gerejaViewModel.gereja.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gereja.judul) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        content = { paddingValues ->
            Pengguna(gereja = gereja, paddingValues)
        }
    )
}

@Composable
fun Pengguna(
    gereja: Gereja,
    paddingValues: PaddingValues
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(gereja.gambar)
        .crossfade(true)
        .error(R.drawable.ic_launcher_foreground)  // Tambahkan placeholder untuk error
        .build()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Image section
        AsyncImage(
            model = imageRequest,
            contentDescription = gereja.judul,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        )
        // Location and Price section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = gereja.judul,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = gereja.aliran,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gereja.lokasi,
                modifier = Modifier.padding(start = 16.dp),
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gereja.jadwal,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "About",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = gereja.deskripsi,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GerejaPenggunaPreview() {
    ProjectTingkat2Theme {
        GerejaPengguna(rememberNavController(), gereja = Gereja())
    }
}