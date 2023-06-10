package com.bishal.lazyreader.presentation.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.presentation.screens.home.HomeScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

Scaffold(
    topBar = {
        ReaderAppBar(
            title = "Update Book",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false
        ){
            navController.popBackStack()
        }

}) {
    val bookInfo = produceState<DataOrException<List<MBook>,
            Boolean,
            Exception>>(initialValue = DataOrException(data = emptyList(),
        true, Exception(""))){
        value = viewModel.data.value
    }.value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 23.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Log.d("INFO", "BookUpdateScreen: ${viewModel.data.value.data.toString()}")
            if (bookInfo.loading == true) {
                LinearProgressIndicator()
                bookInfo.loading = false
            } else {
                Surface(
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth(),
                    shape = CircleShape
                ) {
                    ShowBookUpdate(bookInfo = viewModel.data.value,
                    bookItemId = bookItemId)

                }

                val matchingBook = viewModel.data.value.data?.find { mBook ->
                mBook.googleBookId == bookItemId

                }

                if (matchingBook !=null) {
                    ShowSimpleForm(book = matchingBook, navController)
                } else {
                    Log.d("book", "this is not the same book")
                }
            }

        }

    }

}

}