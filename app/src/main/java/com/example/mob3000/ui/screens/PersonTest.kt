package com.example.mob3000.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.R
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