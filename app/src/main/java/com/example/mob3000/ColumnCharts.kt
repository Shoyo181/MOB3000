package com.example.mob3000

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.mob3000.ui.theme.BlueBrush
import com.example.mob3000.ui.theme.DarkblueBrush
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars


@Composable
fun Chart(profilData: List<ProfilData>){ // scoreData: List<ProfilData>, tittel: List<String>

    val totalScoreLables = remember { listOf("Nevrotisisme", "Ekstroversjon", "ÅpenhetForErfaringer", "Medmenneskelighet", "Planmessighet") }
    val nevrotisismeLables = remember { listOf("Angst", "Sinne", "Deprisjon", "Selvbevissthet", "Impulsivitet", "Sårbarhet") }
    val EkstroversjonLables = remember { listOf("Vennlighet", "Sosiabilitet", "Selvmarkering", "Aktivitet", "Spennings-søking", "Positive følelser") }
    val åpenhetForErgaringerLabels = remember { listOf("Fantasi", "Estetikk", "Følelser", "Eventyrlyst", "Intellekt", "Liberale verdier") }
    val medmenneskelighetLabels = remember { listOf("Tillit", "Moral", "Altruisme", "Føyelighet", "Beskjedenhet", "Følsomhet") }
    val planmessighetLabels = remember { listOf("Kompetanse", "Orden", "Pliktoppfyllenhet", "Prestasjonsstreben", "Selvdisiplin", "Betenksomhet") }

    val tittel = remember { listOf(totalScoreLables, nevrotisismeLables, EkstroversjonLables, åpenhetForErgaringerLabels, medmenneskelighetLabels, planmessighetLabels) }

    var digramIndex by remember { mutableStateOf(0) }

    Column{
        // bruker Column for å organisere innhold

        Text(
            if(digramIndex == 0)
                "Big 5 Score"
            else
                totalScoreLables[digramIndex-1]
        )

        when (digramIndex){
            0 -> oneChart(barsBuilder(profilData, tittel, 0))
            1 -> oneChart(barsBuilder(profilData, tittel, 1))
            2 -> oneChart(barsBuilder(profilData, tittel, 2))
            3 -> oneChart(barsBuilder(profilData, tittel, 3))
            4 -> oneChart(barsBuilder(profilData, tittel, 4))
            5 -> oneChart(barsBuilder(profilData, tittel, 5))
        }


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
                        .size(16.dp)
                ){

                }
            }

        }
    }


}

fun barsBuilder (profilData: List<ProfilData>, tittel: List<List<String>>, index: Int ): List<Bars> {
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
                    label = profil.navn,
                    value = profil.score[index][barIndex].toDouble(),
                    color = DarkblueBrush
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
            .height(400.dp) //TODO: dynamisk for forskjellige mobiler,
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