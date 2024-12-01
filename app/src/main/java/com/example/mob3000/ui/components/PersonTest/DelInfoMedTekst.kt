package com.example.mob3000.ui.components.PersonTest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.R
import com.example.mob3000.data.models.ApiData.Facet


/**
 * Komponenten Viser frem info om alle "del-teamene"(angst, sinne, osv) fra testen med en pai-graf
 * som indikerer hvor høy scoren var for det gitt temaet
 * Bruker TekstDeler og AnimertPaiGraf
 *
 * @param info Liste med all info om et "del-teamene" fra testen
 */
@Composable
fun DelInfoMedTekst(
    info: List<Facet>
) {
    for (i in info) {
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = colorResource(id = R.color.maghogny).copy(alpha = 0.9f),
                    spotColor = colorResource(id = R.color.maghogny).copy(alpha = 0.9f)
                )
                .background(
                    color = colorResource(id = R.color.ivory2),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = i.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.maghogny),
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Row{
                Column(
                    modifier = Modifier.fillMaxWidth(0.3f)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        AnimertPaiGraf(i.score, 20, 100)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = i.scoreText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.dusk)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TekstDeler(i.text)
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
    }
}