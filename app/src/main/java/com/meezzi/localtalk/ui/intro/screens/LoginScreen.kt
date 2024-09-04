package com.meezzi.localtalk.ui.intro.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.meezzi.localtalk.R

@Composable
fun LoginScreen(
    onSignInClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SignInGoogleButton(onClick = onSignInClick)
        }
    }
}

@Composable
fun SignInGoogleButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.ic_google_sign_in),
        contentDescription = stringResource(R.string.sign_in_with_google),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .clickable(onClick = onClick)
    )
}