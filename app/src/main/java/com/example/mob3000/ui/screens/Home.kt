package com.example.mob3000.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.mob3000.R
import com.example.mob3000.data.firebase.FirebaseService.hentAntallDokumenter
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
            text = stringResource(id = R.string.app_name), color = Color(0xFF66433F),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.welcome) + ", $email.",
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
                title = stringResource(id = R.string.info_card_title),
                description = stringResource(id = R.string.info_card_desc),
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
        title = stringResource(id = R.string.profiles_count),
        description = personerCount,
        backgroundColor = colorResource(id = R.color.ivory)
    )
}
