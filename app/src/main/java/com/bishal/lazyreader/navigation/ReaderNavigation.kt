package com.bishal.lazyreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bishal.lazyreader.presentation.screens.details.ReaderDetailsScreen
import com.bishal.lazyreader.presentation.screens.home.ReaderHomeScreen
import com.bishal.lazyreader.presentation.screens.login.ReaderLoginScreen
import com.bishal.lazyreader.presentation.screens.lottie.ReaderLottieScreen
import com.bishal.lazyreader.presentation.screens.search.ReaderSearchScreen
import com.bishal.lazyreader.presentation.screens.stats.ReaderStatsScreen
import com.bishal.lazyreader.presentation.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReaderScreen.LottieScreen.name,

    ) {
        composable(ReaderScreen.LottieScreen.name) {
            ReaderLottieScreen(navController = navController)
        }
        composable(ReaderScreen.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderHomeScreen.name) {
            ReaderHomeScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreen.SearchScreen.name) {
            ReaderSearchScreen(navController = navController)
        }
        composable(ReaderScreen.DetailScreen.name) {
            ReaderDetailsScreen(navController = navController)
        }
        composable(ReaderScreen.UpdateScreen.name) {
            BookUpdateScreen(navController = navController)
        }
    }
}