import androidx.compose.foundation.background
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


data class Person(
    val name: String,
    val age: String,
    val email: String,
    val testid: String
)

@Composable
fun PersonListScreen(modifier: Modifier) {
    // State for å holde personer på listen
    var personList by remember { mutableStateOf(listOf
        (Person("Kjartan Øyen", "30", "kjartan@hotmail.com", "8Y8YFSHDS"),
        Person("Mie Rønningen", "25", "mie@hotmail.com", "JISFAS8SS"),
        Person("Skybert", "0", "sky@baert.no", "JIASJFASD8"),
        Person("Noldus", "23", "noldus@hotmail.com", "8Y8YFSHKG"),
        Person("Mummitrollet", "2", "mum@hotmail.com", "8Y8YFSHsfa" ),
        Person("Skybert", "0", "sky@baert.no", "JIASJFASD8"),
        )) }

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
                containerColor = Color(0xFF817C52)
                ){
                Icon(Icons.Filled.Add, contentDescription = "Legg til person")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            NavigationBar {  }
        },
        content = { innerPadding ->
            Box (modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4E2D0))
            )
            // Vise listen med personene som er laget
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(personList) { person ->
                    PersonCard(
                        person)
                }
            }

            // Legg-til-person vindu
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            if (newPersonNavn.isNotEmpty() && newPersonAlder.isNotEmpty() && newPersonEmail.isNotEmpty() && newTestID.isNotEmpty()) {
                                personList = personList + Person(newPersonNavn, newPersonAlder, newPersonEmail, newTestID)
                                showDialog = false
                                newPersonNavn = ""
                                newPersonAlder = ""
                                newPersonEmail = ""
                                newTestID = ""
                            }
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF817C52)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(8.dp)
                            ) {
                            Text("Legg til person")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF817C52)),
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
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Alder input
                            OutlinedTextField(
                                value = newPersonAlder,
                                onValueChange = { newPersonAlder = it },
                                label = { Text("Alder") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Email input
                            OutlinedTextField(
                                value = newPersonEmail,
                                onValueChange = { newPersonEmail = it },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Email input
                            OutlinedTextField(
                                value = newTestID,
                                onValueChange = { newTestID = it },
                                label = { Text("TestId") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun PersonCard(person: Person) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
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
