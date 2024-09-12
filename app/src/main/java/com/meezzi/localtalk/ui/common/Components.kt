package com.meezzi.localtalk.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.meezzi.localtalk.R

@Composable
fun TextTitleLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun CustomDialogLocation(
    onClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.permission_request_message),
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            HorizontalDivider(modifier = Modifier.padding(10.dp))

            Text(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.permission_location_title),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = stringResource(R.string.permission_location_content),
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp),
                onClick = onClick,
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFff7c80),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}