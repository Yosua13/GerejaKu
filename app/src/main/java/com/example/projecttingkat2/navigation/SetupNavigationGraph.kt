package com.example.projecttingkat2.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projecttingkat2.MainActivity
import com.example.projecttingkat2.model.User
import com.example.projecttingkat2.ui.screen.BeritaAcaraScreen
import com.example.projecttingkat2.ui.screen.BookScreen
import com.example.projecttingkat2.ui.screen.HomeScreen
import com.example.projecttingkat2.ui.screen.LoginRegisterScreen
import com.example.projecttingkat2.ui.screen.OnboardingScreen
import com.example.projecttingkat2.ui.screen.ProfilScreen
import com.example.projecttingkat2.ui.screen.detail.DetailBeritaAcaraScreen
import com.example.projecttingkat2.ui.screen.detail.DetailBukuScreen
import com.example.projecttingkat2.ui.screen.detail.DetailGerejaScreen
import com.example.projecttingkat2.ui.screen.detail.KEY_ID_BERITA
import com.example.projecttingkat2.ui.screen.detail.KEY_ID_BUKU
import com.example.projecttingkat2.ui.screen.detail.KEY_ID_GEREJA
import com.example.projecttingkat2.ui.screen.userscreen.GerejaPengguna
import com.example.projecttingkat2.ui.screen.userscreen.KEY_ID_GEREJA_PENGGUNA

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavigationGraph(navHostController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.LandingPage.route) {
            OnboardingScreen(navHostController)
        }

        composable(route = Screen.LoginRegister.route) {
            LoginRegisterScreen(navHostController)
        }

        composable(route = Screen.Home.route) {
            HomeScreen(navHostController)
        }
        composable(route = Screen.GerejaFormBaru.route) {
            DetailGerejaScreen(navHostController)
        }
        composable(
            route = Screen.GerejaFormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_GEREJA) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(KEY_ID_GEREJA)
            DetailGerejaScreen(navHostController, id)
        }
        composable(
            route = Screen.GerejaPengguna.route,
            arguments = listOf(
                navArgument(KEY_ID_GEREJA_PENGGUNA) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(KEY_ID_GEREJA_PENGGUNA)
            GerejaPengguna(navHostController, viewModel(), id)
        }

        composable(route = Screen.Book.route) {
            BookScreen(navHostController)
        }
        composable(route = Screen.BukuFormBaru.route) {
            DetailBukuScreen(navHostController)
        }
        composable(
            route = Screen.BukuFormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_BUKU) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(KEY_ID_BUKU)
            DetailBukuScreen(navHostController, id)
        }
        composable(route = Screen.BeritaAcara.route) {
            BeritaAcaraScreen(navHostController)
        }
        composable(route = Screen.BeritaFormBaru.route) {
            DetailBeritaAcaraScreen(navHostController)
        }
        composable(
            route = Screen.BeritaFormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_BERITA) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(KEY_ID_BERITA)
            DetailBeritaAcaraScreen(navHostController, id)
        }
        composable(route = Screen.Profil.route) {
            ProfilScreen(navHostController)
        }
    }
}