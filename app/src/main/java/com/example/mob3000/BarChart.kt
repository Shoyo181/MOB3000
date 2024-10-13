package com.example.mob3000


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.indices
import androidx.core.view.size
import kotlin.text.toFloat


@Composable
fun BarChart(titel: String, data: List<List<Int>>, labels: List<String>) {
    // Skriv ut tittelen
    Text(
        text = titel,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
            //.border(1.dp, Color.Gray) // Legg til en ramme
    ){
        // Litt mellomrom mellom tittelen og søylediagrammet
        Spacer(modifier = Modifier.height(16.dp))

        // Tegning søylediagrammet
        Canvas(modifier = Modifier
            .fillMaxWidth() // Fyll bredden på skjermen
            .height(400.dp) // Angi en fast høyde
        ) {
            val width = size.width
            val height = size.height
            val sectionWidth = width / data[0].size * 0.9f


            val paint = Paint().apply {
                color = Color.Blue
                style = PaintingStyle.Fill
            }
            val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)

            // Tegn x-aksen
            drawLine(
                color = Color.Black,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 2.dp.toPx()
            )

            // Tegn y-aksen
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 2.dp.toPx()
            )

            // Tegner etiketter på x-aksen - labels
            val textStyle = TextStyle(
                color = Color.Black,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic // Sett skråstil
            )

            for (i in data[0].indices) {
                val x = i * (sectionWidth * 1.1f) + (sectionWidth / 2)  // Plasser etiketten i midten av hver bar
                val y = 20.dp.toPx() // Plasser etiketten på toppen av Canvas i x-aksen
                drawContext.canvas.nativeCanvas.drawText(
                    labels[i],
                    x,
                    y,
                    android.graphics.Paint().apply {
                        color = textStyle.color.toArgb()
                        textSize = textStyle.fontSize.toPx()
                    }
                )
            }

            // Tegner etiketter på y-aksen - tall basert på data
            // Finn maksverdi i data og rund opp til nærmeste 10-er
            val maxValue = data.flatten().maxOrNull() ?: 0
            val yAxisMax = (maxValue + 9) / 10 * 10

            // Tegn y-akse linjer og tall
            for (i in 0..4) {
                val yValue = yAxisMax * i / 4 // Beregn y-verdi for linjen
                val yPosition =
                    size.height - (yValue.toFloat() / yAxisMax * size.height * 0.8f) // Beregn y-posisjon på Canvas

                // Tegn linje
                drawLine(
                    color = Color.LightGray, // Eller annen farge du ønsker
                    start = Offset(0f, yPosition),
                    end = Offset(size.width, yPosition),
                    strokeWidth = 1.dp.toPx()
                )

                // Tegn tall
                drawContext.canvas.nativeCanvas.drawText(
                    yValue.toString(),
                    10.dp.toPx(), // Juster x-posisjon etter behov
                    yPosition - 5.dp.toPx(), // Juster y-posisjon etter behov
                    android.graphics.Paint().apply {
                        color = Color.Black.toArgb()
                        textSize = 10.sp.toPx()
                    }
                )
            }

            // Beregner total bredde for alle søyler
            val totalBarWidth = width * 0.8f // Bruk 80% av bredden til søylene
            val numColumnsPerSection = data.size // Antall søyler per "seksjon"
            val barWidth = (sectionWidth / numColumnsPerSection) * 0.95f // Bredde for hver søyle

            // Tegning søylene i diagrammet
            for (i in data.indices) {
                val x = i * barWidth + (barWidth * 0.2f) + 40 // Beregn x-posisjon for søylen

                // Tegn søyler for hvert datasett
                var currentHeight = 0f

                for (j in data[i].indices) {
                    val barHeight = data[i][j].toFloat() / yAxisMax * size.height * 0.8f
                    val top = size.height - currentHeight - barHeight

                    val datasetX = (x + j * (barWidth * 2)) * 1.05f // Beregn x-posisjon for datasettet  -- ikke ferdig!!! TODO

                    drawRect(
                        color = colors[i],
                        topLeft = Offset(datasetX, top),
                        size = Size(barWidth, barHeight)
                    )
                    //currentHeight += barHeight
                }
            }
        }
    }
}