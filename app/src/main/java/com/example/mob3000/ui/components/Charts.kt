package com.example.mob3000.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.models.ScoreList
import com.example.mob3000.data.repository.ScoreUtils.barsBuilder

/**
 * Komponent som viser frem de flersøyldede diagrammene for de valgte profilene
 * viser frem en om gangen slik at vi får animasjon hver gang vi går igjennom diagrammene
 * Bruker komponenten OneChart og funksjone barsBuilder
 */
@Composable
fun Charts(profilData: List<ScoreList>){ // scoreData: List<ProfilData>, tittel: List<String>
    // hjelpe variabel for å fordele farger til de forkjellige profilene valgt
    val farger = listOf(
        SolidColor(colorResource(id = R.color.blue)),
        SolidColor(colorResource(id = R.color.orange)),
        SolidColor(colorResource(id = R.color.green)),
        SolidColor(colorResource(id = R.color.pink)),
        SolidColor(colorResource(id = R.color.purple)),
        SolidColor(colorResource(id = R.color.yellow))
    )

    // diagramIndex husker på hvilket diagram som er valgt
    var digramIndex by remember { mutableStateOf(0) }

    // bruker Column for å organisere innhold
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // Tittel for diagrammet
        Text(profilData[0].results[digramIndex].score.title)
        // Knapper for å velge de forskjellige diagrammene
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.offset( y = (-16).dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ){
            // Lager knappene
            for ( i in profilData[0].results.indices){
                Button(
                    onClick = {digramIndex = i},
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(digramIndex == i)
                            Color(0x86FFFFFF) else Color(0x7E000000)
                    )
                ) {/*Trengs ingen kode her, men må ha krøllparantes*/ }
            }

        }
        // lager og viser frem det valgte diagrammet,
        // bruker key siden vi ikke vil at Android Studio skal gjenbruke komponenten
        // våres, det vil medføre at det står feil info på siden av diagrammet
        when (digramIndex){
            in 0..5 -> {
                key(digramIndex){
                    OneChart(barsBuilder(profilData, digramIndex, farger), profilData.size)
                }
            }
        }
    }
}
