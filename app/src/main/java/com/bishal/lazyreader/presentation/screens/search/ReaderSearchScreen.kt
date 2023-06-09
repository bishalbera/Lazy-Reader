@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.bishal.lazyreader.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.presentation.common.InputField
import com.bishal.lazyreader.presentation.common.ReaderAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderSearchScreen(
    navController: NavController,
    viewModel: ReaderSearchScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                navController = navController,
                showProfile = false,
                icon = Icons.Default.ArrowBack
            ){
                navController.popBackStack()
            }

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
        Surface {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){ searchQuery ->
                    viewModel.searchBooks(query = searchQuery)

                }
                Spacer(modifier = Modifier.height(13.dp))


            }

        }

    }


}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }


        InputField(valueState = searchQueryState,
            modifier = modifier.padding(vertical = 45.dp),
            labelId = "Search",
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
    }



}

