package com.example.mob3000.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.firebase.AuthService
import com.example.mob3000.ui.components.ButtonKomponent

@Composable
fun Settings (
    modifier: Modifier
){
    Column {
        Box (
            modifier = Modifier
                .padding(16.dp)
                .background(colorResource(id = R.color.ivory)),
        ) {
            Text(
                text = "Her kommer det mer innstillinger for brukeren etterhvert. Blandt annet det å kunne redigere informasjon, logge ut, slette bruker o.l."
            )
        }
        ButtonKomponent(
            text = "Logg ut",
            onClick = { AuthService.loggUt() },
        )
    }
}


