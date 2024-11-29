package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.R
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.models.ApiData.Facet
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.ui.components.AnimertPaiGraf
import com.example.mob3000.ui.components.LoadingIndicator

@Composable
fun PersonTest(
    testId: String?,
    name: String?
) {
    val lang = stringResource(id = R.string.language_api)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TestResultatKort(
            testId = testId ?: "",
            lang = lang,
            backgroundColor = colorResource(id = R.color.ivory)
        )
        Spacer(modifier = Modifier.padding(bottom = 60.dp))
    }
}

@Composable
fun TestResultatKort(
    testId: String,
    lang: String,
    backgroundColor: Color
) {
    var resultatListe by remember { mutableStateOf<List<Result>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    // henter info om en profil test
    LaunchedEffect(testId) {
        loading = true
        // henter all data i "results" fra api -  det er denne informasjonen vi trenger
        val scores = PersonlighetstestRep(Nettverksmodul.apiService).fetchScore(testId, lang)
        resultatListe = scores
        loading = false
    }

    if(loading){
        LoadingIndicator()
    }else if(resultatListe.isNotEmpty()){
        // lager kortene
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for(resultat in resultatListe){
                InfoBlokk(
                    info = resultat,
                    backgroundColor = backgroundColor
                )
                Log.d("PersonTest", "Laget blokk - ${resultat.title}")
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }else{
        Text(text = stringResource(id = R.string.no_data))
    }
}

@Composable
fun InfoBlokk(
    info: Result,
    backgroundColor: Color
){
    var utvidt by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)) {
            // tittel
            Row{
                Column(
                    modifier = Modifier.fillMaxWidth( if (utvidt) 1f else 0.7f )
                ){
                    Text(text = info.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xff66433F))
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = info.shortDescription, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color.dusk))
                }
                if(!utvidt) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)

                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimertPaiGraf(info.score, 120, 100)
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = info.scoreText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xff817A81)
                        )
                    }


                }

            }

            if (utvidt) {
                Spacer(modifier = Modifier.height(8.dp))
                // Beskrivelse
                DelInfoMedTekst(info.facets)
                //TekstDeler(info.description)
            }

            TextButton(
                onClick = { utvidt = !utvidt },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorResource(R.color.dusk)
                )
            ) {
                Icon (
                imageVector = if (!utvidt) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = if (!utvidt) "Utvid" else "Skjul"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if(!utvidt) stringResource(id = R.string.show_more) else stringResource(id = R.string.show_less),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
@Composable
fun DelInfo(
    info: List<Facet>
){
    //Log.d("PersonTest", "DelInfo er opprettet -------------------------")
    //Log.d("PersonTest", "info.size : ${info.size}")
    for (i in 1..info.size step 3){
        Row{
            for(j in 0..2){
                val index = i+j-1
                //Log.d("PersonTest", "Laget del - ${info[index].title}")
                //Log.d("PersonTest", "i: $i, j: $j - index: $index")
                Column{
                    Text(modifier = Modifier.align(Alignment.CenterHorizontally) ,text = info[index].title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xff817A81))
                    Spacer(modifier = Modifier.width(8.dp))
                    AnimertPaiGraf(info[index].score, 20, 100)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(modifier = Modifier.align(Alignment.CenterHorizontally) ,text = info[index].scoreText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xff817A81))
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
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

@Composable
fun GraderGraf(
    info: Result,
    maxValue: Int,
    backgroundColor: Color
){



}

fun apiTekstRydder(tekst: String): List<String>{
    //viser seg at formatet fra Api kommer med litt forskjellige måter å dele opp tekst
    // metoden formaterer tekst og gjør klar til printing
    val nyTekst = tekst.replace("<br/>", "<br />")
    val linjer = nyTekst.split("<br /><br />")
    val nyeLinjer = mutableListOf<String>()
    // tar bort resterende \n
    for(linje in linjer){
        nyeLinjer.add(linje.replace("\n", " "))
    }
    return nyeLinjer
}

@Composable
fun TekstDeler(tekst: String){
    // lager Text filer, når dataen fra API sier at det er linje skifte legger vi inn en Spacer
    val deltTeskt = apiTekstRydder(tekst)
    Column{
        deltTeskt.forEach { del ->
            if (del.isNotBlank()) {
                Text(text = del, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xff817A81))
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
