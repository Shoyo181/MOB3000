package com.example.mob3000.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.R
import com.example.mob3000.data.api.Nettverksmodul
import com.example.mob3000.data.models.ApiData.Facet
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.repository.PersonlighetstestRep
import com.example.mob3000.data.repository.ScoreUtils
import com.example.mob3000.ui.components.PersonTest.AnimertPaiGraf
import com.example.mob3000.ui.components.LoadingIndicator
import com.example.mob3000.ui.components.PersonTest.DelInfoMedTekst
import com.example.mob3000.ui.components.PersonTest.DelTestKort
import com.example.mob3000.ui.components.PersonTest.TekstDeler
import com.example.mob3000.ui.components.PersonTest.TestKort
import com.example.mob3000.ui.components.PersonTest.TestResultatSamling

/**
 * Screen komponent som viser frem testresultat for en profil samt navn til person/profil
 * Bruker TestResultatSamling.kt for å vise resultatene
 *
 * @param testId Id til test - sendes vidre til TestResultatSamling
 * @param name Navn til person/profil
 */
@Composable
fun PersonTest(
    testId: String?,
    name: String?
) {
    val lang = stringResource(id = R.string.language_api)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.name) + ": $name",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.maghogny)
        )
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        TestResultatSamling(
            testId = testId ?: "",
            lang = lang,
            backgroundColor = colorResource(id = R.color.ivory)
        )
        Spacer(modifier = Modifier.padding(bottom = 60.dp))
    }
}