package com.example.mob3000

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mob3000.ui.components.SwipeLandingsside.Dot
import com.example.mob3000.ui.components.SwipeLandingsside.LoggInnDialog
import com.example.mob3000.ui.theme.Typography


/**
 * Denne komponenten vil bruke se hvis de ikke er innlogget
 * Komponenten gir brukeren mulighet til å logge inn eller registrere seg
 * Vil også vise frem litt informasjon om applikasjonen
 * Bruker Dot og LoggInnDialog komponentene
 */
@Composable
fun SwipeLandingsside(
    onLoginSuccess: () -> Unit
) {
    var showLoggInnVindu by remember { mutableStateOf(false) }

    val pages = (listOf(
        stringResource(id = R.string.pages_swipe_1),
        stringResource(id = R.string.pages_swipe_2),
        stringResource(id = R.string.pages_swipe_3)
    ))
    val pagerState = rememberPagerState(
        pageCount = {pages.size}
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(colorResource(id = R.color.sand), (colorResource(id = R.color.dusk)))
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // pager for swiping av tekst
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 100.dp, 10.dp, 10.dp)
            ) { page ->
                Text(
                    text = pages[page],
                    textAlign = TextAlign.Center,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.SemiBold ),
                    color = colorResource(id = R.color.maghogny),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            //page indikator
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                repeat(pages.size) { index ->
                    Dot(isSelected = pagerState.currentPage == index)
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            // logg inn/registrer knapp m. funksjonalitet
            Button(
                onClick = { showLoggInnVindu = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.maghogny))
            ) {
                Text(stringResource(id = R.string.button_loginregister), color = colorResource(id = R.color.sand),
                    style = Typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // footer
            Text(
                text = "Copyright (c) 2024 KJAMIE. All rights reserved.",
                color = colorResource(id = R.color.dusk),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showLoggInnVindu) {
            LoggInnDialog(
                onDismiss = {showLoggInnVindu = false},
                onLoginSuccess = {
                    onLoginSuccess()
                }
            )
        }
    }
}



