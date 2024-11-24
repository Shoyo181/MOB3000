package com.example.mob3000.ui.components

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
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
            0 -> oneChart(barsBuilder(profilData, 0, farger), profilData.size)
            1 -> oneChart(barsBuilder(profilData, 1, farger), profilData.size)
            2 -> oneChart(barsBuilder(profilData, 2, farger), profilData.size)
            3 -> oneChart(barsBuilder(profilData, 3, farger), profilData.size)
            4 -> oneChart(barsBuilder(profilData, 4, farger), profilData.size)
            5 -> oneChart(barsBuilder(profilData, 5, farger), profilData.size)
        }
        Spacer(modifier = Modifier.padding(50.dp))

       /* Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset( y = (-16).dp)
                .align(alignment = Alignment.CenterHorizontally)
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

        }*/
        Spacer(modifier = Modifier.padding(40.dp))
    }
}

fun barsBuilder (profilData: List<ScoreList>, index: Int, color: List<SolidColor>): List<Bars> {
    // antall søyler for dette diagrammet
    val numBars = profilData[0].results[index].facets.size

    //Log.d("barsBuilder-info-space", "-----------------------------")
    //Log.d("barsBuilder-info", "tittel index: " + tittel[index].size)
    //Log.d("barsBuilder-info", "numBars     : " + numBars)

    // lager ferdig data for hva som skal vises i diagrammet
    val barsData = (0 until numBars).map { barIndex ->
        //Log.d("barsBuilder-info-space", "     ")
        //Log.d("barsBuilder-info", "- Bar index: " + barIndex)
        Bars(
            label = profilData[0].results[index].facets[barIndex].title,
            values = profilData.mapIndexed { i, profil ->
                //Log.d("barsBuilder-info", "-- barInx: " + barIndex + ", index: " + index + ", profil: " + profil.score[index][barIndex])
                Bars.Data(
                    label = profil.name,
                    value = profil.results[index].facets[barIndex].score.toDouble(),
                    color = color[i],
                )
            }
        )
    }
    return barsData
}

@Composable
fun oneChart(barsData: List<Bars>, ant: Int){
    // bruker biblioteket for å lage diagrammet
    val konfig = LocalConfiguration.current
    val skjermHøyde = konfig.screenHeightDp.dp
    val søyleTykkelse = 40/ant
    Log.d("barsBuilder-info-space", "-----------------------------")
    Log.d("barsBuilder-info", "søyleTykkelse: " + søyleTykkelse)
    Log.d("barsBuilder-info", "barData.size: " + barsData.size)

    ColumnChart(
        Modifier
            .fillMaxSize()
            .height(skjermHøyde * 0.6f) //TODO: dynamisk for forskjellige mobiler,
            .padding(8.dp),
        data = barsData,
        barProperties = BarProperties(
            spacing = 3.dp,
            thickness = søyleTykkelse.dp
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
}