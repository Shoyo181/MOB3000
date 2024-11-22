package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.R
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.models.ApiData.Facet
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.repository.PersonlighetstestRep

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
    }
}

@Composable
fun TestResultatKort(
    testId: String,
    lang: String,
    backgroundColor: Color
){
    var resultatListe by remember { mutableStateOf<List<Result>>(emptyList()) }
    // henter info om en profil test
    LaunchedEffect(testId) {
        // henter all data i "results" fra api -  det er denne informasjonen vi trenger
        val scores = PersonlighetstestRep(Nettverksmodul.apiService).fetchScore(testId, lang)
        resultatListe = scores
    }


    // lager kortene
    if(resultatListe.isNotEmpty()){
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
    }else{ // legg til loading
        Text(text = "Ingen data")
    }
}

fun sorterApiData(scores: List<Result>){

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
        Column(modifier = Modifier.padding(16.dp)) {
            // tittel
            Row{
                Text(text = info.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xff66433F))
                Spacer(modifier = Modifier.padding(8.dp))
                GraderGraf(
                    info = info,
                    maxValue = 120,
                    backgroundColor = backgroundColor
                )
            }

            if(utvidt) {
                Spacer(modifier = Modifier.height(8.dp))
                // Beskrivelse
                TekstDeler(info.description)
            }

            TextButton(
                onClick = { utvidt = !utvidt },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                // TODO: fiks så man kan se iconet
                if (!utvidt) {
                    Icons.Filled.KeyboardArrowDown
                } else {
                    Icons.Filled.KeyboardArrowUp
                }
            }
        }
    }
}
@Composable
fun DelInfo(
    info: Facet
){

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
