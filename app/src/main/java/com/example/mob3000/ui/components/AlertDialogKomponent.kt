package com.example.mob3000.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
fun AlertDialogKomponent(
    visDialog: Boolean,
    onBekreft: () -> Unit,
    onAvbryt: () -> Unit,
    tittel: String,
    tekst: String,
    bekreftTekst: String,
    avbrytTekst: String
){
    if(visDialog){
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { onAvbryt() },
            confirmButton = {
                ButtonKomponent(
                    text = bekreftTekst,
                    onClick = {
                        onBekreft()
                        onAvbryt()
                    },
                )
            },
            dismissButton = {
                ButtonKomponent(
                    text = avbrytTekst,
                    onClick = { onAvbryt() },
                )
            },
            title =  { Text(tittel) },
            text = { Text(tekst) }
        )
    }
}

