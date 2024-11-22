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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.mob3000.R
import com.example.mob3000.data.firebase.FirebaseService.hentAntallDokumenter
import com.example.mob3000.ui.components.InfoKort
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
            text = stringResource(id = R.string.app_name), color = colorResource(id = R.color.maghogny),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.welcome) + ", $email.",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Spacer(modifier = Modifier.height(4.dp))
        }
        Column {
            InfoKort(
                title = stringResource(id = R.string.info_card_title),
                description = stringResource(id = R.string.info_card_desc),
                backgroundColor = colorResource(id = R.color.ivory),
                image = painterResource(id = R.drawable.chartt_transparent1)
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoKortMedPersonerCount()

            Spacer(modifier = Modifier.padding(30.dp))
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
    Box(
        modifier = Modifier.padding(80.dp, 10.dp),
        contentAlignment = Alignment.Center
    ) {

        InfoKort(
            title = stringResource(id = R.string.profiles_count),
            description = personerCount,
            backgroundColor = colorResource(id = R.color.ivory),
            modifier = Modifier
                .width(50.dp)
        )
    }
}
