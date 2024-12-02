package com.example.mob3000.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objekt som inneholder nettverksrelaterte konfigurasjoner og gir tilgang til API-tjenesten.
 *
 * Dette objektet definerer base-URL-en for API-en og initialiserer en Retrofit-instans
 * for å utføre nettverkskall. Det gir en `apiService`-egenskap av typen [ApiService] som kan brukes til å
 * få tilgang til API-endefunksjon
 */
object Nettverksmodul {
    // base link til APIet
    private const val BASE_URL = "https://bigfive-f9cymyeb1-rubynor.vercel.app/"

    /**
     * En instans av [ApiService] som brukes til å utføre nettverkskall.
     *
     * Denne egenskapen initialiseres lat/lazy, noe som betyr at den først opprettes
     * når den blir brukt første gang. Den gir tilgang til API-endefunksjoner definert i [ApiService],
     */
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}