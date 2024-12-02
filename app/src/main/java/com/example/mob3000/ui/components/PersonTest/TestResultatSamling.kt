package com.example.mob3000.ui.components.PersonTest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.ui.components.LoadingIndicator

/**
 * Komponent som viser frem testresultat for en profil med navn til person/profil
 * Henter test ved hjelp av PersonlighetstestRep.fetchScore() og presenterer dataen
 * ved hjelp av TestKort, denne viser bare frem en del av testen
 *
 * @param testId Id til test
 * @param lang Valgt språk for test
 * @param backgroundColor Farge til bakgrunn
 *
 * Funksjoner
 * - Henter test ved hjelp av PersonlighetstestRep.fetchScore()
 * - Presenterer dataen ved hjelp av TestKort
 * - Viser loading hvis dataen ikke er på plass med en gang
 */
@Composable
fun TestResultatSamling(
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
                TestKort(
                    info = resultat,
                    backgroundColor = backgroundColor
                )
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }else{
        Text(text = stringResource(id = R.string.no_data))
    }
}