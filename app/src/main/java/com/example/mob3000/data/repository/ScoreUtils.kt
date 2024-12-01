package com.example.mob3000.data.repository

import android.util.Log
import androidx.compose.ui.graphics.SolidColor
import com.example.mob3000.data.models.ApiData.Result
import com.example.mob3000.data.models.Person
import com.example.mob3000.data.models.Score
import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreList
import ir.ehsannarmani.compose_charts.models.Bars

/**
 *
 * Inneholder hjelpefunksjoner for behandlige av score til sammenligning
 */
object ScoreUtils{
    /**
     * Limer sammen resultat fra en test med en person
     * Legger også inn ett ekstra "Del-Tema" som egentlig er hovedkategoriene, slik at
     * blir lettere å vise det frem i sammenligning
     *
     * @param scores Resultatene fra en test
     * @param person Personen som resultatene gjelder
     * @return ScoreList med sammenlignes data for en person/profil
     */
    fun lagChartsData(scores: List<Result>, person: Person): ScoreList {
        // det er i klassen (Result) som vi ikke tenger, så tar det med ikke vidre

        val tempScoreData = mutableListOf<ScoreData>()

        // legger inn en ekstra facet som er hovedscore, bruker dummydata for domain og "Score"
        val totalScore = mutableListOf<Score>()
        for(s in scores){
            totalScore.add(Score(s.score, s.title))
        }

        // heldivis trenger vi ikke å oversette "Big 5"
        tempScoreData.add(ScoreData("T", Score(0, "Big 5"), totalScore))

        // legger inn resternde facet inn som Scoredata listen
        for(i in scores.indices){
            val facet = mutableListOf<Score>()
            for(s in scores[i].facets){
                facet.add(Score(s.score, s.title))
            }
            tempScoreData.add(ScoreData(scores[i].domain, Score(scores[i].score, scores[i].title), facet))
        }

        // returnerer med navn og resultatene
        return ScoreList(person.name, tempScoreData)
    }

    /**
     * Beregner hvor søylene i diagrammet skal være samt andre verdier, farge, navn
     * Bruker index for å vise til hvilket datasett (Big5, nevro.. osv) som skal brukes
     *
     * @param profilData Liste av ScoreList med data for alle personer/profiler som skal sammenlignes
     * @param index Index for hvilket datasett som skal brukes
     * @param color Liste med farger, en for hver profil
     * @return Liste med Bars, data til flersøyle-diagrammet
     *
     * Vidre forklaring:
     * - numBars er antall søyle-samling i diagrammet, altså hvor mange "del-temaer" det er
     * - Bars sin label er navnet til "del-temaet"
     * - Bars.data sin label er navnet til personen
     * - Bars.data sin value er scoren til personen, høyden til søylen
     */
    fun barsBuilder (profilData: List<ScoreList>, index: Int, color: List<SolidColor>): List<Bars> {
        // antall søyler for dette diagrammet
        val numBars = profilData[0].results[index].facets.size

        // lager ferdig data for hva som skal vises i diagrammet
        val barsData = (0 until numBars).map { barIndex ->
            Bars(
                label = profilData[0].results[index].facets[barIndex].title,
                values = profilData.mapIndexed { i, profil ->
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

    /**
     * Funksjonen endrer på formatet vi mottar fra API
     * Api har html tagger for linje deling returnerer derfor en liste med linjer
     *
     * @param tekst Teksten som skal formateres
     * @return Liste med formaterte linjer
     */
    fun apiTekstRydder(tekst: String): List<String>{
        //viser seg at formatet fra Api kommer med litt forskjellige måter å dele opp tekst
        // formaterer først bort break line tag, deler tekst opp i linjer
        val nyTekst = tekst.replace("<br/>", "<br />")
        val linjer = nyTekst.split("<br /><br />")

        // tar hensyn til alle forskjellige måter /n blir skrevet men lager bare ett mellomrom
        // siden /n ikke samsvarer med linjeskift i tekst, tar bor ekstra mellomrom
        val nyeLinjer = linjer.map{ linje ->
            linje
                .replace("\n \n ", " ")
                .replace("\n\n", " ")
                .replace("\n", " ")
                .trim()
        }

        // returnerer nye linjer
        return nyeLinjer
    }
}