@file:OptIn(ExperimentalMaterialApi::class)

package com.bishal.lazyreader.presentation.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.domain.model.Item

@Composable
fun ReaderDetailsScreen(
    navController: NavController,
    viewModel: ReaderDetailViewModel = hiltViewModel(),
    bookId: String
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
                boookImage = bookInfo.data?.volumeInfo?.imageLinks?.thumbnail,
                imageFraction = currentSheetFraction,
                bookInfo = bookInfo,
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
        }
    )

}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavController
) {


}

@Composable
fun BackgroundContent(
    boookImage: String?,
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
                    tint = Color.White
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