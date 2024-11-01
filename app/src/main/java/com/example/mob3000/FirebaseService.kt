package com.example.mob3000

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.mob3000.Person

object FirebaseService {
    private val firestore = FirebaseFirestore.getInstance()

    fun hentPersoner(onSuccess: (List <Person>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .addSnapshotListener{ snapshot, error ->
                if(error != null) {
                    onFailure(error)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty) {
                    val personList = snapshot.toObjects(Person::class.java)
                    onSuccess(personList)
                } else {
                    onSuccess(emptyList())
                }
            }
    }
    fun leggTilPerson (person: Person, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Personer")
            .add(person)
            .addOnSuccessListener{
                    documentReferanse ->
                val autoId = documentReferanse.id
                onSuccess(autoId)}
            .addOnFailureListener{exception -> onFailure(exception)}
    }
}

object AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registrerBruker (email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (Exception) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception ?: Exception("Registrering feilet"))
                }
            }
    }

    fun logginnBruker(email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (Exception) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception ?: Exception("Logg inn feilet."))
                }
            }
    }
}

