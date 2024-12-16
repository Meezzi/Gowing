package com.meezzi.localtalk.ui.intro.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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
fun WelcomeText() {
    Text(
        text = stringResource(R.string.welcome_text),
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.headlineLarge
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = stringResource(R.string.gowing),
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F2D89),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
private fun SplashLogoAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pigeon_flying_animation))
    val logoAnimationState = animateLottieCompositionAsState(composition = composition)

    LottieAnimation(
        composition = composition,
        progress = { logoAnimationState.progress },
        modifier = Modifier.size(300.dp)
    )
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