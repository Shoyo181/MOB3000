package com.example.mob3000

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign


@Composable
fun Home(modifier: Modifier = Modifier) {
    var showLoginnVindu by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Personlighetstest for bedrifter",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
               // .wrapContentSize(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.bilde_2),
            contentDescription = "Forsidebilde",
            modifier = Modifier
                .padding(6.dp)
                .size(200.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text("Bilde skal kanskje være her")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF817C52)
                )
            ) {
                Text("Sammenlign")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { showLoginnVindu = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF817C52)
                )
            )
            {
                Text("Logg inn")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


        Column {
            InfoKort(
                title = "Dette er en test på alignment. ",
                description = "Sjekk om den er i riktig posisjon",
                backgroundColor = Color(0xFF33333),
                image = painterResource(R.drawable.bilde_2)
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoKort(
                title = "Sjekker den her også",
                description = "Hvordan funker denne tru?",
                backgroundColor = Color(0xFFEEAC7E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoKort(
                title = "Dette er også en test",
                description = "Noe her",
                backgroundColor = Color(0xFF8F8360)
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoKort(
                title = "Dette er også en test",
                description = "Noe her",
                backgroundColor = Color(0xFFE9CF99)
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoKort(
                title = "Dette er også en test",
                description = "Noe her",
                backgroundColor = Color(0xFF8C523B)
            )
            Spacer(modifier = Modifier.height(8.dp))

        }
    }

    if (showLoginnVindu) {
        LoginDialog(onDismiss = {showLoginnVindu = false})
        }
    }
@Composable
fun InfoKort(
    title: String,
    description: String,
    backgroundColor: Color,
    image: Painter? = null) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if(image != null) {
                Image (
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp)
        }
    }
}

@Composable
fun LoginDialog(onDismiss: () -> Unit) {

    // State variabler for email og passord input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Logg inn") },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Passord") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(onClick = {

                //Handler for det som skjer etter bruker har trykket logg inn, men tom for nå.
                onDismiss() // Lukker dialogvindu.
            },
                    colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF817C52)),
            ) {
                Text("Logg inn")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss()
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF817C52)),
                ) {
                Text("Avbryt")
            }
        }
    )
}