package com.example.mob3000.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.ui.components.InfoKort

@Composable
fun PersonTest(
    testId: String?,
    name: String?
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()

    ) {
        InfoKort(
            title = "${testId}, ${name}",
            description = stringResource(id = R.string.info_card_desc),
            backgroundColor = colorResource(id = R.color.ivory)
        )
    }
}

@Composable
fun TestResultatKort(
    title: String,
    description: String
){
    // henter info om en profil test
}