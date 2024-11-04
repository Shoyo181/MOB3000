package com.example.mob3000


import android.R.id.bold
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Home(modifier: Modifier = Modifier) {
    var showLoginnVindu by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val brukerInnlogget = FirebaseAuth.getInstance().currentUser
    val email = brukerInnlogget?.email ?: "Ingen epost tilgjengelig"
    Column(
        modifier = Modifier
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Big Five Bedrift", color = Color(0xFF66433F),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Velkommen, $email.",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        Column {
            InfoKort(
                title = "Litt info om hvordan dette skal fungere",
                description = "Her kan du lage profiler med informasjon om dine ansatte og få opp personlighetstest resultater. Etter å ha laget ulike ansatte med den informasjonen som trengs, som du kan gjøre i Profiler - så kan du etter gjøre sammenligninger mellom de ulike ansatte du har laget i Sammenlign.",
                backgroundColor = colorResource(id = R.color.ivory)
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoKortMedPersonerCount()

        }
    }
}
@Composable
fun InfoKort(
    title: String,
    description: String,
    backgroundColor: Color,
    image: Painter? = null) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
        .shadow(
            elevation = 12.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Black.copy(alpha = 0.2f),
            spotColor = Color.Black.copy(alpha = 0.2f)
    ),
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
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xff66433F))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xff817A81))
        }
    }
}
@Composable
fun InfoKortMedPersonerCount() {
    var personerCount by remember { mutableStateOf("0") }

    LaunchedEffect(Unit) {
        hentAntallDokumenter(
            onResult = { count -> personerCount = count.toString() },
            onFailure = { error("Gikk ikke å hente antall dokumenter") }
        )
    }

    InfoKort(
        title = "Profiler laget",
        description = personerCount,
        backgroundColor = colorResource(id = R.color.ivory)
    )
}
fun hentAntallDokumenter(onResult: (Int) -> Unit, onFailure: (Exception) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if(userId != null) {
        FirebaseFirestore.getInstance().collection("Personer")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener{documents ->
                val antall = documents.size()
                onResult(antall)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    } else {
        onFailure(Exception("Bruker er ikke logget inn."))
    }
}