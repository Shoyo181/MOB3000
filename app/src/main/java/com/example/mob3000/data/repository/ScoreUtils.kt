package com.example.mob3000.data.repository

import android.util.Log
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.models.Person
import com.example.mob3000.data.models.Score
import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreList

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
}