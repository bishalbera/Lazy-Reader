@file:OptIn(ExperimentalMaterialApi::class)

package com.bishal.lazyreader.presentation.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.ApiClient
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.domain.model.Item
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.presentation.common.RoundedButton
import com.bishal.lazyreader.util.Constants
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ReaderDetailsScreen(
    navController: NavController,
    viewModel: ReaderDetailViewModel = hiltViewModel(),
    bookId: String,

) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )
    val currentSheetFraction = scaffoldState.currentSheetFraction

    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
        value = viewModel.getBookInfo(bookId)
    }.value

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            topStart = 11.dp,
            topEnd = 11.dp
        ),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 140.dp,
        sheetContent = {

            ShowBookDetails(
                bookInfo = bookInfo,
                navController = navController
            )
        },
        content = {
            BackgroundContent(
                imageFraction = currentSheetFraction,
                bookInfo = bookInfo
            ) {
                navController.popBackStack()
            }
        }
    )

}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavController,
    //sheetBackgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id
    val brushColor: List<Color> = listOf( Color(0xff227C70),
        Color(0xffC92C6D))

    Surface(modifier = Modifier
        .fillMaxWidth()


    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = brushColor, startY = 30f, endY = 830f
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = bookData?.title.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
            Text(
                text = "Authors: ${bookData?.authors.toString()}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.background
            )
            Text(
                text = "Published On: ${bookData?.publishedDate.toString()}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )


            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)

                    )

            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "star",
                        tint = Color(0xffFFD93D)

                    )
                    Text(text = bookData?.averageRating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(15.dp))


                    Text(text = "PageCount: ${bookData?.pageCount.toString()}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(text = "Categories: ${bookData?.categories.toString()}",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                }
            }

            val cleanDescription = HtmlCompat.fromHtml(
                bookData?.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            Text(
                text = "Description:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp))
            Text(
                text = cleanDescription,
                modifier = Modifier
                    .padding(12.dp),
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
            )

            val context = LocalContext.current

            val client = remember {
                ApiClient.createClient(context)
            }
            var userID by remember { mutableStateOf("") }
            LaunchedEffect(key1 = client) {
                try {
                    val id = Account(client = client).get().id
                    userID = id
                } catch (e: AppwriteException) {
                    Log.e("Appwrite", "Error getting user ID: ${e.message}")
                }



            }
            //Buttons
            Row(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround) {
                RoundedButton(label = "Save"){
                    //save this book to the appwrite database
                    val book = MBook(
                        title = bookData?.title,
                        authors = bookData?.authors.toString(),
                        description = bookData?.description,
                        categories = bookData?.categories.toString(),
                        notes = "",
                        photoUrl = bookData?.imageLinks?.thumbnail,
                        publishedDate = bookData?.publishedDate,
                        pageCount = bookData?.pageCount.toString(),
                        rating = 0.0.toInt(),
                        googleBookId = googleBookId,
                        userId = userID)

                    saveToAppwrite( navController = navController, context = context, book = book)



                }
                Spacer(modifier = Modifier.width(25.dp))
                RoundedButton(label = "Cancel"){
                    navController.popBackStack()
                }

            }


        }


    }


}

@Composable
fun BackgroundContent(
    imageFraction: Float,
    bookInfo: Resource<Item>,
    onCloseClicked: () -> Unit,
) {


    val bookData = bookInfo.data?.volumeInfo
    val imageUrl = bookData?.imageLinks?.thumbnail
    val painter = rememberAsyncImagePainter(model = imageUrl)


    Box(modifier = Modifier
        .fillMaxSize()

    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = imageFraction + 0.8f)
                .align(Alignment.TopStart),
            painter = painter,
            contentDescription = "book image",
            contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier
                    .padding(10.dp),
                onClick = { onCloseClicked() }
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onTertiary
                )

            }

        }
    }


}

val BottomSheetScaffoldState.currentSheetFraction: Float
    get() {

        return when (val progress = bottomSheetState.progress) {
            0f -> 1f
            1f -> 0f
            else -> 1f - progress
        }
    }

@SuppressLint("CoroutineCreationDuringComposition")

fun saveToAppwrite(book: MBook?, navController: NavController, context: Context) {
    book?.let {
        val client: Client = ApiClient.createClient(context = context)
        val databases = Databases(client)
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            try {
                val data = it.toHashMap() as Map<String, String>
                if (data.isNotEmpty() ) {
                    Log.d("Appwrite", "Data: $data") // debug log
                    databases.createDocument(
                        collectionId = Constants.bookCollection_Id,
                        databaseId = Constants.database_Id,
                        documentId = ID.unique(),
                        data = data
                    )
                    navController.popBackStack()
                } else {
                    // Handle empty data here
                    Log.e("Appwrite", "Error: data is empty")
                }
            } catch (e: AppwriteException) {
                Log.e("Appwrite", "Error: " + e.message, e)
            }
        }
    }
}