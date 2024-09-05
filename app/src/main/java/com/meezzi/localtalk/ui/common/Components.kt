package com.meezzi.localtalk.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TextBodyMedium(text: String, color: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
    )
}

@Composable
fun TextTitleLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}