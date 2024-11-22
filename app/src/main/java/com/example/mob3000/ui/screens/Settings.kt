package com.example.mob3000.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.firebase.AuthService
import com.example.mob3000.ui.components.AnimertCircularProgressIndicator
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.InfoKort
import kotlinx.coroutines.delay


@Composable
fun Settings (
    modifier: Modifier
)

{
var progress = 0.75f
    Box {
        Column(

            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()

        ) {
            InfoKort(
                title = "Settings",
                description = stringResource(id = R.string.info_card_desc),
                backgroundColor = colorResource(id = R.color.ivory)

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ButtonKomponent(
                    text = "Logg ut",
                    onClick = { AuthService.loggUt() },
                )
            }

            AnimertCircularProgressIndicator(sumResultat = 0.75f, 100)
        }
    }
}
