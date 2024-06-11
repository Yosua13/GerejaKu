package com.example.projecttingkat2.ui.screen.userscreen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttingkat2.R
import com.example.projecttingkat2.firebase.PenggunaBukuRepository
import com.example.projecttingkat2.model.Buku
import com.example.projecttingkat2.util.PenggunaBukuViewModelFactory
import com.example.projecttingkat2.viewmodel.PenggunaBukuViewModel
import com.example.projecttingkat2.viewmodel.UserViewModel

const val KEY_ID_BUKU_PENGGUNA = "idBukuPengguna"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuPengguna(
    navHostController: NavHostController,
    id: String? = null,
) {

    val repository = PenggunaBukuRepository()
    val factory = PenggunaBukuViewModelFactory(repository)
    val viewModel: PenggunaBukuViewModel = viewModel(factory = factory)
    val buku by viewModel.buku.collectAsState()

    LaunchedEffect(id) {
        try {
            if (id != null) {
                Log.d("BukuPengguna", "LaunchedEffect triggered with ID: $id")
                viewModel.getBukuById(id)
            } else {
                Log.d("BukuPengguna", "No Buku ID provided, current buku ID: ${buku?.id}")
            }
        } catch (e: Exception) {
            Log.e("BukuPengguna", "Error in LaunchedEffect: ${e.message}", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { "" },
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent,
        content = { paddingValues ->
            buku?.let {
                Log.d("BukuPengguna", "Displaying Buku: ${it.judul}")
                PenggunaBuku(buku = it, paddingValues = paddingValues)
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.Transparent),  // Make the Box background transparent
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    )
}

@Composable
fun PenggunaBuku(
    buku: Buku,
    paddingValues: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        BackGroundPosterBuku(buku)
        ForegroundPosterBuku(buku)
        Column(
            Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                .align(Alignment.BottomCenter)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = buku.judul,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 38.sp,
                color = Color.White,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )
            RatingBuku(buku, modifier = Modifier)
            TextBuilderBuku(
                icon = Icons.Filled.Info,
                title = "Sinopsis Buku:",
                bodyText = buku.sinopsis
            )
        }
    }
}


@Composable
fun TextBuilderBuku(icon: ImageVector, title: String, bodyText: String) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = "Person",
            tint = Color.White
        )
        Text(
            text = title,
            Modifier.padding(start = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
    BasicText(
        text = AnnotatedString(bodyText),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        style = TextStyle(
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    )
}

@Composable
fun RatingBuku(buku: Buku, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier.fillMaxWidth(),
    ) {
        Row {
            Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = Color.White)
            Text(
                text = buku.penulis,
                modifier.padding(start = 6.dp),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(25.dp))
    }
}


@Composable
fun ForegroundPosterBuku(buku: Buku) {

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .width(250.dp)
            .padding(top = 80.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = buku.gambar, contentDescription = buku.judul,
            Modifier
                .width(250.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .width(250.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xB91A1B1B),
                        )
                    ), shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Composable
fun BackGroundPosterBuku(buku: Buku) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        AsyncImage(
            model = buku.gambar, contentDescription = buku.judul,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.6f)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.DarkGray
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}