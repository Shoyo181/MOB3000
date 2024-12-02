package com.example.mob3000.ui.components.SwipeLandingsside

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Hjelpe komponent for å lage en indikator for hver side i swipingen
 */
@Composable
fun Dot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(
                color = if (isSelected) Color.White else Color.LightGray,
                shape = CircleShape
            )
    )
}
