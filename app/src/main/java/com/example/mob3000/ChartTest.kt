package com.example.mob3000

import androidx.compose.runtime.Composable
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun ResultChart(scores: List<Result>) {
    val entries = scores.mapIndexed { index, result ->
        BarEntry(index.toFloat(), result.score.toFloat())
    }

    val barDataSet = BarDataSet(entries, "Scores").apply {
        colors = listOf(Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED, Color.CYAN)
    }

    val barData = BarData(barDataSet)

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                data = barData
                description.text = "Personlighetstest resultater"
                xAxis.valueFormatter = IndexAxisValueFormatter(scores.map { it.domain })
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}