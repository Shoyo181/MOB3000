package com.example.mob3000.ui.screens

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.firebase.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.example.mob3000.R
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.OutlinedTextFieldKomponent
import com.google.firebase.firestore.DocumentId
import com.example.mob3000.data.models.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListeScreen(modifier: Modifier = Modifier, navController: NavHostController) {

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
                        },
                        onSeResultat = {
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
    onSlett: () -> Unit,
    onSeResultat: () -> Unit
) {
    var visSlettDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
            elevation = 12.dp,
            shape = RoundedCornerShape(16.dp),
                ambientColor = colorResource(id = R.color.dusk3).copy(alpha = 0.7f),
                spotColor = colorResource(id = R.color.dusk3).copy(alpha = 0.7f))
            .clickable(onClick = onKortKlikket),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.ivory))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleLarge,
                color = colorResource(id = R.color.maghogny),
            )
            if(erUtvidet) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(id = R.string.age) + " : ${person.age}")
                Text(text = stringResource(id = R.string.email) + " : ${person.email}")
                Text(text = stringResource(id = R.string.testid) + " : ${person.testid}")

                Spacer(modifier = Modifier.height(8.dp))
                Row (
                    modifier = Modifier
                        .padding(1.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ButtonKomponent(
                        text = stringResource(id = R.string.edit),
                        onClick = onRediger,
                        padding = PaddingValues(3.dp)
                    )
                    ButtonKomponent(
                        text = stringResource(id = R.string.delete),
                        onClick = { visSlettDialog = true },
                        padding = PaddingValues(3.dp)
                    )
                    ButtonKomponent(
                        text = stringResource(id = R.string.results),
                        onClick = onSeResultat,
                        padding = PaddingValues(3.dp)
                    )
                }
            }
        }
    }
    if (visSlettDialog) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { visSlettDialog = false },
            confirmButton = {
                ButtonKomponent(
                    text = stringResource(id = R.string.yes_delete),
                    onClick = {
                        onSlett()
                        visSlettDialog = false
                    },
                    padding = PaddingValues(4.dp)
                )
            },
            dismissButton = {
                ButtonKomponent(
                    text = stringResource(id = R.string.cancel),
                    onClick = { visSlettDialog = false },
                    padding = PaddingValues(4.dp)
                )
            },
            title = { Text(stringResource(id = R.string.confirm_delete)) },
            text = { Text(stringResource(id = R.string.question_delete)) }
        )
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
                    text = stringResource(id = R.string.alert_createprofile_create),
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
                    text = stringResource(id = R.string.cancel),
                    onClick = onDismiss
                )
            },
            title = { Text(stringResource(id = R.string.alert_createprofile_title)) },
            text = {
                Column {
                    OutlinedTextFieldKomponent(
                        value = newPersonNavn,
                        onValueChange = { newPersonNavn = it },
                        label = stringResource(id = R.string.name)
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonAlder,
                        onValueChange = { newPersonAlder = it },
                        label = stringResource(id = R.string.age)
                    )
                    OutlinedTextFieldKomponent(
                        value = newPersonEmail,
                        onValueChange = { newPersonEmail = it },
                        label = stringResource(id = R.string.email)
                    )
                    OutlinedTextFieldKomponent(
                        value = newTestID,
                        onValueChange = { newTestID = it },
                        label = stringResource(id = R.string.testid)

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
                text = stringResource(id = R.string.alert_edit_save),
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
                text = stringResource(id = R.string.cancel),
                onClick = onDismiss
            )
        },
        title = {Text(stringResource(id = R.string.alert_edit_title))},
        text = {
            Column {
                OutlinedTextFieldKomponent(
                    value = oppdatertNavn,
                    onValueChange = {oppdatertNavn = it},
                    label = stringResource(id = R.string.name)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertAlder,
                    onValueChange = { oppdatertAlder = it },
                    label = stringResource(id = R.string.age)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertEpost,
                    onValueChange = { oppdatertEpost = it },
                    label = stringResource(id = R.string.email)
                )
                OutlinedTextFieldKomponent(
                    value = oppdatertTestId,
                    onValueChange = { oppdatertTestId = it },
                    label = stringResource(id = R.string.testid)
                )
            }
        }
    )
}


