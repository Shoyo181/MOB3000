package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.api.Result
import com.example.mob3000.data.api.ScoreService
import com.example.mob3000.data.firebase.FirebaseService
import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreResponse
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.data.repository.ScoreRepo
import com.example.mob3000.ui.components.ResultChart
import com.google.firebase.auth.FirebaseAuth


data class Person(
    val name: String,
    val age: String,
    val email: String,
    val testid: String,
    val userId: String
)
{
    constructor() : this ("", "", "", "", "")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListScreen(modifier: Modifier) {

    var personList by remember {mutableStateOf<List<Person>>(emptyList())}
    var selectedResultID by remember {mutableStateOf<String?> (null)}

    LaunchedEffect(Unit) {
        FirebaseService.hentPersoner(
            onSuccess = {fetchedePersoner ->
                personList = fetchedePersoner
            },
            onFailure = {exception -> print ("feil")
            }
        )
    }
    // State for å vise dialogvindu
    var showDialog by remember { mutableStateOf(false) }

    //State for å selektere en person for å endre eller slette.
    var valgtPerson by remember { mutableStateOf<Person?>(null)}
    var visEndreSletteDialog by remember {mutableStateOf(false)}
    // state for å lage en ny person
    var newPersonNavn by remember { mutableStateOf("") }
    var newPersonAlder by remember { mutableStateOf("") }
    var newPersonEmail by remember { mutableStateOf("") }
    var newTestID by remember {mutableStateOf("") }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Dialogvindu for å legge til en ny person
                showDialog = true
            }, modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFFF5F5F2)
            ){
                Icon(Icons.Filled.Add, contentDescription = "Legg til person")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            NavigationBar {  }
        },
        content = { innerPadding ->
            if (selectedResultID != null) {
                PersonDetailScreen(
                    testID = selectedResultID!!,
                    apiService = Nettverksmodul.apiService, // Pass the apiService instance
                    onBack = {
                        selectedResultID = null
                    }
                )
            } else {
            // Vise listen med personene som er laget
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFEAD1BA), Color(0xFF817A81))
                    ))
            ) {
                items(personList) { person ->
                    PersonCard(
                        person = person,
                        onClick = { selectedResultID = person.testid
                        Log.d("API-Test", "Kall personcard, testId: ${person.testid}")}
                    )
                }
            }

            // Legg-til-person vindu
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            if (newPersonNavn.isNotEmpty() && newPersonAlder.isNotEmpty() && newPersonEmail.isNotEmpty() && newTestID.isNotEmpty()) {
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                val nyPerson = Person(
                                    name = newPersonNavn,
                                    age = newPersonAlder,
                                    email = newPersonEmail,
                                    testid = newTestID,
                                    userId = currentUser?.uid ?: ""
                                )
                                FirebaseService.leggTilPerson(
                                    nyPerson,
                                    onSuccess = {
                                        Log.d("Firestore", "Dokument laget")
                                        showDialog = false
                                        newPersonNavn = ""
                                        newPersonAlder = ""
                                        newPersonEmail = ""
                                        newTestID = ""
                                    },
                                    onFailure = {exception -> Log.e("Firestore", "Dokument feilet ved lagring")}
                                )
                            }
                        },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA18073)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Legg til person")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA18073)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Avbryt")
                        }
                    },
                    title = { Text("Legg til person") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newPersonNavn,
                                onValueChange = { newPersonNavn = it },
                                label = { Text("Navn") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Alder input
                            OutlinedTextField(
                                value = newPersonAlder,
                                onValueChange = { newPersonAlder = it },
                                label = { Text("Alder") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Email input
                            OutlinedTextField(
                                value = newPersonEmail,
                                onValueChange = { newPersonEmail = it },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Email input
                            OutlinedTextField(
                                value = newTestID,
                                onValueChange = { newTestID = it },
                                label = { Text("TestId") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White
                                )
                            )
                        }
                    }
                )}
            }
        }
    )
}

@Composable
fun PersonCard(person: Person, onClick: () -> Unit ) {
    Log.d("API-test", "Personcard on click.")
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
            Text(text = person.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Alder: ${person.age}")
            Text(text = "Email: ${person.email}")
            Text(text="TestID: ${person.testid}")
        }
    }
}
@Composable
fun PersonDetailScreen(testID: String, onBack: () -> Unit, apiService: ApiService) {
    var scores by remember { mutableStateOf<List<Result>>(emptyList()) }
    val repo = remember { PersonlighetstestRep(apiService) }

    var testScores by remember { mutableStateOf<List<ScoreData>>(emptyList()) }
    //val scoreRepo = remember { ScoreRepo(apiService) }

    Log.d("API-test", "PersonDetails screen er oppe")
    Log.d("API-test", "TestID: $testID")


    // Forsøk nr.3
    LaunchedEffect(testID) {
        // kjører api kall når vi har fått en ny TestID
        Log.d("API-test", "Test av ny data")

        // henter data fra API
        scores = repo.fetchScore(testID)

        // resultatet skal inneholde all info om testen, ALL!!
    }



    /*
    LaunchedEffect(testID) {
        Log.d("API-test", "Test av ny data")

        testScores = scoreRepo.fetchAllScore(testID)

        Log.d("API-test", testScores.toString())

        for (score in testScores) {
            Log.d("API-test", "Domain: ${score.domain}, Title: ${score.score.title}, Score: ${score.score.score}")
            for(facet in score.facets) {
                Log.d("API-test", "Title: ${facet.title}, Score: ${facet.score}")
            }
        }
    }
    *//*

        scores = repo.fetchScore(resultID)
        Log.d("API-test", "PersonDetails screen er ferdig")
        for (score in scores) {
            Log.d("API-test", "Domain: ${score.domain}, Score: ${score.score}")
        }
    }*/

    Column (
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEAD1BA), Color(0xFF817A81))
                ))
            .fillMaxHeight()
    ) {
        Button(onClick = onBack,
            modifier = Modifier
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66433F))
        ) {
            Text("Tilbake til liste med personer")
        }

        if (scores.isNotEmpty()) {
            ResultChart(scores)
            Log.d("API-test", "Domain: ${scores[0].domain}, Score: ${scores[0].score}")
        } else {
            Text("Loading...", modifier = Modifier.padding(16.dp))
        }
    }
}


