package com.example.mob3000.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import kotlinx.coroutines.delay

@Composable
fun AnimertPaiGraf(
    value: Int,
    maxValue: Int,
    pieSize: Int
) {
    // regner ut hvor my "prosent" som skal vises
    val sumResultat = (value.toFloat() / maxValue.toFloat())

    var progress by remember {mutableStateOf(0f)}

    LaunchedEffect(sumResultat) {
        //Tidsbruk utregning etc.
        val animasjonDurasjon = 2000
        val frameDurasjon = 16L

        var tid = 0L

        while(tid < animasjonDurasjon) {
            val animatedProgress = tid / animasjonDurasjon.toFloat() * sumResultat
            progress = animatedProgress
            tid += frameDurasjon
            delay(frameDurasjon)
        }
        progress = sumResultat
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(pieSize.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress }, // Animasjonen styrer progresjonen
            modifier = Modifier.size(pieSize.dp),
            color = colorResource(id = R.color.maghogny),
            trackColor = colorResource(id = R.color.sand),
            strokeWidth = 8.dp
        )
        // Tekst med tall resultat i midten
        Text(
            text = "${(progress.toFloat() * maxValue).toInt()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
