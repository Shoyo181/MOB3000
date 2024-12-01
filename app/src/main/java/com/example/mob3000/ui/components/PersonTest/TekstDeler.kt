package com.example.mob3000.ui.components.PersonTest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.data.repository.ScoreUtils

/**
 * Komponenten bruker funksjonen apiTekstRydder() fra ScoreUtils.kt
 * Får en liste med String, hver string blir printet med et linjeskift mellom hverandre
 *
 * @param tekst String med tekst fra API
 */
@Composable
fun TekstDeler(tekst: String){
    // lager Text filer, når dataen fra API sier at det er linje skifte legger vi inn en Spacer
    val deltTeskt = ScoreUtils.apiTekstRydder(tekst)
    Column{
        deltTeskt.forEach { del ->
            if (del.isNotBlank()) {
                Text(text = del, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xff817A81))
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}