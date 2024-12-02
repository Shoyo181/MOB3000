package com.example.mob3000

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mob3000.data.firebase.AuthService.logginnBruker
import com.example.mob3000.data.firebase.AuthService.registrerBruker
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.OutlinedTextFieldKomponent
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

/**
 * Hjelpe komponent for å lage en indikator for hver side i swipingen
 */
@Composable
fun Dot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(
                color = if (isSelected) Color.White else Color.LightGray,
                shape = CircleShape
            )
    )
}

/**
 * Komponenten er for innlogging eller registrering
 * Valgte å ikke lage dette super komplisert og la disse funksjonene sammen
 * Bruker AuthServices.kt for å logge inn og registrere bruker
 * Bruker OutLinedTextFieldKomonent.kt (våres) for å lage tekstfelt
 * Bruker AlertDialog og OutlinedTextField fra Material Design for å lage dialogvinduet
 *
 *  @param onDismiss Callback for når vinduet lukkes
 *  @param onLoginSuccess Callback for når innlogging er vellykket
 */
@Composable
fun LoggInnDialog(onDismiss: () -> Unit, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.alert_login_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        text = {
            Column {
                OutlinedTextFieldKomponent(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = stringResource(id = R.string.email),
                )
                // Passord, lagt til slik at passord ikke syntes, derfor brukes ikke gjenbrukbar komponent
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.trim() },
                    label = { Text(stringResource(id = R.string.alert_login_passord)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.ivory),
                        unfocusedContainerColor = colorResource(id = R.color.ivory)),
                    visualTransformation = PasswordVisualTransformation()
                )
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = Color.Red)
                }
            }
        },
        confirmButton = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                ButtonKomponent(
                    text = stringResource(id = R.string.alert_login_register),
                    onClick = {
                        registrerBruker(email, password,
                            onSuccess = {
                                onLoginSuccess
                            },
                            onFailure = {errorMessage = it}
                        )
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))

                ButtonKomponent(
                    text = stringResource(id = R.string.alert_login_login),
                    onClick = {
                        logginnBruker(email, password,
                            onSuccess = {
                                onLoginSuccess()
                            },
                            onFailure =  { errorMessage = it })
                    },
                )
            }
                Spacer(modifier = Modifier.height(5.dp))
                ButtonKomponent(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onDismiss() }
                )
            }
        },
        containerColor = colorResource(id = R.color.ivory)
    )
}

