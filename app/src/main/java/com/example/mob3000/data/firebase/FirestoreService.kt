package com.example.mob3000.data.firebase

import com.example.mob3000.data.models.Bruker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.example.mob3000.data.models.Person

/**
 * Objekt som inneholder CRUD opperasjoner til Firestore.
 * Oppretter en instanse av FirebaseFirestore som kobling til Firestore.
 */
object FirestoreService {
    // Firebase Firestore instans
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Henter en liste med personer fra collection "Personer" i Firestore.
     * Henter først hvilken bruker som er logget inn for å finne ut hvilke profiler som er
     * kobler til innlogget bruker
     *
     * @param onSuccess Callback som blir kalt med en liste av personer som er hentet fra Firestore
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved henting av data
     * @return
     */
    fun hentPersoner(onSuccess: (List <Person>) -> Unit, onFailure: (Exception) -> Unit) {
        // Henter innlogget bruker
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        // query for å finne alle personene som er kobler til innlogget bruker
        firestore.collection("Personer")
            .whereEqualTo("userId", userId)
            .addSnapshotListener{ snapshot, error ->
                if(error != null) {
                    // error håndtering
                    onFailure(error)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty) {
                    // konverterer resultat til en liste av Person-objekter
                    val personList = snapshot.documents.map { document ->
                        // legger til dokument id for hver dokument, i tilfelle det ikke allerede finnes i dokumentet
                        val person = document.toObject(Person::class.java)
                        person?.copy(documentId = document.id)

                    }.filterNotNull()
                    // sender liste med personer tilbake
                    onSuccess(personList)
                } else {
                    // sender tom liste tilbake hvis det ikke finnes noen personer
                    onSuccess(emptyList())
                }
            }
    }

    /**
     * Legger inn et dokument i collection "Personer" i Firestore.
     * Henter innlogget bruker
     * Returnerer en String med dokumentId hvis vi klarte å legge til profilen/personen
     *
     * @param person Person som skal legges til i Firestore
     * @param onSuccess Callback som blir kalt med en String som er dokumentId
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved innsending av data
     */
    fun leggTilPerson (person: Person, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        // Henter innlogget bruker
        val currentUser = FirebaseAuth.getInstance().currentUser
        val personMedUserId = person.copy(userId = currentUser?.uid ?: "")
        // variabel for dokument id, denne returneres med onSuccsess
        var autoId = ""

        firestore.collection("Personer")
            .add(personMedUserId)
            .addOnSuccessListener{
                    documentReferanse ->
                // sender dokumentID
                autoId = documentReferanse.id
                onSuccess(autoId)}
            .addOnFailureListener{exception -> onFailure(exception)}
    }

    /**
     * Legger inn et dokument i collection "Brukere" i Firestore.
     * Dokumentet er info om ny bruker som er registrert
     *
     * @param bruker Bruker som skal legges til i Firestore
     * @param onSuccess Callback som blir kalt med en String som er dokumentId
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved innsending av data
     */
    fun leggTilBruker(bruker: Bruker, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit ) {
        firestore.collection("Brukere")
            .add(bruker)
            .addOnSuccessListener{documentReference ->
                val autoId = documentReference.id
                onSuccess(autoId)
            }
            .addOnFailureListener{exception -> onFailure(exception)}
    }

    /**
     * Sletter et dokument i collection "Personer" i Firestore.
     * Bruker dokument id for å finne riktig dokument.
     *
     * @param person Person som skal slettes
     * @param onSuccess Callback som blir kalt hvis sletting er vellykket
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved sletting
     */
    fun slettPerson (person: Person, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .document(person.documentId)
            .delete()
            .addOnSuccessListener{
                onSuccess()
            }
            .addOnFailureListener{ exception ->
                onFailure(exception)
            }
    }

    /**
     * Oppdaterer et dokument i collection "Personer" i Firestore.
     * Bruker dokument id for å finne riktig dokument.
     *
     * @param person Person som skal oppdateres
     * @param onSuccess Callback som blir kalt hvis oppdatering er vellykket
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved oppdatering
     */
    fun oppdaterPerson(person: Person, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .document(person.documentId)
            .set(person)
            .addOnSuccessListener{
                onSuccess()
            }
            .addOnFailureListener{ exception ->
                onFailure(exception)
            }
    }

    /**
     * Henter antall dokumenter i collection "Personer" i Firestore.
     * Isteden for å ha uid som innparameter, bruker vi FirebaseAuth for å hente innlogget bruker
     *
     * @param onResult Callback som blir kalt med antall dokumenter som er hentet
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved henting av data
     */
    fun hentAntallDokumenter(onResult: (Int) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userId != null) {
            firestore.collection("Personer")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener{documents ->
                    val antall = documents.size()
                    onResult(antall)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("Bruker er ikke logget inn."))
        }
    }

    /**
     * Legger til dokumentId i collection "Personer" i Firestore.
     * Bruker id (dokumentID) for å finne riktig dokument.
     *
     * @param id Id som skal legges til i Firestore - dokumentID
     * @param onSuccess Callback som blir kalt hvis innsending er vellykket
     * @param onFailure Callback som blir kalt hvis det oppstår en feil ved innsending
     */
    fun leggTilDocRefPerson(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .document(id)
            .update("documentId", id)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}