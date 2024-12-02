package com.example.mob3000.ui.components.PersonListe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.models.Person
import com.example.mob3000.ui.components.AlertDialogKomponent
import com.example.mob3000.ui.components.ButtonKomponent

/**
 * Komponent for å vise frem ett personkort med informasjon om gitt person/profil
 * Gjør kortet trykkbar for å se mer informasjon om personen
 * Bruker Card fra Material Design
 * Bruker AlertDialogKomponent og ButtonKomponent
 *
 * @param person Person/profil som skal vises frem
 * @param erUtvidet Boolean som sier om kortet er utvidet eller ikke
 * @param onKortKlikket Lambda som kjøres når kortet trykkes
 * @param onRediger Lambda som kjøres når rediger-knappen trykkes
 * @param onSlett Lambda som kjøres når slett-knappen trykkes
 * @param onSeResultat Lambda som kjøres når resultater-knappen trykkes
 *
 * Funksjoner
 * - Viser en dialog når slett-knappen trykkes
 * - Håndterer om kortet er utvidet eller ikke, viser informasjon deretter
 */
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
                Text(text = stringResource(id = R.string.stilling) +" : ${person.stilling}")
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
        AlertDialogKomponent(
            visDialog = visSlettDialog,
            tittel = stringResource(id = R.string.confirm_delete),
            tekst = stringResource(id = R.string.question_delete),
            bekreftTekst = stringResource(id = R.string.confirm),
            avbrytTekst = stringResource(id = R.string.cancel),
            onBekreft = { onSlett () },
            onAvbryt = {visSlettDialog = false}
        )
    }
}
