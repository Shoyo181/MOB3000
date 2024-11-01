package com.example.mob3000

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.mob3000.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuthInvalidUserException


@Composable
fun SwipeLandingsside() {
    var showLoginnVindu by remember { mutableStateOf(false) }

    val pages by remember {mutableStateOf(listOf(
        "Strålingen kan ha sterke kjemiske og biologiske virkninger, og brukes for å stimulere fotokjemiske og fotobiologiske prosesser. Ultrafiolett stråling er kjennetegnet ved selektiv absorpsjon i molekyler og atomer, det vil si at stråling innen bestemte bølgelengdeområder slipper gjennom noen stoffer og absorberes sterkt i andre.",
        "De instrumentene som nå vanligvis benyttes for å måle strålingsintensiteten, er fotodioder og fotomultiplikatorer. Ultrafiolett stråling kan også påvises med fotografisk film, ved fluorescens og med ioniseringskammer. Bølgelengden bestemmes spektroskopisk med glassprismer, gitterspektrometre og absorpsjonsspektrometre.",
        "Ultrafiolett stråling oppstår som termostråling fra legemer ved temperaturer over 3000 grader celsius (°C), ved gassutladninger, elektriske lysbuer (sveiseflammer) og som bremsestråling. Sollys inneholder ultrafiolett stråling, men bølgelengder under 290 nanometer absorberes fullstendig av ozonlaget i atmosfæren."
    ))}
    val pagerState = rememberPagerState(
        pageCount = {pages.size}
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEAD1BA), Color(0xFF817A81))
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
                    color = Color(0xFF66433F),
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
                onClick = { showLoginnVindu = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66433F))
            ) {
                Text("Logg inn / Registrer", color = Color(0xFFEAD1BA),
                    style = Typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // footer
            Text(
                text = "Copyright (c) 2024 KJAMIE. All rights reserved.",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showLoginnVindu) {
            LoginDialog(onDismiss = {showLoginnVindu = false})
        }
    }
}

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
@Composable
fun LoginDialog(onDismiss: () -> Unit) {

    // State variabler for email og passord input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(
            text = "Logg inn eller opprett bruker",
            style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column{
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Passord") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    AuthService.logginnBruker(email, password,
                        onSuccess = {
                            //Handler for det som skjer etter bruker har trykket logg inn, men tom for nå.
                            onDismiss() // Lukker dialogvindu.
                        },
                        onFailure = { exception ->
                            if (exception is FirebaseAuthInvalidUserException) {
                                AuthService.registrerBruker(email, password,
                                    onSuccess = {
                                        onDismiss()
                                    },
                                    onFailure = { registerExeption ->
                                    }
                                )
                            } else {
                            }
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF66433F)),
            ) {
                Text("Logg inn/Registrer",
                    style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF66433F)
                ),
            ) {
                Text("Avbryt",
                    style = MaterialTheme.typography.labelLarge)
            }

        }
    )
}

