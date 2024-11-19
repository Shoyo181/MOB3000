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
import androidx.compose.ui.res.colorResource
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.api.ScoreService
import com.example.mob3000.data.firebase.FirebaseService
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreResponse
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.data.repository.ScoreRepo
import com.example.mob3000.ui.components.ResultChart
import com.google.firebase.auth.FirebaseAuth
import com.example.mob3000.R
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.OutlinedTextFieldKomponent
import com.google.firebase.firestore.DocumentId
import com.example.mob3000.data.models.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListeScreen(modifier: Modifier) {

    var personListe by remember {mutableStateOf<List<Person>>(emptyList())}
    var utvidetPerson by remember { mutableStateOf<String?>(null) }
    var personEndre by remember { mutableStateOf<Person?> (null)}

    //var selectedResultID by remember {mutableStateOf<String?> (null)}

    /*-- State for å vise frem leggg til person dialog-boks --*/
    var visLeggTil by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        FirebaseService.hentPersoner(
            onSuccess = {fetchedePersoner -> personListe = fetchedePersoner },
            onFailure = {exception -> Log.e("Firestore", "Feil: $exception") }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                // Dialogvindu for å legge til en ny person
                onClick = { visLeggTil = true },
                modifier = Modifier.padding(16.dp),
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
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                    Brush.verticalGradient(
                        colors = listOf(colorResource(id = R.color.sand), (colorResource(id = R.color.dusk)))
                    )
                )
            ) {
                items(personListe) { person ->
                    PersonKort(
                        person = person,
                        erUtvidet = utvidetPerson == person.testid,
                        onKortKlikket = {
                            utvidetPerson = if(utvidetPerson == person.testid) null else person.testid
                        },
                        onRediger = {personEndre = person},
                        onSlett = {
                            FirebaseService.slettPerson(
                                person = person,
                                onSuccess = {
                                    personListe = personListe.filter {it.testid != person.testid}
                                    utvidetPerson = null
                                },
                                onFailure = {exception ->
                                    Log.e("Firestore", "Feil ved sletting: ${exception.message}")
                                }
                            )
                        }
                    )
                }
            }

            personEndre?.let {person ->
                EndrePerson(
                    person = person,
                    onDismiss = {personEndre = null},
                    onLagre = {oppdatertPerson ->
                        FirebaseService.oppdaterPerson(
                            oppdatertPerson,
                            onSuccess = {
                                personListe = personListe.map {
                                    if(it.testid == oppdatertPerson.testid) oppdatertPerson else it
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
                        FirebaseService.leggTilPerson(
                            newPerson,
                            onSuccess =  {Log.d("Firestore", "Person lagt til") },
                            onFailure = {exception -> Log.e("Firestore", "Feil: $exception")}
                        )
                    }
                )
        }
    )
}
@Composable
fun PersonKort(
    person: Person,
    erUtvidet: Boolean,
    onKortKlikket: () -> Unit,
    onRediger: () -> Unit,
    onSlett: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onKortKlikket),
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
            if(erUtvidet) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Alder: ${person.age}")
                Text(text = "Email: ${person.email}")
                Text(text = "TestID: ${person.testid}")

                Spacer(modifier = Modifier.height(8.dp))
                ButtonKomponent(
                    text = "Endre",
                    onClick = onRediger
                )
                Spacer(modifier = Modifier.height(8.dp))
                ButtonKomponent(
                    text = "Slett",
                    onClick = onSlett
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeggTilPerson(
    visLeggTil: Boolean,
    onDismiss: () -> Unit,
    onLeggTilPerson: (Person) -> Unit
) {
    if (visLeggTil) {
        var newPersonNavn by remember { mutableStateOf("") }
        var newPersonAlder by remember { mutableStateOf("") }
        var newPersonEmail by remember { mutableStateOf("") }
        var newTestID by remember { mutableStateOf("") }


        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                ButtonKomponent(
                    text = "Legg til person",
                    onClick = {
                        if (newPersonNavn.isNotEmpty() && newPersonAlder.isNotEmpty() &&
                            newPersonEmail.isNotEmpty() && newTestID.isNotEmpty()
                        ) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val nyPerson = Person(
                                name = newPersonNavn,
                                age = newPersonAlder,
                                email = newPersonEmail,
                                testid = newTestID,
                                userId = currentUser?.uid ?: ""
                            )
                            onLeggTilPerson(nyPerson)
                            onDismiss()
                        }
                    }
                )
            },
            dismissButton = {
                ButtonKomponent(
                    text = "Avbryt",
                    onClick = onDismiss
                )
            },
            title = { Text("Legg til person") },
            text = {
                Column {
                    OutlinedTextFieldKomponent(
                        value = newPersonNavn,
                        onValueChange = { newPersonNavn = it },
                        label = "Navn",
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonAlder,
                        onValueChange = { newPersonAlder = it },
                        label = "Alder",
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonEmail,
                        onValueChange = { newPersonEmail = it },
                        label = "Email"
                    )
                    OutlinedTextFieldKomponent(
                        value = newTestID,
                        onValueChange = { newTestID = it },
                        label = "TestId"

                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndrePerson (
    person: Person,
    onDismiss: () -> Unit,
    onLagre: (Person) -> Unit
) {
    var oppdatertNavn by remember {mutableStateOf(person.name)}
    var oppdatertAlder by remember {mutableStateOf(person.age)}
    var oppdatertEpost by remember {mutableStateOf(person.email)}
    var oppdatertTestId by remember {mutableStateOf(person.testid)}

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ButtonKomponent(
                text = "Lagre",
                onClick = {
                    val oppdatertPerson = person.copy(
                        name = oppdatertNavn,
                        age = oppdatertAlder,
                        email = oppdatertEpost,
                        testid = oppdatertTestId
                    )
                    onLagre(oppdatertPerson)
                    onDismiss()
                }
            )
        },
        dismissButton = {
            ButtonKomponent(
                text = "Avbryt",
                onClick = onDismiss
            )
        },
        title = {Text("Endre person")},
        text = {
            Column {
                OutlinedTextFieldKomponent(
                    value = oppdatertNavn,
                    onValueChange = {oppdatertNavn = it},
                    label = "Navn"
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertAlder,
                    onValueChange = { oppdatertAlder = it },
                    label = "Alder"
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertEpost,
                    onValueChange = { oppdatertEpost = it },
                    label = "Email"
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertTestId,
                    onValueChange = { oppdatertTestId = it },
                    label = "Email"
                )
            }
        }
    )
}


