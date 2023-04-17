package com.example.composepractice.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composepractice.ui.theme.welcomeAssets

@Composable
fun WelcomePage(navigation: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        Image(
            painter = rememberVectorPainter(image = ImageVector.vectorResource(id = MaterialTheme.welcomeAssets.background)),
            contentDescription = "weclome_bg",
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = MaterialTheme.welcomeAssets.illos)),
                contentDescription = "weclome_illos",
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 48.dp, start = 88.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberVectorPainter(image = ImageVector.vectorResource(id = MaterialTheme.welcomeAssets.logo)),
                    contentDescription = "weclome_logo",
                    modifier = Modifier.wrapContentSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = "Beautiful home garden solutions",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(
                        text = "Create account",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
                TextButton(
                    onClick = { TouristGuide.toLogin() },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(48.dp),
                ) {
                    Text(
                        text = "Log in",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun WelcomePageLightPreview() {
//    BloomTheme() {
//        WelcomePage()
//    }
//}
//
//@Preview
//@Composable
//fun WelcomePageDarkPreview() {
//    BloomTheme() {
//        WelcomePage()
//    }
//}