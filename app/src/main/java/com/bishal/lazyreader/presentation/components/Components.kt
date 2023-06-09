package com.bishal.lazyreader.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(
        modifier = Modifier
            .padding(start = 5.dp, top = 1.dp)

    ) {
        Column {
            Text(
                text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left,

            )

        }

    }

}