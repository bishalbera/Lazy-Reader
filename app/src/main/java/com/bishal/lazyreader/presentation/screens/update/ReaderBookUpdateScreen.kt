package com.bishal.lazyreader.presentation.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.presentation.screens.home.HomeScreenViewModel
import com.bishal.lazyreader.util.formatDate

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

@Composable
fun ShowSimpleForm(
    book: MBook,
    navController: NavHostController
) {
    val context = LocalContext.current

    val notesText = remember { mutableStateOf("") }

    val isStartedReading = remember { mutableStateOf(false) }

    val isFinishedReading = remember { mutableStateOf(false) }

    val ratingVal = remember { mutableStateOf(0) }

    SimpleForm(defaultValue = book.notes.toString().ifEmpty { "No thoughts available :(" }){ note ->
        notesText.value = note

    }
    
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(
            onClick = { },
        enabled = book.startedReading == null) {
            if (book.startedReading == null) {
                if (!isStartedReading.value) {
                    Text(text = "Started Reading")
                } else {
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Started On: ${formatDate(book.startedReading!!)}")
            }
            
        }

        
    }

}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String
) {
    Row() {
        Spacer(modifier = Modifier.width(43.dp))
        if (bookInfo.data != null) {
            val matchingBook = bookInfo.data!!.find { mBook ->
                mBook.googleBookId == bookItemId
            }

            if (matchingBook != null) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    CardListItem(book = matchingBook, onPressDetails = {})
                }
            } else {

            }
        }
    }

}

@Composable
fun CardListItem(
    book: MBook,
    onPressDetails: () -> Unit
) {
    Card(modifier = Modifier
        .padding(
            start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp
        )
        .clip(RoundedCornerShape(20.dp))
        .clickable { },
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(painter = rememberAsyncImagePainter(model = book.photoUrl.toString()),
                contentDescription = null ,
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                        )
                    ))
            Column {
                Text(text = book.title.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)

                Text(text = book.authors.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 8.dp,
                        end = 8.dp,
                        top = 2.dp,
                        bottom = 0.dp))

                Text(text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 8.dp))

            }

        }


    }

}
