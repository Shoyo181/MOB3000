package com.example.mob3000.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mob3000.R
/**
 * Gjenbrukbar komponent for button.
 * Denne blir brukt i de fleste buttons i applikasjonen.
 *
 *
 * @param text Teksten som skal vises på knappen
 * @param onClick Callback som blir kalt når knappen trykkes
 * @param modifier Modifier for knappen
 * @param containerColor Farge for knappen
 * @param contentColor Farge for tekst
 * @param shape Form for knappen
 * @param padding Padding rundt knappen
 */
@Composable
fun ButtonKomponent(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(id = R.color.dusk),
    contentColor: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    padding: PaddingValues = PaddingValues(8.dp)
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        shape = shape,
        modifier = modifier.padding(padding)
    ) {
        Text(
            text,
            fontWeight = FontWeight.Medium,)
    }
}
