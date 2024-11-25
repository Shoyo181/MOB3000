package com.example.mob3000.ui.screens


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
import com.example.mob3000.data.firebase.AuthService
import com.example.mob3000.ui.components.ButtonKomponent
import com.example.mob3000.ui.components.InfoKort


@Composable
fun Settings (
    modifier: Modifier
    ){
    val kontekst = LocalContext.current
    val lang = stringResource(id = R.string.language_api)
    val url = "https://bigfive-test.com/${lang}"

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            )
            {
                ButtonKomponent(
                    text = "Logg ut",
                    onClick = { AuthService.loggUt() },
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            InfoKort(
                title = stringResource(id = R.string.information),
                description = stringResource(id = R.string.information_version)
                        +"\n"+ stringResource(id = R.string.information_desc) + "\n" + stringResource(id = R.string.information_madeby),
                backgroundColor = colorResource(id = R.color.ivory)
            )

            Spacer(modifier = Modifier.padding(16.dp))

            InfoKort(
                title = stringResource(id = R.string.more_information),
                description = stringResource(id = R.string.rubynor_info),
                backgroundColor = colorResource(id = R.color.ivory),
                button = {
                    ButtonKomponent(text = "BigFive RubyNor",
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            kontekst.startActivity(intent)
                        }
                    )
                }
            )
        }
}