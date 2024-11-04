package com.example.mob3000

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun Settings (
    modifier: Modifier
){
    Button(
        onClick = {AuthService.loggUt() },
        modifier = Modifier
            .padding(20.dp),
        colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff66433F)),
    ){
        Text ("Logg ut")
    }
}


