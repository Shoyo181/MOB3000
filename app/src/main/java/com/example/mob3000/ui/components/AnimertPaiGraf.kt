package com.example.mob3000.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import kotlinx.coroutines.delay

/**
 * Komponent som viser frem en sirkel som indikerer hvor mye prostent en variabel har - en pai graf
 * Grafen er animert, og oppdateres jevnt fra 0 til endepunkt
 *
 * @param value er verdien som skal vises i grafen
 * @param maxValue er maks verdien - 100%
 * @param pieSize er størrelsen på grafen
 *
 * Funksjonen/koponenten inkluderer
 * - en beregning av prosentandel, bruker value og maxValue
 * - en animasjon for å oppdatere prosentandelen over tid, styrt av en korutine i launched effect
 * - en visuelfremvisning av prosentandelen med en sirkel og tekst
 */
@Composable
fun AnimertPaiGraf(
    value: Int,
    maxValue: Int,
    pieSize: Int
) {
    // regner ut hvor my prosent, brukes til sirkelen
    val sumResultat = (value.toFloat() / maxValue.toFloat())
    // variabel for å holde styr på animasjonsfremdriften
    var progress by remember {mutableStateOf(0f)}

    // bruker launched effect for å regne ut animasjonen, animasjonen setter i gang mår sumResultat endres
    LaunchedEffect(sumResultat) {
        //Tidsbruk utregning etc.
        val animasjonDurasjon = 2000
        val frameDurasjon = 16L

        var tid = 0L
        // oppdaterer animasjon
        while(tid < animasjonDurasjon) {
            val animatedProgress = tid / animasjonDurasjon.toFloat() * sumResultat
            progress = animatedProgress
            tid += frameDurasjon
            delay(frameDurasjon) // bruker delay for å etterligne animasjonsflyt
        }
        // sikrer oss at siste verdi er riktig
        progress = sumResultat
    }
    // viser "grafen" i en Box for å få innholdet i midten eller sentrert
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(pieSize.dp)
    ) {
        // Tegner den animerte pai-grafen
        CircularProgressIndicator(
            progress = { progress }, // Animasjonen styrer progresjonen
            modifier = Modifier.size(pieSize.dp),
            color = colorResource(id = R.color.maghogny),
            trackColor = colorResource(id = R.color.sand),
            strokeWidth = 8.dp
        )
        // Tekst med tall resultat i midten
        Text(
            text = "${(progress * maxValue).toInt()}", // Dynamisk oppdatert
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
