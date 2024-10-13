package com.example.mob3000


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.ui.theme.Blue
import com.example.mob3000.ui.theme.Red

@Composable
fun CircularProgressBar(score: Float, label: String) {
    val strokeWidth = 10.dp

    Box(modifier = Modifier.size(100.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Lager sirkelen som er i bakgrunnen av sirkeldiagrammet
            drawCircle(
                color = Blue,
                style = Stroke(width = strokeWidth.toPx())
            )

            // Lager arc i en annen farge for å få frem prosenttall i en annen farge enn bakgrunnen.
            drawArc(
                color = Red,
                startAngle = -90f,
                sweepAngle = (360 * score / 100),
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        // Viser hva det skal stå i tall under sirkeldiagrammet.
        Text(
            text = "${score.toInt()}%",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }

    // Label for tekst under sirkel
    Text(
        text = label,
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}