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


@Composable
fun Charts(profilData: List<ScoreList>){ // scoreData: List<ProfilData>, tittel: List<String>

    val totalScoreLables = remember { listOf("Nevrotisisme", "Ekstroversjon", "Åpenhet for erfaringer", "Medmenneskelighet", "Planmessighet") }
    val nevrotisismeLables = remember { listOf("Angst", "Sinne", "Depresjon", "Selvbevissthet", "Impulsivitet", "Sårbarhet") }
    val EkstroversjonLables = remember { listOf("Vennlighet", "Sosiabilitet", "Selvmarkering", "Aktivitet", "Spenningssøking", "Positive følelser") }
    val åpenhetForErgaringerLabels = remember { listOf("Fantasi", "Estetikk", "Følelser", "Eventyrlyst", "Intellekt", "Liberale verdier") }
    val medmenneskelighetLabels = remember { listOf("Tillit", "Moral", "Altruisme", "Føyelighet", "Beskjedenhet", "Følsomhet") }
    val planmessighetLabels = remember { listOf("Kompetanse", "Orden", "Pliktoppfyllenhet", "Prestasjonsstreben", "Selvdisiplin", "Betenksomhet") }

    val tittel = remember { listOf(totalScoreLables, nevrotisismeLables, EkstroversjonLables, åpenhetForErgaringerLabels, medmenneskelighetLabels, planmessighetLabels) }

    val farger = listOf(
        SolidColor(colorResource(id = R.color.blue)),
        SolidColor(colorResource(id = R.color.orange)),
        SolidColor(colorResource(id = R.color.green)),
        SolidColor(colorResource(id = R.color.pink)),
        SolidColor(colorResource(id = R.color.purple)),
        SolidColor(colorResource(id = R.color.yellow))
    )

    var digramIndex by remember { mutableStateOf(0) }
    val tempFarge = SolidColor(colorResource(id = R.color.dusk2))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // bruker Column for å organisere innhold

        Text(profilData[0].results[digramIndex].score.title)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.offset( y = (-16).dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ){
            for ( i in tittel.indices){
                Button(
                    onClick = {digramIndex = i},
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(digramIndex == i)
                            Color(0x86FFFFFF) else Color(0x7E000000)
                    )
                ) {

                }
            }

        }

        when (digramIndex){
            0 -> OneChart(barsBuilder(profilData, 0, farger), profilData.size)
            1 -> OneChart(barsBuilder(profilData, 1, farger), profilData.size)
            2 -> OneChart(barsBuilder(profilData, 2, farger), profilData.size)
            3 -> OneChart(barsBuilder(profilData, 3, farger), profilData.size)
            4 -> OneChart(barsBuilder(profilData, 4, farger), profilData.size)
            5 -> OneChart(barsBuilder(profilData, 5, farger), profilData.size)
        }
        Spacer(modifier = Modifier.padding(50.dp))
        Spacer(modifier = Modifier.padding(40.dp))
    }
}
