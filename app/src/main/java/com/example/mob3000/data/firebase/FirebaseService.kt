package com.example.mob3000.data.firebase

import com.example.mob3000.data.models.Bruker
import com.example.mob3000.data.firebase.FirebaseService.leggTilBruker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.example.mob3000.data.models.Person

object FirebaseService {
    private val firestore = FirebaseFirestore.getInstance()

    fun hentPersoner(onSuccess: (List <Person>) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        firestore.collection("Personer")
            .whereEqualTo("userId", userId)
            .addSnapshotListener{ snapshot, error ->
                if(error != null) {
                    onFailure(error)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty) {
                    val personList = snapshot.documents.map { document ->
                        val person = document.toObject(Person::class.java)
                        person?.copy(documentId = document.id)
                    }.filterNotNull()

                    onSuccess(personList)
                } else {
                    onSuccess(emptyList())
                }
            }
    }

    fun leggTilPerson (person: Person, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val personMedUserId = person.copy(userId = currentUser?.uid ?: "")

        firestore.collection("Personer")
            .add(personMedUserId)
            .addOnSuccessListener{
                    documentReferanse ->
                val autoId = documentReferanse.id
                onSuccess(autoId)}
            .addOnFailureListener{exception -> onFailure(exception)}
    }

    fun leggTilBruker(bruker: Bruker, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit ) {

        firestore.collection("Brukere")
            .add(bruker)
            .addOnSuccessListener{documentReference ->
                val autoId = documentReference.id
                onSuccess(autoId)
            }
            .addOnFailureListener{exception -> onFailure(exception)}
    }

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
    fun hentAntallDokumenter(onResult: (Int) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userId != null) {
            FirebaseFirestore.getInstance().collection("Personer")
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
}


object AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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

                        leggTilBruker(bruker,
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
    fun loggUt(    ) {
        auth.signOut()
    }


}
