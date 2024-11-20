package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.api.Nettverksmodul.apiService
import com.example.mob3000.data.firebase.FirebaseService
import com.example.mob3000.data.models.Person
import com.example.mob3000.data.models.ProfilData
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.models.Score
import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreList
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.ui.components.Chart
import org.intellij.lang.annotations.Language


// side for å vise frem sammenligning
@Composable
fun Sammenlign(modifier: Modifier){
    val totScoreTest = listOf(10, 20, 35, 40, 50)
    val totScoreTest2 = listOf(60, 10, 50, 70, 40)

    val nevrotisismeTest = listOf(10, 20, 35, 40, 50, 60)
    val nevrotisismeTest2 = listOf(60, 10, 50, 70, 40, 40)

    val ekstroversjonTest = listOf(10, 20, 35, 40, 50, 60)
    val ekstroversjonTest2 = listOf(60, 10, 50, 70, 40, 40)

    val åpenhetForErfaringerTest = listOf(10, 20, 35, 40, 50, 60)
    val åpenhetForErfaringerTest2 = listOf(60, 10, 50, 70, 40, 40)

    val medmenneskelighetTest = listOf(10, 20, 35, 40, 50, 60)
    val medmenneskelighetTest2 = listOf(60, 10, 50, 70, 40, 40)

    val planmessighetTest = listOf(10, 20, 35, 40, 50, 60)
    val planmessighetTest2 = listOf(60, 10, 50, 70, 40, 40)

    val scoreDataTest = listOf(totScoreTest, nevrotisismeTest, ekstroversjonTest, åpenhetForErfaringerTest, medmenneskelighetTest, planmessighetTest)

    val scoreDataTest2 = listOf(totScoreTest2, nevrotisismeTest2, ekstroversjonTest2, åpenhetForErfaringerTest2, medmenneskelighetTest2, planmessighetTest2)

    val profiler = listOf(
        ProfilData("1", "Noldus", "kjartan@hotmail.com", scoreDataTest),
        ProfilData("2", "Skybert", "kjartan@hotmail.com", scoreDataTest2),
    )
    /*
    fun PersonDetailScreen(testID: String, onBack: () -> Unit, apiService: ApiService) {
    var scores by remember { mutableStateOf<List<Result>>(emptyList()) }
    val repo = remember { PersonlighetstestRep(apiService) }
    Log.d("API-test", "PersonDetails screen er oppe")
    Log.d("API-test", "TestID: $testID")
    // Forsøk nr.3
    LaunchedEffect(testID) {
        // kjører api kall når vi har fått en ny TestID
        Log.d("API-test", "Test av ny data")

        // henter data fra API
        scores = repo.fetchScore(testID)

        // resultatet skal inneholde all info om testen, ALL!!

        Log.d("API-test", "PersonDetails screen er ferdig")
        Log.d("API-test", "${scores.toString()}")



    }
    */

    // lage en liste over alle personer i databasen
    // bruker kan velge hvilke personer som skal sammenlignes
    // de som er valgt henter vi data fra ved hjelp av api kall
    // forer denne dataen inn til charts, for en visuell visning
    // det er en "switch" som går mellom sammenligneing og velging av profiler


    var valgIndex by remember { mutableStateOf(0) }
    val valgTilSammenligning = listOf("Velg Profiler", "Sammenlign Profiler")

    // liste over personer fra database
    var personListe by remember {mutableStateOf<List<Person>>(emptyList())}
    var personTilSammenligning by remember {mutableStateOf<List<Person>>(emptyList())}
    var personMedScore by remember {mutableStateOf<List<ScoreList>>(emptyList())}

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
                ){
                    Text(text = value)
                }
            }
        }
        if(valgIndex == 0){
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
                    TestKort(
                        person = person,
                        onClick = {
                            // legger person inn til sammenligning hvis den ikke allerede er med
                            if(personTilSammenligning.contains(person)){
                                personTilSammenligning = personTilSammenligning.filter { it != person }
                            }else{
                                personTilSammenligning = personTilSammenligning + person
                            }
                        }
                    )
                }
            }

        }else{
            Log.d("Sammenligning", "Personer med i sammenligning: ${personTilSammenligning.map { person -> person.name } }")
            // henter og sorterer data fra api

            LaunchedEffect(personTilSammenligning) {
                personMedScore = personTilSammenligning.map { person ->
                    val scores = hentDataFraApi(person.testid, lang, apiService)
                    sorterUtScore(scores, person)
                }
                Log.d("Sammenligning", "TEEEEEEST")
            }


            Log.d("Sammenligning", "Personer med score: ${personMedScore.map { person -> person.name } }")
            // sørg for at dette ikke looper
            Chart(personMedScore)
        }
    }

}

suspend fun hentDataFraApi(testID: String, language: String, apiService: ApiService): List<Result> {
    return try{
        val repo = PersonlighetstestRep(apiService)
        val scores = repo.fetchScore(testID, language)
        Log.d("API-test-hent_Score", "Full respons hentet: $scores")
        scores
    }catch (e: Exception){
        Log.e("API-test-hent_Score", "Feil ved henting av data: ${e.message}", e)
        emptyList()
    }
}

fun sorterUtScore(scores: List<Result>, person: Person): ScoreList {
    // innparameter er liste over test score for en profil/person
    // det er også masse infromasjon i denne klassen som vi ikke tenger

    val tempScoreData = mutableListOf<ScoreData>()

    // legger inn en ekstra facet som er hovedscore, bruker dummydata for domain og "Score"
    val totalScore = mutableListOf<Score>()
    for(s in scores){
        totalScore.add(Score(s.score, s.title))
    }

    tempScoreData.add(ScoreData("T", Score(0, "Big 5"), totalScore))

    for(i in scores.indices){
        val facet = mutableListOf<Score>()
        for(s in scores[i].facets){
            facet.add(Score(s.score, s.title))
        }
        tempScoreData.add(ScoreData(scores[i].domain, Score(scores[i].score, scores[i].title), facet))
    }

    Log.d("API-test", "Sortert liste: $tempScoreData")

    return ScoreList(person.name, tempScoreData)
}


@Composable
fun TestKort(
    person: Person,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
