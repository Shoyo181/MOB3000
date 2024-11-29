package com.example.mob3000.data.repository

import android.util.Log
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import com.example.mob3000.R
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.models.Person
import com.example.mob3000.data.models.Score
import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreList
import ir.ehsannarmani.compose_charts.models.Bars

object ScoreUtils{
    fun lagChartsData(scores: List<Result>, person: Person): ScoreList {
        // innparameter er liste over test score for en profil/person
        // det er også masse infromasjon i denne klassen som vi ikke tenger

        val tempScoreData = mutableListOf<ScoreData>()

        // legger inn en ekstra facet som er hovedscore, bruker dummydata for domain og "Score"
        val totalScore = mutableListOf<Score>()
        for(s in scores){
            totalScore.add(Score(s.score, s.title))
        }

        tempScoreData.add(ScoreData("T", Score(0, "Big 5"), totalScore))

        for(i in scores.indices){
            val facet = mutableListOf<Score>()
            for(s in scores[i].facets){
                facet.add(Score(s.score, s.title))
            }
            tempScoreData.add(ScoreData(scores[i].domain, Score(scores[i].score, scores[i].title), facet))
        }

        Log.d("API-test", "Sortert liste: $tempScoreData")

        return ScoreList(person.name, tempScoreData)
    }

    fun barsBuilder (profilData: List<ScoreList>, index: Int, color: List<SolidColor>): List<Bars> {
        // antall søyler for dette diagrammet
        val numBars = profilData[0].results[index].facets.size

        //Log.d("barsBuilder-info-space", "-----------------------------")
        //Log.d("barsBuilder-info", "tittel index: " + tittel[index].size)
        //Log.d("barsBuilder-info", "numBars     : " + numBars)

        // lager ferdig data for hva som skal vises i diagrammet
        val barsData = (0 until numBars).map { barIndex ->
            //Log.d("barsBuilder-info-space", "     ")
            //Log.d("barsBuilder-info", "- Bar index: " + barIndex)
            Bars(
                label = profilData[0].results[index].facets[barIndex].title,
                values = profilData.mapIndexed { i, profil ->
                    //Log.d("barsBuilder-info", "-- barInx: " + barIndex + ", index: " + index + ", profil: " + profil.score[index][barIndex])
                    Bars.Data(
                        label = profil.name,
                        value = profil.results[index].facets[barIndex].score.toDouble(),
                        color = color[i],
                    )
                }
            )
        }
        return barsData
    }
}