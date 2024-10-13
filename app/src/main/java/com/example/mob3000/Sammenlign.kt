package com.example.mob3000

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.unit.dp


// side for å vise frem sammenligning
@Composable
fun Sammenlign(modifier: Modifier){
    Text(
        text = "Sammenlign dine profiler"
    )

    val data1 = listOf(
        listOf(60, 37, 17, 24, 75), // Data for første søyle
        listOf(26, 10, 50, 70, 10), // Data for andre søyle

    )
    val data2 = listOf(
        listOf(60, 37, 17, 24, 75, 50), // Data for første søyle
        listOf(26, 10, 50, 70, 10, 40), // Data for andre søyle
        // ... (data for flere søyler)
    )
    val data3 = listOf(
        listOf(10, 20, 35, 40, 50, 60), // Data for første søyle
        listOf(60, 10, 50, 70, 40, 40) // Data for andre søyle
    )

    val data1Labels = remember { listOf("Nevrotisisme", "Ekstroversjon", "ÅpenhetForErfaringer", "Medmenneskelighet", "Planmessighet") }
    val data2Labels = remember { listOf("Angst", "Sinne", "Deprisjon", "Selvbevissthet", "Impulsivitet", "Sårbarhet") }
    val data3Labels = remember { listOf("1", "2", "3", "4", "5", "6") }

    LazyColumn(
        modifier = Modifier.padding(16.dp), // Padding rundt LazyColumn
        verticalArrangement = Arrangement.spacedBy(16.dp) // Mellomrom mellom items
    ) {
        item {
            BarChart("Big 5 Test",data1, data1Labels)
        }
        item {
            BarChart("Nevrotisme",data2, data2Labels)
        }
        item {
            BarChart("Test",data3, data3Labels)
        }
        item {
            BarChart("Test",data3, data3Labels)
        }
    }

}


