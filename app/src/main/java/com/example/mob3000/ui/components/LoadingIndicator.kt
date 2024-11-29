package com.example.mob3000.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R

/**
 * Komponent som viser frem en sirkel som spinner rundt, indikerer at noe er lastet inn
 * Laget en komponent slik at den er lik hvor enn den skulle ha blitt brukt
 */
@Composable
fun LoadingIndicator (){
    // rammer inn sirkelen i en boks, slik at vi kan plasere innholdet i minden av skjermen
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ){
        // legger på litt padding på top så denne komponenten ikke er "klistret" til noe annet
        CircularProgressIndicator(
            modifier = Modifier
                .width(52.dp)
                .padding(top = 32.dp),
            color = colorResource(id = R.color.maghogny),
            trackColor = colorResource(id = R.color.sand),
        )
    }

}