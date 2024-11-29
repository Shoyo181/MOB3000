package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.api.Nettverksmodul.apiService
import com.example.mob3000.data.firebase.FirebaseService
import com.example.mob3000.data.models.Person
import com.example.mob3000.data.models.ScoreList
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.data.repository.ScoreUtils
import com.example.mob3000.ui.components.Charts
import com.example.mob3000.ui.components.ProfilVelgeKort


// side for å vise frem sammenligning
@Composable
fun Sammenlign(modifier: Modifier){
    // lage en liste over alle personer i databasen
    // bruker kan velge hvilke personer som skal sammenlignes
    // de som er valgt henter vi data fra ved hjelp av api kall
    // forer denne dataen inn til charts, for en visuell visning
    // det er en "switch" som går mellom sammenligneing og velging av profiler


    var valgIndex by remember { mutableStateOf(0) }
    val valgTilSammenligning = listOf(
            stringResource(id = R.string.compare_segmentbutton_choose),
            stringResource(id = R.string.compare_segmentbutton_compare)
            )

    // liste over personer fra database
    var personListe by remember {mutableStateOf<List<Person>>(emptyList())}
    var personTilSammenligning by remember {mutableStateOf<List<Person>>(emptyList())}
    var personMedScore by remember {mutableStateOf<List<ScoreList>>(emptyList())}
    var visAlert by remember {mutableStateOf(false)}

    val lang = stringResource(id = R.string.language_api)

    // henter listen fra databasen
    LaunchedEffect(Unit) {
        FirebaseService.hentPersoner(
            onSuccess = {fetchedePersoner -> personListe = fetchedePersoner },
            onFailure = {exception -> Log.e("Firestore", "Feil: $exception") }
        )
    }

    //
    Column(
       modifier = Modifier
           .fillMaxWidth()
           //.background(Color.White)
           .padding(16.dp)
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            valgTilSammenligning.forEachIndexed { index, value ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = valgTilSammenligning.size),
                    onClick = { valgIndex = index },
                    selected = index == valgIndex,
                    colors = SegmentedButtonDefaults.colors(colorResource(R.color.dusk))
                ){
                    Text(text = value)
                }
            }
        }
        if(valgIndex == 0){
            if(visAlert){
                Snackbar() {
                    Text(stringResource(id = R.string.compare_limit))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(colorResource(id = R.color.sand), (colorResource(id = R.color.dusk)))
                        )
                    )
            ){
                items(personListe) { person ->
                    ProfilVelgeKort(
                        person = person,
                        valgtePersoner = personTilSammenligning,
                        onClick = {
                            // legger person inn til sammenligning hvis den ikke allerede er med
                            if(personTilSammenligning.contains(person)){
                                personTilSammenligning = personTilSammenligning.filter { it != person }
                                visAlert =false
                            }else{
                                if(personTilSammenligning.size >= 6) {
                                    // liste som kan sammenlignes begrenses til 6, viser alert hvis det er mer
                                    visAlert = true
                                }else {
                                    personTilSammenligning = personTilSammenligning + person
                                    Log.d("sjekk alert", "personer til sammenligning.size: ${personTilSammenligning.size}")
                                }
                            }
                        }
                    )
                }
            }

        }else{
            Log.d("Sammenligning", "Personer med i sammenligning: ${personTilSammenligning.map { person -> person.name } }")
            // henter og sorterer data fra api

            LaunchedEffect(personTilSammenligning) {
                // TODO: Legg til loading
                personMedScore = personTilSammenligning.map { person ->
                    val scores = PersonlighetstestRep(apiService).fetchScore(person.testid, lang)
                    ScoreUtils.lagChartsData(scores, person)
                }
                Log.d("Sammenligning", "TEEEEEEST")
            }


            Log.d("Sammenligning", "Personer med score: ${personMedScore.map { person -> person.name } }")
            // Hvis det ikke er noen i lista, så viser vi frem en melding istedenfor diagrammet
            if(personMedScore.isNotEmpty()){
                Charts(profilData = personMedScore)
            }else{ // legg til loading
                Text("Ingen data")
            }

        }
    }

}