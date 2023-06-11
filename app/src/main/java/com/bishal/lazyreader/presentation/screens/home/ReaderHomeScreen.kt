@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.presentation.screens.home

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bishal.lazyreader.ApiClient
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.navigation.ReaderScreen
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.presentation.components.ListCard
import com.bishal.lazyreader.presentation.components.TitleSection
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {


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
            HomeContent(navController, viewModel, context = LocalContext.current)

        }

    }


}

@Composable
fun HomeContent(
    navController: NavController,
    viewModel: HomeScreenViewModel,
    context: Context
) {
    var userID by remember { mutableStateOf("") }
    val client = remember {
        ApiClient.createClient(context)
    }
    LaunchedEffect(key1 = client) {
        try {
            val id = Account(client = client).get().id
            userID = id
        } catch (e: AppwriteException) {
            Log.e("Appwrite", "Error getting user ID: ${e.message}")
        }
    }
    var listOfBooks = listOf<MBook>()
    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.filter { mBook ->
            mBook.userId == userID


        }
        Log.d("Books", "Homecontent: $listOfBooks")
    }





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
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPressed: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)
    ) {
        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()
        }else{
            if (listOfBooks.isEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found, Add a book",
                        style = TextStyle(color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }else{
                for (book in listOfBooks) {
                    ListCard(book ) {
                        onCardPressed(book.googleBookId.toString())

                    }
                }
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

    HorizontalScrollableComponent(readingNowList){
        Log.d("TAG", "BoolListArea: $it")
        navController.navigate(ReaderScreen.UpdateScreen.name + "/$it")
    }

}
