@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.presentation.common.ReaderAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(navController: NavController) {



    Scaffold(
        topBar = {
                 ReaderAppBar(
                     title = "Lazy Reader",
                     navController = navController,

                 )

        },
        bottomBar = {
            BottomBar(
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )

        }
    ) {

    }


}