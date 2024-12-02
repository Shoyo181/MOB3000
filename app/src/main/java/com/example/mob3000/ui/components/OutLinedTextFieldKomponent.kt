package com.example.mob3000.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R

/**
 * Gjenbrukbar komponent for OutlinedTextField som brukes i ulike komponenter i appen.
 * Bruker OutlinedTextField fra Material Design for å lage en tekstfelt.
 *
 * @param value Verdien som vises i tekstfeltet
 * @param onValueChange En funksjon som blir kalt når verdien i tekstfeltet endres
 * @param label Teksten som vises som label for tekstfeltet
 * @param modifier Modifier for tekstfeltet
 */
@Composable
fun OutlinedTextFieldKomponent(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label) },
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.ivory),
            unfocusedContainerColor = colorResource(id = R.color.ivory),
            focusedBorderColor = colorResource(id = R.color.dusk)
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
}
