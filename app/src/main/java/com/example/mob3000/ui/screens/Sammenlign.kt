package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.mob3000.data.firebase.FirestoreService
import com.example.mob3000.data.models.Person
import com.example.mob3000.data.models.ScoreList
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.data.repository.ScoreUtils
import com.example.mob3000.ui.components.sammenlign.Charts
import com.example.mob3000.ui.components.LoadingIndicator
import com.example.mob3000.ui.components.sammenlign.ProfilVelgeKort


/**
 * Screen hvor bruker kan velge hvilke profiler som skal sammenlignes
 * Det er en toggle knapp på toppen som bytter mellom velging av profiler og visning av sammenligning
 * for de valgte profilene.
 * Vi lager in liste over alle profiler hvor bruker kan velge hvem som skal videre til sammenligning
 * Vi får hjelp av komponenten Charts.kt for å vise frem flersøylediagrammet med sammenligning
 */
@Composable
fun Sammenlign(modifier: Modifier){
    //variabler for segmentButton
    var valgIndex by remember { mutableStateOf(0) }
    val valgTilSammenligning = listOf(
            stringResource(id = R.string.compare_segmentbutton_choose),
            stringResource(id = R.string.compare_segmentbutton_compare)
            )

    // Variabler for alle profiler samt hvilke profiler som er valg. Ekstra hjelpe variabel for
    // som bruker har valgt formange profiler
    var personListe by remember {mutableStateOf<List<Person>>(emptyList())}
    var personTilSammenligning by remember {mutableStateOf<List<Person>>(emptyList())}
    var personMedScore by remember {mutableStateOf<List<ScoreList>>(emptyList())}
    var visAlert by remember {mutableStateOf(false)}
    var loading by remember {mutableStateOf(false)}
    //variabel for å huske og hente hvilket språk sammenligningen skal være i
    val lang = stringResource(id = R.string.language_api)

    // henter profil listen fra databasen
    LaunchedEffect(Unit) {
        FirestoreService.hentPersoner(
            onSuccess = {fetchedePersoner -> personListe = fetchedePersoner },
            onFailure = {exception -> Log.e("Firestore", "Feil: $exception") }
        )
    }

    // Legger alle komponenter i en kolonne, slik at alt kommer pyntelig under hverandre
    Column(
       modifier = Modifier
           .fillMaxWidth()
           //.background(Color.White)
           .padding(16.dp)
    ) {
        // toggel eller switch knapp som bytter mellom sammenligning av profiler og visning av profiler
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
        // hvis bruker har valgt å velge profiler viser vi en liste ovar alle profiler
        if(valgIndex == 0){
            // hvis bruker har valgt for mange profiler viser vi en "alert" som egentlig bare er en snackbar
            if(visAlert){
                Snackbar() {
                    Text(stringResource(id = R.string.compare_limit))
                }
            }
            // legger alle profiler i en liste og viser dem
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(colorResource(id = R.color.sand), (colorResource(id = R.color.dusk)))
                        )
                    )
            ){
                //ett item per profil/person
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
            // hvis bruker ikke skal velge profiler til sammenligning viser vi frem
            // sammenligningen veg hjelp av Charts komponenten

            // henter først data om testen ved hjelp av testID lagret i hver profil
            LaunchedEffect(personTilSammenligning) {
                loading = true
                personMedScore = personTilSammenligning.map { person ->
                    val scores = PersonlighetstestRep(apiService).fetchScore(person.testid, lang)
                    ScoreUtils.lagChartsData(scores, person)
                }
                loading = false
            }

            Log.d("Sammenligning", "Personer med score: ${personMedScore.map { person -> person.name } }")
            // Hvis det ikke er noen i lista, så viser vi frem en melding istedenfor diagrammet
            if(loading){
                LoadingIndicator()
            }else if(personMedScore.isNotEmpty()){
                Charts(profilData = personMedScore)
            }
            else{ // legg til loading
                Text(text = stringResource(id = R.string.no_data))
            }

        }
        Spacer(modifier = Modifier.padding(bottom = 60.dp))
    }
}