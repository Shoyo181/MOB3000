package com.example.mob3000.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.models.Person


/**
 * Kort for å vise frem hver enkelt profil
 * Gjør den "clickable" slik at man kan trykke på kortet og dermed velge den
 */
@Composable
fun ProfilVelgeKort(
    person: Person,
    valgtePersoner: List<Person>,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors =
        if(valgtePersoner.contains(person)){
            CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.dusk2)
            )
        }else{
            CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.ivory)
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}