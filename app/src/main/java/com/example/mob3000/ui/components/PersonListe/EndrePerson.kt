package com.example.mob3000.ui.components.PersonListe

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
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

@Composable
fun EndrePerson (
    person: Person,
    onDismiss: () -> Unit,
    onLagre: (Person) -> Unit
) {
    var oppdatertNavn by remember { mutableStateOf(person.name) }
    var oppdatertAlder by remember { mutableStateOf(person.age) }
    var oppdatertEpost by remember { mutableStateOf(person.email) }
    var oppdatertTestId by remember { mutableStateOf(person.testid) }
    var oppdatertStilling by remember {mutableStateOf(person.stilling)}
    var regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    var feilInputEpost by remember { mutableStateOf(false) }
    var errorMeldingEpost by remember { mutableStateOf("") }

    val ugyldigInputEpost = stringResource(id = R.string.wrong)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ButtonKomponent(
                text = stringResource(id = R.string.alert_edit_save),
                onClick = {
                    if (!oppdatertEpost.matches(regex)) {
                        feilInputEpost = true
                        errorMeldingEpost = ugyldigInputEpost
                    } else {
                        feilInputEpost = false
                        errorMeldingEpost = ""
                        val oppdatertPerson = person.copy(
                            name = oppdatertNavn,
                            age = oppdatertAlder,
                            email = oppdatertEpost,
                            stilling = oppdatertStilling,
                            testid = oppdatertTestId
                        )
                        onLagre(oppdatertPerson)
                        onDismiss()
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
        title = {Text(stringResource(id = R.string.alert_edit_title))},
        text = {
            Column {
                OutlinedTextFieldKomponent(
                    value = oppdatertNavn,
                    onValueChange = {oppdatertNavn = it},
                    label = stringResource(id = R.string.name)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertAlder,
                    onValueChange = { oppdatertAlder = it },
                    label = stringResource(id = R.string.age)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertEpost,
                    onValueChange = { oppdatertEpost = it },
                    label = stringResource(id = R.string.email)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertStilling,
                    onValueChange = { oppdatertStilling = it },
                    label = stringResource(id = R.string.stilling)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertTestId,
                    onValueChange = { oppdatertTestId = it },
                    label = stringResource(id = R.string.testid)
                )
                if(feilInputEpost){
                    Text(
                        text = errorMeldingEpost,
                        color = colorResource(id = R.color.red)
                    )
                }
            }
        },
        containerColor = colorResource(id = R.color.ivory)
    )
}


