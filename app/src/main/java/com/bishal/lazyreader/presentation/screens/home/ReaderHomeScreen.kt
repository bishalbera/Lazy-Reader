@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.presentation.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.navigation.ReaderScreen
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

        ReadingRightNowArea(
            listOfBooks = listOfBooks,
            navController = navController
        )

        TitleSection(label = "Reading List")

        BookListArea(
            listOfBooks = listOfBooks,
            navController = navController
        )


    }





}

@Composable
fun BookListArea(
    listOfBooks: List<MBook>,
    navController: NavController
) {
   val addedBooks = listOfBooks.filter { mBook ->
   mBook.startedReading == null && mBook.finishedReading == null

   }
    HorizontalScrollableComponent(addedBooks){
        Log.d("TAG", "BookListArea: $it")
        navController.navigate(ReaderScreen.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    onCardPressed: () -> Unit
) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)
    ) {
        if (listOfBooks.isEmpty()) {
            Surface(
                modifier = Modifier
                    .padding(23.dp)
            ) {
                Text(
                    text = "No books found, Add a book",
                    style = TextStyle(color = Color.Red.copy(alpha = 0.4f)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        } else{
            for (book in listOfBooks) {

            }
        }

    }

}

@Composable
fun ReadingRightNowArea(
    listOfBooks: List<MBook>,
    navController: NavController
) {
    //Filter books by reading now
    val readingNowList = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }


}
