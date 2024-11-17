package com.example.mob3000.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mob3000.data.models.ProfilData
import com.example.mob3000.ui.components.Chart


// side for å vise frem sammenligning
@Composable
fun Sammenlign(modifier: Modifier){
    val totScoreTest = listOf(10, 20, 35, 40, 50)
    val totScoreTest2 = listOf(60, 10, 50, 70, 40)

    val nevrotisismeTest = listOf(10, 20, 35, 40, 50, 60)
    val nevrotisismeTest2 = listOf(60, 10, 50, 70, 40, 40)

    val ekstroversjonTest = listOf(10, 20, 35, 40, 50, 60)
    val ekstroversjonTest2 = listOf(60, 10, 50, 70, 40, 40)

    val åpenhetForErfaringerTest = listOf(10, 20, 35, 40, 50, 60)
    val åpenhetForErfaringerTest2 = listOf(60, 10, 50, 70, 40, 40)

    val medmenneskelighetTest = listOf(10, 20, 35, 40, 50, 60)
    val medmenneskelighetTest2 = listOf(60, 10, 50, 70, 40, 40)

    val planmessighetTest = listOf(10, 20, 35, 40, 50, 60)
    val planmessighetTest2 = listOf(60, 10, 50, 70, 40, 40)

    val scoreDataTest = listOf(totScoreTest, nevrotisismeTest, ekstroversjonTest, åpenhetForErfaringerTest, medmenneskelighetTest, planmessighetTest)

    val scoreDataTest2 = listOf(totScoreTest2, nevrotisismeTest2, ekstroversjonTest2, åpenhetForErfaringerTest2, medmenneskelighetTest2, planmessighetTest2)

    val profiler = listOf(
        ProfilData("1", "Noldus", "kjartan@hotmail.com", scoreDataTest),
        ProfilData("2", "Skybert", "kjartan@hotmail.com", scoreDataTest2),
    )

    //
   Box(
       modifier = Modifier
           .fillMaxWidth()
           //.background(Color.White)
           .padding(16.dp)
   ) {
       Chart(profiler)
   }

}


