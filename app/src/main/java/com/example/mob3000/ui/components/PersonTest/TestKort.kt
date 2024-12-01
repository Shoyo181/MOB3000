package com.example.mob3000.ui.components.PersonTest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.R
import com.example.mob3000.data.models.ApiData.Result

/**
 * Komponenten viser frem et kort med informasjon om en del av testen
 * Bruker vil se tittel, liten beskrivelse, score vist i en pai-graf
 * Har en "vis mer" knapp som utvider kortet
 * Med utvidelse blir også beskrivelsen mer grundig og pai-grafen forsvinner
 * Under dukker alle "del-temaene" ved hjelp av DelInfoMedTekst.kt
 *
 * @param info Data klasse Result, en del/et tema av testen
 * @param backgroundColor Farge til kortet
 */
@Composable
fun TestKort(
    info: Result,
    backgroundColor: Color
){
    var utvidt by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)) {
            Row{
                Column(
                    modifier = Modifier.fillMaxWidth( if (utvidt) 1f else 0.7f )
                ){
                    // tittel
                    Text(text = info.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xff66433F))
                    Spacer(modifier = Modifier.padding(8.dp))
                    // hvis kortet er utvidet utvider vi beskrivelsen
                    if(utvidt){
                        TekstDeler(info.description)
                    }else{
                        Text(text = info.shortDescription, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color.dusk))
                    }
                }
                // hvis kortet ikke er utvidet vises pai-grafen
                if(!utvidt) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimertPaiGraf(info.score, 120, 100)
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = info.scoreText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xff817A81)
                        )
                    }
                }
            }
            // hvis kortet er utvidet vises alle "del-temaene"
            if (utvidt) {
                Spacer(modifier = Modifier.height(8.dp))
                // Beskrivelse
                DelInfoMedTekst(info.facets)
            }
            // bruker en textButton siden den allerede er gjennomsiktig
            TextButton(
                onClick = { utvidt = !utvidt },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorResource(R.color.dusk)
                )
            ) {
                Icon (
                    imageVector = if (!utvidt) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = if (!utvidt) "Utvid" else "Skjul"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if(!utvidt) stringResource(id = R.string.show_more) else stringResource(id = R.string.show_less),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}