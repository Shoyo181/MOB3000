package com.example.mob3000.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.mob3000.data.models.ProfilData
import com.example.mob3000.R
import com.example.mob3000.data.models.ScoreList
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars


@Composable
fun Chart(profilData: List<ScoreList>){ // scoreData: List<ProfilData>, tittel: List<String>

    val totalScoreLables = remember { listOf("Nevrotisisme", "Ekstroversjon", "Åpenhet for erfaringer", "Medmenneskelighet", "Planmessighet") }
    val nevrotisismeLables = remember { listOf("Angst", "Sinne", "Depresjon", "Selvbevissthet", "Impulsivitet", "Sårbarhet") }
    val EkstroversjonLables = remember { listOf("Vennlighet", "Sosiabilitet", "Selvmarkering", "Aktivitet", "Spenningssøking", "Positive følelser") }
    val åpenhetForErgaringerLabels = remember { listOf("Fantasi", "Estetikk", "Følelser", "Eventyrlyst", "Intellekt", "Liberale verdier") }
    val medmenneskelighetLabels = remember { listOf("Tillit", "Moral", "Altruisme", "Føyelighet", "Beskjedenhet", "Følsomhet") }
    val planmessighetLabels = remember { listOf("Kompetanse", "Orden", "Pliktoppfyllenhet", "Prestasjonsstreben", "Selvdisiplin", "Betenksomhet") }

    val tittel = remember { listOf(totalScoreLables, nevrotisismeLables, EkstroversjonLables, åpenhetForErgaringerLabels, medmenneskelighetLabels, planmessighetLabels) }

    var digramIndex by remember { mutableStateOf(0) }
    val tempFarge = SolidColor(colorResource(id = R.color.maghogny))
    Column{
        // bruker Column for å organisere innhold


        Text(
            if(digramIndex == 0)
                "Big 5 Score"
            else
                totalScoreLables[digramIndex-1]
        )

        when (digramIndex){
            0 -> oneChart(barsBuilder(profilData, tittel, 0, tempFarge))
            1 -> oneChart(barsBuilder(profilData, tittel, 1, tempFarge))
            2 -> oneChart(barsBuilder(profilData, tittel, 2, tempFarge))
            3 -> oneChart(barsBuilder(profilData, tittel, 3, tempFarge))
            4 -> oneChart(barsBuilder(profilData, tittel, 4, tempFarge))
            5 -> oneChart(barsBuilder(profilData, tittel, 5, tempFarge))
        }
        Spacer(modifier = Modifier.padding(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .offset( y = (-16).dp)
                .align(Alignment.CenterHorizontally)
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
                ){

                }
            }

        }
    }


}

fun barsBuilder (profilData: List<ScoreList>, tittel: List<List<String>>, index: Int, color: SolidColor): List<Bars> {
    // antall søyler for dette diagrammet
    val numBars = tittel[index].size

    //Log.d("barsBuilder-info-space", "-----------------------------")
    //Log.d("barsBuilder-info", "tittel index: " + tittel[index].size)
    //Log.d("barsBuilder-info", "numBars     : " + numBars)

    // lager ferdig data for hva som skal vises i diagrammet
    val barsData = (0 until numBars).map { barIndex ->
        //Log.d("barsBuilder-info-space", "     ")
        //Log.d("barsBuilder-info", "- Bar index: " + barIndex)
        Bars(
            label = tittel[index][barIndex],
            values = profilData.map { profil ->
                //Log.d("barsBuilder-info", "-- barInx: " + barIndex + ", index: " + index + ", profil: " + profil.score[index][barIndex])
                Bars.Data(
                    label = profil.name,
                    value = profil.results[index].facets[barIndex].score.toDouble(),
                    color = color
                )
            }
        )
    }
    return barsData
}

@Composable
fun oneChart(barsData: List<Bars>){
    // bruker biblioteket for å lage diagrammet
    ColumnChart(
        Modifier
            .fillMaxWidth()
            .height(580.dp) //TODO: dynamisk for forskjellige mobiler,
            .padding(16.dp, top = 32.dp),
        data = barsData,
        barProperties = BarProperties(
            spacing = 3.dp,
            thickness = 20.dp
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
}