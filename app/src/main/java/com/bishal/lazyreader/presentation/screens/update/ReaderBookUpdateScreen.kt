@file:OptIn(ExperimentalComposeUiApi::class)

package com.bishal.lazyreader.presentation.screens.update

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.ApiClient
import com.bishal.lazyreader.R
import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.navigation.ReaderScreen
import com.bishal.lazyreader.presentation.common.InputField
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.presentation.common.RoundedButton
import com.bishal.lazyreader.presentation.components.RatingBar
import com.bishal.lazyreader.presentation.screens.home.HomeScreenViewModel
import com.bishal.lazyreader.util.Constants
import com.bishal.lazyreader.util.formatDate
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowSimpleForm(
    book: MBook,
    navController: NavHostController
) {
    val context = LocalContext.current
    val client = ApiClient.createClient(context = context)

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
            onClick = { isStartedReading.value = true },
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
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = { isFinishedReading.value = true },
            enabled = book.finishedReading == null) {
            if (book.finishedReading == null) {
                if (!isFinishedReading.value) {
                    Text(text = "Mark as Read")
                } else {
                    Text(text = "Finished Reading!")
                }
            } else {
                Text(text = "Finished On: ${formatDate(book.finishedReading!!)}")
            }

        }

        
    }
    Text(
        text = "Rating", modifier = Modifier.padding(3.dp))
    book.rating?.let {
        RatingBar(rating = it!!){ rating->
            ratingVal.value = rating
            Log.d("TAG", "ShowSimpleForm: ${ratingVal.value}")

        }
    }


    Row {
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.from(Instant.now()) else book.finishedReading
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.from(Instant.now()) else book.startedReading

        val bookUpdate = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()
        var userID by remember { mutableStateOf("") }
        LaunchedEffect(key1 = client) {
            try {
                val id = Account(client = client).get().id
                userID = id
            } catch (e: AppwriteException) {
                Log.e("Appwrite", "Error getting user ID: ${e.message}")
            }



        }

        RoundedButton(label = "Update"){
            if (bookUpdate) {
                val databases = Databases(client)
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch{
                    try {
                         databases.updateDocument(
                            databaseId = Constants.database_Id,
                            collectionId = Constants.bookCollection_Id,
                            documentId = ID.unique(),
                            data = bookToUpdate
                        )
                        navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                    } catch (e: AppwriteException) {
                        Log.w("Error", "error updating doc")
                    }

                }


            }

        }
        Spacer(modifier = Modifier.width(100.dp))
        val openDialog = remember { mutableStateOf(false) }

        if (openDialog.value) {
            ShowAlertDialog(message = stringResource(id = R.string.sure) + "\n" +
            stringResource(id = R.string.action), openDialog) {
                val databases = Databases(client)
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch{
                    try {
                        databases.deleteDocument(
                            databaseId = Constants.database_Id,
                            collectionId = Constants.bookCollection_Id,
                            documentId = userID,

                        )
                        openDialog.value = false
                        navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                    } catch (e: AppwriteException) {
                        Log.w("Error", "error deleting doc")
                    }

                }

            }
        }
        RoundedButton("Delete") {
            openDialog.value = true
        }
    }

}

@Composable
fun ShowAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Delete Book") },
            text = { Text(text = message) },
            confirmButton = {
                Button(
                    onClick = {
                        onYesPressed.invoke()
                        openDialog.value = false // Close the dialog after pressing "Yes"
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text(text = "No")
                }
            }
        )
    }

}

@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book!",
    onSearch: (String) -> Unit
) {
    Column {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) { textFieldValue.value.trim().isNotEmpty() }

        InputField(
            modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color(0xff867070), CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Enter Your thoughts",
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                if (!valid)return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            })

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
