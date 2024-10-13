import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class Person(
    val name: String,
    val age: String,
    val email: String
)

@Composable
fun PersonListScreen() {
    // State for å holde personer på listen
    var personList by remember { mutableStateOf(listOf(Person("Kjartan Øyen", "30", "kjartan@hotmail.com"), Person("Mie Rønningen", "25", "mie@hotmail.com"))) }

    // State for å vise dialogvindu
    var showDialog by remember { mutableStateOf(false) }

    // state for å lage en ny person
    var newPersonName by remember { mutableStateOf("") }
    var newPersonAge by remember { mutableStateOf("") }
    var newPersonEmail by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Dialogvindu for å legge til en ny person
                showDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Legg til person")
            }
        },
        content = { innerPadding ->
            // Vise listen med personene som er laget
            LazyColumn(
                contentPadding = innerPadding,

                modifier = Modifier.fillMaxSize()
            ) {
                items(personList) { person ->
                    PersonCard(person)
                }
            }

            // Legg-til-person vindu
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            if (newPersonName.isNotEmpty() && newPersonAge.isNotEmpty() && newPersonEmail.isNotEmpty()) {
                                personList = personList + Person(newPersonName, newPersonAge, newPersonEmail)
                                showDialog = false
                                newPersonName = ""
                                newPersonAge = ""
                                newPersonEmail = ""
                            }
                        }) {
                            Text("Legg til person")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Avbryt")
                        }
                    },
                    title = { Text("Legg til person") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newPersonName,
                                onValueChange = { newPersonName = it },
                                label = { Text("Navn") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Alder input
                            OutlinedTextField(
                                value = newPersonAge,
                                onValueChange = { newPersonAge = it },
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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = person.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Alder: ${person.age}")
            Text(text = "Email: ${person.email}")
        }
    }
}
