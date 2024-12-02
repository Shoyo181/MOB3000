package com.example.mob3000.ui.components.PersonListe

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.mob3000.R
import com.example.mob3000.data.models.Person
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.OutlinedTextFieldKomponent
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LeggTilPerson(
    visLeggTil: Boolean,
    onDismiss: () -> Unit,
    onLeggTilPerson: (Person) -> Unit
) {
    if (visLeggTil) {
        var newPersonNavn by remember { mutableStateOf("") }
        var newPersonAlder by remember { mutableStateOf("") }
        var newPersonEmail by remember { mutableStateOf("") }
        var newTestID by remember { mutableStateOf("") }
        var newPersonStilling by remember { mutableStateOf("") }
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        var feilInput by remember { mutableStateOf(false) }


        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                ButtonKomponent(
                    text = stringResource(id = R.string.alert_createprofile_create),
                    onClick = {
                        feilInput = false
                        if (newPersonNavn.isNotEmpty() && newPersonAlder.isNotEmpty() &&
                            newPersonEmail.isNotEmpty() && newPersonEmail.matches(regex) &&
                            newTestID.isNotEmpty()
                        ) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val nyPerson = Person(
                                name = newPersonNavn,
                                age = newPersonAlder,
                                email = newPersonEmail,
                                testid = newTestID,
                                stilling = newPersonStilling,
                                userId = currentUser?.uid ?: ""
                            )
                            onLeggTilPerson(nyPerson)
                            onDismiss()
                        } else {
                            feilInput = true
                        }
                    }
                )
            },
            dismissButton = {
                ButtonKomponent(
                    text = stringResource(id = R.string.cancel),
                    onClick = onDismiss
                )
            },
            title = { Text(stringResource(id = R.string.alert_createprofile_title)) },
            text = {
                Column {
                    OutlinedTextFieldKomponent(
                        value = newPersonNavn,
                        onValueChange = { newPersonNavn = it },
                        label = stringResource(id = R.string.name)
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonAlder,
                        onValueChange = { newPersonAlder = it },
                        label = stringResource(id = R.string.age)
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonEmail,
                        onValueChange = { newPersonEmail = it },
                        label = stringResource(id = R.string.email)
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonStilling,
                        onValueChange = { newPersonStilling = it },
                        label = stringResource(id = R.string.stilling)
                    )
                    OutlinedTextFieldKomponent(
                        value = newTestID,
                        onValueChange = { newTestID = it },
                        label = stringResource(id = R.string.testid)
                    )

                    if (feilInput){
                        Text(
                            text = stringResource(R.string.wrong),
                            color = colorResource(R.color.red),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            containerColor = colorResource(id = R.color.ivory)
        )
    }
}