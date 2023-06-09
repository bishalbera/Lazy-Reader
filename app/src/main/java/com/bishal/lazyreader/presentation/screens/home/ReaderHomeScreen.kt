@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.presentation.components.TitleSection

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
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeContent(navController)

        }

    }


}

@Composable
fun HomeContent(
    navController: NavController
) {
    var listOfBooks = listOf<MBook>()


    Column(
        modifier = Modifier
            .padding(top = 65.dp),
        verticalArrangement = Arrangement.Top

    ) {
        Row(
            modifier = Modifier
            .align(alignment = Alignment.Start)
        ) {
            TitleSection(label = "Currently Reading")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))



        }


    }





}
