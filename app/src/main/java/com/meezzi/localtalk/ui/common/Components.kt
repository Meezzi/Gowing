package com.meezzi.localtalk.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextTitleLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}