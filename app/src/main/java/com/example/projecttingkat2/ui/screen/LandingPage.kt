package com.example.projecttingkat2.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projecttingkat2.R
import com.example.projecttingkat2.navigation.Screen
import com.example.projecttingkat2.ui.theme.ProjectTingkat2Theme

@Composable
fun LandingPage(navHostController: NavHostController) {
    Box(modifier = Modifier
    ) {
        // Image background
        Image(
            painter = painterResource(R.drawable.landing_page), // Replace with your background image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        // Column to center content with some padding
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp) // Add some padding
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            // Church name
            Text(
                text = "GerejaKu",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )

            // Spacer to add some space between text and button
            Spacer(modifier = Modifier.height(500.dp))

            // Button with text "Pelajari Lebih Lanjut" (Learn More)
            Text(
                text = "Selamat Datang di GerejaKu\nTempat di mana iman bertemu komunitas",
                color = Color.White
            )
            Button(
                onClick = { navHostController.navigate(Screen.LoginRegister.route) },
                colors = ButtonDefaults
                    .buttonColors(Color.White,contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Text(
                    text = "Mulai",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    ProjectTingkat2Theme {
        LandingPage(rememberNavController())
    }
}