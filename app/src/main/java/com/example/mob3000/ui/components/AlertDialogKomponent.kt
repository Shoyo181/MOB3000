package com.example.mob3000.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.mob3000.R

/**
 * Komponenten brukes til å lage en gjenbrukbare AlertDialog.
 * Den får også inn ButtonKomponenter i seg, slik at brukeren kan trykke på bekreft og avbryt.
 * Denne blir brukt på flere sider og tekst kan justeres, slik at den blir tilpasset de ulike verdiene.
 * Bruker AlertDialog fra Material Design.
 *
 * @param visDialog er en boolean som sier om dialogen skal vises eller ikke
 * @param onBekreft er en callback som blir kalt når brukeren trykker på bekreft knappen
 * @param onAvbryt er en callback som blir kalt når brukeren trykker på avbryt knappen
 * @param tittel er teksten som vises i tittelen til dialogen
 * @param tekst er teksten som vises i hovedteksten til dialogen
 * @param bekreftTekst er teksten som vises på bekreft knappen
 * @param avbrytTekst er teksten som vises på avbryt knappen
 */
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
            text = { Text(tekst) },
            containerColor = colorResource(id = R.color.ivory)
        )
    }
}

