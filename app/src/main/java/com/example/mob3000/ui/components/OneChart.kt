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
 *
 */
@Composable
fun OneChart(barsData: List<Bars>, ant: Int){
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