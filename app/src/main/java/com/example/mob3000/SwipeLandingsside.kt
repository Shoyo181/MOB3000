package com.example.mob3000

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.mob3000.AuthService.logginnBruker
import com.example.mob3000.AuthService.registrerBruker
import com.example.mob3000.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

data class Bruker (
    val id: String = "",
    val email: String = ""
)

@Composable
fun SwipeLandingsside(
    onLoginSuccess: () -> Unit
) {
    var showLoginnVindu by remember { mutableStateOf(false) }

    val pages by remember {mutableStateOf(listOf(
        "Opprett en bedriftsbruker eller logg inn hvis du allerede har opprettet din bruker. Lag profiler over dine ansatte og kom i gang med å sammenligne personlighetstrekk ved dine ansatte.",
        "Få dine ansatte til å ta en Big Five personlighetstest fra Ruby Nor. Personlighetstesten er vitenskapelig og basert på 5 ulike punkter: Nevrotisisme, planmessighet, medmenneskelighet, ekstraversjon og åpenhet for erfaringer. ",
        "Etter at ansatte tar testen bruk ID fra testen inn i appen og få oversikt over de ulike resultatene fra hver profil, og sammenlign ansatte og skap gode team i bedriften din."
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
                color = Color(0xFF817A81),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showLoginnVindu) {
            LoginDialog(
                onDismiss = {showLoginnVindu = false},
                onLoginSuccess = {
                    onLoginSuccess()
                }
            )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDialog(onDismiss: () -> Unit, onLoginSuccess: () -> Unit) {
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
                    text = "Logg inn eller registrer",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth(),
                     colors = TextFieldDefaults.outlinedTextFieldColors(
                         containerColor = Color.White
                     )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.trim() },
                    label = { Text("Passord") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White
                    ),
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
                Button(
                    onClick = {
                        registrerBruker(email, password,
                            onSuccess = {
                                onLoginSuccess
                            },
                            onFailure = {errorMessage = it}
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66433F))
                ) {
                    Text("Registrer", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        logginnBruker(email, password,
                            onSuccess = {
                                onLoginSuccess()
                            },
                            onFailure =  { errorMessage = it })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66433F))
                ) {
                    Text("Logg inn", style = MaterialTheme.typography.labelLarge)
                }
            }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66433F))
                ) {
                    Text("Avbryt")
                }
            }
        }
    )
}

