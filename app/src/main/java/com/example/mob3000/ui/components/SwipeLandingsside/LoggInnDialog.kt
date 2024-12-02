package com.example.mob3000.ui.components.SwipeLandingsside

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.firebase.AuthService.logginnBruker
import com.example.mob3000.data.firebase.AuthService.registrerBruker
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.OutlinedTextFieldKomponent

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

