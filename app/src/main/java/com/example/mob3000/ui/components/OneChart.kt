package com.example.mob3000.ui.components

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars

/**
 * OneChart er en komponent som tegner et flersøylediagram
 * Bruker biblioteket ir.ehsannarmani.compose_charts for å tegne selve diagrammet
 * I ColumnChart trenger man å oppgi følgende: tyggekse på søyle, avstand mellom de forskjellige temaene og data om søyler
 * Data om søylene er innparameter for komponenten, som er en liste over Bars-objekter. (også fra bibioteket - compose_charts)
 *
 * @param barsData En liste over Bars-objekter som inneholder informasjon om hver enkelt søyle
 * @param ant Antall profiler som skal vises i diagrammet - brukes for å regne ut søyletykkelsen
 */
@Composable
fun OneChart(barsData: List<Bars>, ant: Int){
    // regner ut nyttig informasjon for diagrammet basert på skjerm størrelse
    val konfig = LocalConfiguration.current
    val skjermHøyde = konfig.screenHeightDp.dp
    val søyleTykkelse = 40/ant

    // tenger selve diagrammet, med utregnet informasjon og innparametere
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