package com.example.mob3000.data.firebase

import com.example.mob3000.data.models.Bruker
import com.google.firebase.auth.FirebaseAuth

/**
 * Objekt som inneholder autentiseringsfunksjoner til Firebase Authentication.
 * Oppretter en instans av FirebaseAuth som kobling til Firebase Authentication.
 */
object AuthService {
    // Firebase Authentication instans
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Registrerer en ny bruker med Firebase Authentication.
     * Firebase Authentication håndterer sikkerhet til data.
     * Vi har valgt å bare ha registrering med epost og passord.
     * Bruker firestoreService for å legge til ny bruker i Firestore.
     *
     * @param email Epost-adresse for brukeren
     * @param password Passord for brukeren
     * @param onSuccess Callback som blir kalt hvis registrering er vellykket
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved registrering
     */
    fun registrerBruker(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if(email.isEmpty() || password.length <6) {
            onFailure("Ikke godtatt epost eller passord. Passord må være minst 6 tegn langt.")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    if(user!= null) {
                        val bruker = Bruker(id = user.uid, email = user.email ?: "")
                        // legger ny bruker inn i database
                        FirestoreService.leggTilBruker(bruker,
                            onSuccess= { onSuccess()},
                            onFailure = {exception ->
                                onFailure("Feilet med å legge til bruker i FireStore: ${exception.localizedMessage}")
                            }
                        )
                    } else {
                        onFailure("User registrering suksess, men brukerdata er null")
                    }
                } else {
                    onFailure(task.exception?.localizedMessage?: "Registrering feilet.")
                }
            }
    }

    /**
     * Logg inn en eksisterende bruker med Firebase Authentication.
     * Firebase Authentication håndterer autentiseringslogikken, samt sikkerhet til data.
     *
     * @param email Epost-adresse for brukeren
     * @param password Passord for brukeren
     * @param onSuccess Callback som blir kalt hvis logg inn er vellykket
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved logg inn
     */
    fun logginnBruker(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.localizedMessage ?: "Logg inn feilet.")
                }
            }
    }

    /**
     * Logg ut en bruker med Firebase Authentication.
     *
     */
    fun loggUt() {
        auth.signOut()
    }


}