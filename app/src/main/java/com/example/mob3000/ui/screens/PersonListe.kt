package com.example.mob3000.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.example.mob3000.data.firebase.FirestoreService
import com.example.mob3000.R
import com.example.mob3000.data.models.Person
import com.example.mob3000.ui.components.PersonListe.EndrePerson
import com.example.mob3000.ui.components.PersonListe.LeggTilPerson
import com.example.mob3000.ui.components.PersonListe.PersonKort

/**
 * Screen komponent som viser frem alle personer/profiler bruker har tilgang til. Bruker kan
 * legge til profiler, redigere de og slette de. Kan også åpne ett nytt "vindu" med resultater
 * Når bruker utvider kortet til en person/profil ved å trykke på det, vil eventuelt det kortet
 * som var åpent lukke seg
 * Bruker komponetene: PersonKort, LeggTilPerson, EndrePerson
 * Bruker Scaffold, FloatingActionButton  fra Material3
 *
 * @param modifier Modifier for komponenten
 * @param navController Navigeringskontroller for navigasjon mellom skjermer
 *
 * Funksjoner
 * - Henter alle personer fra Firestore, ved hjelp av FirestoreService.hentPersoner i en korutine
 * - Lager en kort for hver person
 * - Lager en dialogvindu for å legge til en ny person
 * - Hvert kort kan utvides ved å trykke på de
 * - utvidet kort viser informasjon om personen og kan åpne rediger, slett og se resultater
 * - rediger og slett åpner ny dialogvindu for å /redigering/bekrefting
 * - resultater navigerer til ny skjerm/Screen med personens resultat, sender med testID og navn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListeScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    var personListe by remember {mutableStateOf<List<Person>>(emptyList())}
    var utvidetPerson by remember { mutableStateOf<String?>(null) }
    var personEndre by remember { mutableStateOf<Person?> (null)}
    var personDocRef by remember { mutableStateOf<String?> (null)}

    //var selectedResultID by remember {mutableStateOf<String?> (null)}

    /*-- State for å vise frem leggg til person dialog-boks --*/
    var visLeggTil by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        FirestoreService.hentPersoner(
            onSuccess = {fetchedePersoner -> personListe = fetchedePersoner },
            onFailure = {exception -> Log.e("Firestore", "Feil: $exception") }
        )

    }
    LaunchedEffect(personDocRef){
        Log.d("Firestore", "PersonRef: $personDocRef")
        if(personDocRef != null) {
            FirestoreService.leggTilDocRefPerson(
                id = personDocRef!!,
                onSuccess = {Log.d("Firestore", "PersonRef lagt til i DB")},
                onFailure = {exception -> Log.e("Firestore", "Feil: $exception")}
            )
        }
    }

    Scaffold(
        modifier = Modifier.padding(16.dp),
        floatingActionButton = {
            FloatingActionButton(
                // Dialogvindu for å legge til en ny person
                onClick = { visLeggTil = true },
                modifier = Modifier.padding(16.dp),
                containerColor = colorResource(id = R.color.ivory)
            ){
                Icon(Icons.Filled.Add, contentDescription = "Legg til person")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
             Spacer(modifier = Modifier.padding(30.dp))
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.sand),
                                (colorResource(id = R.color.dusk))
                            )
                        )
                    )
            ) {
                items(personListe) { person ->
                    PersonKort(
                        person = person,
                        erUtvidet = utvidetPerson == person.documentId,
                        onKortKlikket = {
                            utvidetPerson = if(utvidetPerson == person.documentId) null else person.documentId
                        },
                        onRediger = {personEndre = person},
                        onSlett = {
                            FirestoreService.slettPerson(
                                person = person,
                                onSuccess = {
                                    personListe = personListe.filter {it.documentId != person.documentId}
                                    utvidetPerson = null
                                },
                                onFailure = {exception ->
                                    Log.e("Firestore", "Feil ved sletting: ${exception.message}")
                                }
                            )
                        },
                        onSeResultat = {
                            // tar bort spesialtegn og mellomrom i navnet for å bruke som URL
                            val encodedName = Uri.encode(person.name)
                            navController.navigate("PersonTest/${person.testid}/${encodedName}")
                        }
                    )
                }
            }

            personEndre?.let {person ->
                EndrePerson(
                    person = person,
                    onDismiss = {personEndre = null},
                    onLagre = {oppdatertPerson ->
                        FirestoreService.oppdaterPerson(
                            oppdatertPerson,
                            onSuccess = {
                                personListe = personListe.map {
                                    if(it.documentId == oppdatertPerson.documentId) oppdatertPerson else it
                                }
                                personEndre = null
                            },
                            onFailure = {exception ->
                                Log.e("Firestore", "Feil ved oppdatering i DB: ${exception}")
                            }
                        )
                    }
                )
            }
                LeggTilPerson(
                    visLeggTil = visLeggTil,
                    onDismiss = {visLeggTil = false },
                    onLeggTilPerson = {newPerson ->
                        FirestoreService.leggTilPerson(
                            newPerson,
                            onSuccess =  {
                                Log.d("Firestore", "Person lagt til med autoID: $it")
                                personDocRef = it
                                         },
                            onFailure = {exception -> Log.e("Firestore", "Feil: $exception")}
                        )
                    }
                )
        }
    )
}

