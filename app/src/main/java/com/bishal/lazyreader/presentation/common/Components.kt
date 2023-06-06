@file:OptIn(ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.presentation.common

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bishal.lazyreader.ApiClient
import com.bishal.lazyreader.navigation.ReaderScreen
import com.bishal.lazyreader.presentation.screens.login.signOut
import io.appwrite.Client
import kotlinx.coroutines.launch

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )

}

@Composable
fun InputField(
    modifier: Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onAction: KeyboardActions
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = { Text(text = labelId)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Transparent,
            cursorColor = Color(0xFFE69360),
            textColor = Color(0xFFE69360),
            disabledBorderColor = Color(0xFF203226),
            focusedBorderColor = Color(0xFF203226),
            focusedLabelColor = Color(0xFFE69360),
            unfocusedLabelColor = Color(0xFFE69360)


        ),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp
            ),
        modifier = modifier
            .padding(
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Transparent,
            cursorColor = Color(0xFFE69360),
            textColor = Color(0xFFE69360),
            disabledBorderColor = Color(0xFF203226),
            focusedBorderColor = Color(0xFF203226),
            focusedLabelColor = Color(0xFFE69360),
            unfocusedLabelColor = Color(0xFFE69360)
        ),
        singleLine = true,
        modifier = modifier
            .padding(
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth(),
        enabled = enabled,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
            ),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close
    }
}

@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    application: Application? = null,
    onBackArrowClicked:() -> Unit = {}
) {
    val client = ApiClient.createClient(context = application!!)
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Row( verticalAlignment = Alignment.CenterVertically) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "logo",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.9f)
                    )
                }
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "arrowBack",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier
                            .clickable { onBackArrowClicked.invoke() }
                    )
                }
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = title,
                    color = Color.Red.copy(alpha = 0.7f),
                    style = TextStyle(fontWeight = FontWeight.Bold,
                        fontSize = 20.sp))

    }},
    actions = {
        if (application != null) { // Check if the application parameter is not null
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        signOut(client )
                        navController.navigate(ReaderScreen.LoginScreen.name)
                    }
                }
            ) {
                if (showProfile) Row() {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "log out"
                    )
                } else Box {}
            }
        }
    },
        modifier = Modifier.background(Color.Transparent))

}
@Composable
fun createClient(context: Context, endpoint: String, projectId: String): Client {
    return Client(context = context)
        .setEndpoint(endpoint)
        .setProject(projectId)
}
