package com.example.mob3000.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mob3000.R

@Composable
fun ButtonKomponent(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(id = R.color.pinkfluff),
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
        Text(text)
    }
}
